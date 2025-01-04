package com.demo.community.board.dto;

import com.demo.community.board.entity.Comment;
import lombok.Data;

@Data
public class CommentResponseDto {

    private Long id;

    private String content;

    private String authorName;

    private String createdDate;

    private String modifiedDate;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorName = (comment.getAuthor() != null) ? comment.getAuthor().getNickname() : null;
        this.createdDate = comment.getCreatedDate().toString();
        this.modifiedDate = comment.getModifiedDate().toString();
    }

}
