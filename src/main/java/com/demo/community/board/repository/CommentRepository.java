package com.demo.community.board.repository;

import com.demo.community.board.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId ORDER BY c.createdDate DESC")
    List<Comment> findCommentsByBoardId(@Param("boardId") Long boardId, Pageable pageable);

}
