package com.demo.community.board.dto;

import com.demo.community.board.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "Content cannot be empty.")
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }

}
