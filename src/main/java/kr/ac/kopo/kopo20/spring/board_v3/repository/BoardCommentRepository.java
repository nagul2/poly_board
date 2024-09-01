package kr.ac.kopo.kopo20.spring.board_v3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import kr.ac.kopo.kopo20.spring.board_v3.domain.BoardComment;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Integer>, JpaSpecificationExecutor<BoardComment> {

    @Query("SELECT c FROM BoardComment c " +
           "LEFT JOIN BoardComment parent ON c.parentCommentUserName = parent.commentUserName " +
           "WHERE c.board.id = :boardId " +
           "ORDER BY COALESCE(parent.id, c.id) DESC, c.commentLevel ASC, c.id ASC")
    List<BoardComment> findCommentsByBoardIdOrderByParentAndId(@Param("boardId") int boardId);

    @Query("SELECT c FROM BoardComment c " +
           "LEFT JOIN BoardComment parent ON c.parentCommentUserName = parent.commentUserName " +
           "WHERE c.board.id = :boardId " +
           "ORDER BY COALESCE(parent.id, c.id) DESC, c.commentLevel ASC, c.id ASC")
    Page<BoardComment> findCommentsByBoardIdOrderByParentAndId(@Param("boardId") int boardId, Pageable pageable);

    Optional<BoardComment> findByCommentUserName(String commentUserName);
    
//    @Query("SELECT c FROM BoardComment c WHERE c.commentUserName = :commentUserName AND c.board.id = :boardId")
//    List<BoardComment> findByCommentUserNameAndBoardId(@Param("commentUserName") String commentUserName, @Param("boardId") int boardId);
    

    // 특정 댓글과 그에 속한 대댓글을 모두 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM BoardComment c WHERE c.commentUserName = :commentUserName OR c.parentCommentUserName = :commentUserName")
    void deleteByCommentUserNameOrParentCommentUserName(@Param("commentUserName") String commentUserName);
    
    @Query("SELECT c FROM BoardComment c WHERE c.parentCommentUserName = :parentCommentUserName")
    List<BoardComment> findByParentCommentUserName(@Param("parentCommentUserName") String parentCommentUserName);



}

