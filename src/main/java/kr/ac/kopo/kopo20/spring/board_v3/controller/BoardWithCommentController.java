package kr.ac.kopo.kopo20.spring.board_v3.controller;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.kopo20.spring.board_v3.domain.Board;
import kr.ac.kopo.kopo20.spring.board_v3.domain.BoardComment;
import kr.ac.kopo.kopo20.spring.board_v3.dto.BoardPagination;
import kr.ac.kopo.kopo20.spring.board_v3.service.BoardCommentService;
import kr.ac.kopo.kopo20.spring.board_v3.service.BoardService;

@Controller
public class BoardWithCommentController {

    @Autowired
    private BoardService boardService;
    
    @Autowired
    private BoardCommentService boardCommentService;

    @GetMapping("")
    public String redirect(HttpServletRequest request) {
//        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        return "redirect:" + baseUrl + "/list?page=1&sizePerpage=10";
        return "redirect:/list?page=1&sizePerpage=10";
    }
    

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value="page", defaultValue="1") Integer page,
                       @RequestParam(value="sizePerpage", defaultValue="10") Integer sizePerpage,
                       HttpSession session) {

        session.setAttribute("page", page);
        session.setAttribute("sizePerpage", sizePerpage);

        PageRequest pageable = PageRequest.of(page-1, sizePerpage, Sort.by(Sort.Direction.DESC, "id"));

        Page<Board> boardItems = boardService.findAll(pageable);
        model.addAttribute("boardItems", boardItems);

        BoardPagination pagination = boardService.getPagination(page, sizePerpage);
        model.addAttribute("pagination", pagination);

        return "List";
    }

    @GetMapping("/view/{id}")
    public String oneViewWithComment(Model model, @PathVariable int id,
                                     @RequestParam(value = "commentPage", required = false, defaultValue = "1") int commentPage,
                                     @RequestParam(value = "sizePerCommentPage", required = false, defaultValue = "10") int sizePerCommentPage,
                                     RedirectAttributes redirectAttributes,
                                     HttpSession session) {

        Integer page = (Integer) session.getAttribute("page");
        Integer sizePerpage = (Integer) session.getAttribute("sizePerpage");

        var boardItemOptional = boardService.findById(id);

        if (!boardItemOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "해당 게시글을 찾을 수 없습니다.");
            return "redirect:/list";
        }

        Board boardItem = boardItemOptional.get();

        // 조회수 증가 시키고 DB에 저장하기
        boardItem.setCount(boardItem.getCount() + 1);
        boardService.saveBoard(boardItem);

        model.addAttribute("page", page);
        model.addAttribute("sizePerpage", sizePerpage);
        model.addAttribute("id", boardItem.getId());
        model.addAttribute("title", boardItem.getTitle());
        model.addAttribute("boardUserName", boardItem.getBoardUserName());
        model.addAttribute("date", boardItem.getDate());
        model.addAttribute("content", boardItem.getContent());
        model.addAttribute("count", boardItem.getCount());

        // 페이지네이션 된 댓글 리스트 가져오기
        var pagination = boardCommentService.getCommentPagination(commentPage, sizePerCommentPage, id);
        model.addAttribute("comments", pagination.getComments());
        model.addAttribute("pagination", pagination);

        return "View";
    }


    @GetMapping("/create")
    public String insertForm(Model model) {
        return "Insert";
    }

    @PostMapping("/create")
    public String saveContent(Board board, RedirectAttributes redirectAttributes) {
        Logger logger = LoggerFactory.getLogger(BoardWithCommentController.class);

        if (board.getTitle() == null || board.getTitle().trim().isEmpty() ||
            board.getBoardUserName() == null || board.getBoardUserName().trim().isEmpty() ||
            board.getContent() == null || board.getContent().trim().isEmpty()) {

            redirectAttributes.addFlashAttribute("errorMessage", "제목, 작성자, 내용을 모두 입력해 주세요");
            return "redirect:/create";
        }

        if (board.getBoardUserName().length() > 20) {
            redirectAttributes.addFlashAttribute("errorMessage", "작성자명은 한글 20자까지 입력 가능합니다.");
            return "redirect:/create";
        }

        if (board.getContent().length() > 500) {
            redirectAttributes.addFlashAttribute("errorMessage", "내용은 500자까지 입력 가능합니다");
            return "redirect:/create";
        }

        board.setCount(1);
        board.setDate(new Date());
        Board savedBoard = boardService.saveBoard(board); // 저장된 게시글을 반환받음

        redirectAttributes.addFlashAttribute("creationSuccess", true);
        logger.info("Redirecting to view page with ID: " + savedBoard.getId());
        return "redirect:/view/" + savedBoard.getId();
    }

    @GetMapping("/update")
    public String updateForm(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Board> boardItemOptional = boardService.findById(id);

        if (!boardItemOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
            return "redirect:/list";  // 게시글 목록 페이지로 리다이렉트
        }

        Board boardItem = boardItemOptional.get();
        model.addAttribute("boardItem", boardItem);
        return "Update";  // 업데이트 폼을 보여주는 JSP 페이지 이름
    }

    @PostMapping("/update")
    public String updateContent(@RequestParam("id") int id,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                RedirectAttributes redirectAttributes) {

        Board updatedBoard = boardService.updateBoard(id, title, content);

        if (updatedBoard == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시글을 찾을 수 없습니다.");
            return "redirect:/list";
        }

        redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 수정되었습니다.");
        return "redirect:/view/" + id;
    }

    @PostMapping("/delete")
    public String deleteContent(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = boardService.deleteBoard(id);

        if (!isDeleted) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시글을 찾을 수 없습니다.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 삭제되었습니다.");
        }

        return "redirect:/list";
    }
    
    // 댓글 컨트롤러
    
    // 댓글 생성
    @PostMapping("/comment/add")
    public String addComment(@RequestParam("boardId") int boardId,
                             @RequestParam("commentUserName") String commentUserName,
                             @RequestParam("commentContent") String commentContent,
                             @RequestParam(value = "parentCommentUserName", required = false) String parentCommentUserName,
                             RedirectAttributes redirectAttributes) {
        BoardComment comment = new BoardComment();
        
        if (parentCommentUserName == null || parentCommentUserName.isEmpty()) {
            // 부모 댓글이 없으므로 comment_level 1로 설정하고, parent_comment_user_name은 본인의 이름으로 설정
            comment.setCommentLevel(1);
            comment.setParentCommentUserName(commentUserName); // 자신의 사용자 이름을 부모로 설정
        } else {
            // 부모 댓글이 존재하므로 comment_level 2로 설정하고, parent_comment_user_name은 부모의 이름으로 설정
            Optional<BoardComment> parentCommentOptional = boardCommentService.findByCommentUserName(parentCommentUserName);
            if (parentCommentOptional.isPresent()) {
                BoardComment parentComment = parentCommentOptional.get();
                comment.setCommentLevel(parentComment.getCommentLevel() + 1);
                comment.setParentCommentUserName(parentCommentUserName); // 부모 댓글의 사용자 이름 설정
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "부모 댓글을 찾을 수 없습니다.");
                return "redirect:/view/" + boardId;
            }
        }

        // 해당 게시글 가져오기
        Optional<Board> boardOptional = boardService.findById(boardId);
        if (!boardOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시글을 찾을 수 없습니다.");
            return "redirect:/list";
        }
        
        // 댓글 정보 설정
        comment.setBoard(boardOptional.get());
        comment.setCommentUserName(commentUserName);
        comment.setContent(commentContent);
        comment.setDate(new Date());
        
        // 댓글 저장
        boardCommentService.addComment(comment);
        redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 추가되었습니다.");

        return "redirect:/view/" + boardId;
    }

    // 댓글 수정
    @PostMapping("/comment/edit")
    public String editComment(@RequestParam("commentUserName") String commentUserName,
                              @RequestParam("commentContent") String commentContent,
                              RedirectAttributes redirectAttributes) {
        BoardComment updatedComment = boardCommentService.updateComment(commentUserName, commentContent);
        
        if (updatedComment == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 댓글을 찾을 수 없습니다.");
            return "redirect:/list";  // 게시글을 찾을 수 없는 경우 리스트로 리다이렉트
        }

        redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 수정되었습니다.");
        return "redirect:/view/" + updatedComment.getBoard().getId();
    }

    @PostMapping("/comment/delete")
    public String deleteComment(@RequestParam("commentUserName") String commentUserName,
                                @RequestParam("boardId") int boardId,
                                RedirectAttributes redirectAttributes) {
        boolean isDeleted = boardCommentService.deleteComment(commentUserName);

        if (!isDeleted) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 댓글을 찾을 수 없습니다.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 삭제되었습니다.");
        }

        return "redirect:/view/" + boardId;
    }


}
