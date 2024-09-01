package kr.ac.kopo.kopo20.spring.board_v3.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.ac.kopo.kopo20.spring.board_v3.domain.Board;
import kr.ac.kopo.kopo20.spring.board_v3.domain.BoardComment;
import kr.ac.kopo.kopo20.spring.board_v3.dto.BoardPagination;
import kr.ac.kopo.kopo20.spring.board_v3.repository.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService {
	

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public BoardPagination getPagination(int page, int sizePerPage) {
        BoardPagination pagination = new BoardPagination();

        long totalItems = boardRepository.count();
        int totalPages = (int) Math.ceil((double) totalItems / sizePerPage);

        // 현재 페이지가 범위를 벗어날 경우 처리
        if (page > totalPages) {
            page = totalPages;
        } else if (page < 1) {
            page = 1;
        }

        // 페이지 그룹 계산 (1부터 시작)
        int currentGroup = (page - 1) / 10;
        int startPageGroup = currentGroup * 10 + 1;
        int endPageGroup = Math.min(startPageGroup + 9, totalPages);

        // 이전, 다음 페이지 계산
        int prePage = page > 1 ? page - 1 : 1;
        int nextPage = page < totalPages ? page + 1 : totalPages;

        // 이전, 다음 그룹 계산
        int preGroupPage = currentGroup > 0 ? (startPageGroup - 10) : 1;
        int nextGroupPage = (endPageGroup < totalPages) ? (startPageGroup + 10) : totalPages;

        // 페이징 데이터 설정
        pagination.setPp(preGroupPage);
        pagination.setP(prePage);
        pagination.setN(nextPage);
        pagination.setNn(nextGroupPage);
        pagination.setCp(page);
        pagination.setStartPage(startPageGroup);
        pagination.setEndPage(endPageGroup);
        pagination.setPageSize(sizePerPage);
        pagination.setTotalPages(totalPages);

        return pagination;
    }
    
    @Override
    public Optional<Board> findById(int id) {
        return boardRepository.findById(id);
    }

    @Override
    public Board saveBoard(Board board) {
        board.setDate(new Date());
        return boardRepository.save(board);
    }

    @Override
    public boolean deleteBoard(int id) {
        Optional<Board> board = boardRepository.findById(id);
        if (board.isPresent()) {
            boardRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Board updateBoard(int id, String title, String content) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            board.setTitle(title);
            board.setContent(content);
            return boardRepository.save(board);
        }
        return null;
    }
    

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
    

}
