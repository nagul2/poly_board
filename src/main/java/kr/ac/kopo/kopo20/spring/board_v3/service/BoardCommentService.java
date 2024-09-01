package kr.ac.kopo.kopo20.spring.board_v3.service;

import java.util.Optional;

import kr.ac.kopo.kopo20.spring.board_v3.domain.BoardComment;
import kr.ac.kopo.kopo20.spring.board_v3.dto.BoardPagination;

public interface BoardCommentService {
    BoardPagination getCommentPagination(int page, int sizePerCommentPage, int boardId);
    
    BoardComment addComment(BoardComment comment);
    
    BoardComment updateComment(String commentUserName, String content);
    
    boolean deleteComment(String commentUserName);
    
    Optional<BoardComment> findByCommentUserName(String commentUserName);
}
