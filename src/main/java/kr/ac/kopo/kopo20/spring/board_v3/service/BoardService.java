package kr.ac.kopo.kopo20.spring.board_v3.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.ac.kopo.kopo20.spring.board_v3.domain.Board;
import kr.ac.kopo.kopo20.spring.board_v3.dto.BoardPagination;

public interface BoardService {
	 BoardPagination getPagination(int currentPage, int pageSize);
	 Optional<Board> findById(int id);
	 Board saveBoard(Board board);
	 boolean deleteBoard(int id);
	 Board updateBoard(int id, String title, String content);
	 Page<Board> findAll(Pageable pageable);
}
