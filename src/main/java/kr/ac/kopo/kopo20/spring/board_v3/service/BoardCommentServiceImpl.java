package kr.ac.kopo.kopo20.spring.board_v3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.ac.kopo.kopo20.spring.board_v3.domain.BoardComment;
import kr.ac.kopo.kopo20.spring.board_v3.dto.BoardPagination;
import kr.ac.kopo.kopo20.spring.board_v3.repository.BoardCommentRepository;

@Service
public class BoardCommentServiceImpl implements BoardCommentService {

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Override
    public BoardPagination getCommentPagination(int page, int sizePerCommentPage, int boardId) {
        Pageable pageable = PageRequest.of(page - 1, sizePerCommentPage);
        Page<BoardComment> commentPage = boardCommentRepository.findCommentsByBoardIdOrderByParentAndId(boardId, pageable);

        int totalPages = commentPage.getTotalPages();
        int currentPage = commentPage.getNumber() + 1;
        int pageSize = commentPage.getSize();

        // 페이지네이션 계산
        int pageGroupSize = 5;  // 한 번에 표시할 페이지 번호의 개수
        int startPage = ((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

        int pp = 1;  // 첫 페이지
        int p = currentPage > 1 ? currentPage - 1 : 1;  // 이전 페이지
        int n = currentPage < totalPages ? currentPage + 1 : totalPages;  // 다음 페이지
        int nn = totalPages;  // 마지막 페이지

        BoardPagination pagination = new BoardPagination();
        pagination.setPp(pp);
        pagination.setP(p);
        pagination.setN(n);
        pagination.setNn(nn);
        pagination.setCp(currentPage);
        pagination.setStartPage(startPage);
        pagination.setEndPage(endPage);
        pagination.setPageSize(pageSize);
        pagination.setTotalPages(totalPages);

        pagination.setComments(commentPage.getContent()); // 댓글 리스트 설정

        return pagination;
    }

    @Override
    public BoardComment addComment(BoardComment comment) {
        comment.setDate(new java.util.Date());  // 댓글 작성 시간을 현재 시간으로 설정
        return boardCommentRepository.save(comment);
    }

    @Override
    public BoardComment updateComment(String commentUserName, String content) {
        Optional<BoardComment> commentOptional = boardCommentRepository.findByCommentUserName(commentUserName);
        if (commentOptional.isPresent()) {
            BoardComment comment = commentOptional.get();
            comment.setContent(content);
            return boardCommentRepository.save(comment);
        }
        return null;
    }

    @Override
    public boolean deleteComment(String commentUserName) {
        Optional<BoardComment> commentOptional = boardCommentRepository.findByCommentUserName(commentUserName);
        if (commentOptional.isPresent()) {
            BoardComment comment = commentOptional.get();
            // 대댓글이 있는 경우 대댓글도 삭제
            List<BoardComment> replies = boardCommentRepository.findByParentCommentUserName(commentUserName);
            for (BoardComment reply : replies) {
                boardCommentRepository.delete(reply);
            }
            boardCommentRepository.delete(comment);
            return true;
        }
        return false;
    }


    @Override
    public Optional<BoardComment> findByCommentUserName(String commentUserName) {
        return boardCommentRepository.findByCommentUserName(commentUserName);
    }
}
