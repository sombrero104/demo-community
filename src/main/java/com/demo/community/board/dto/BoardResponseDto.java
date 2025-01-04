package com.demo.community.board.dto;

import com.demo.community.board.entity.Board;
import com.demo.community.common.util.MaskingSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;

    private String title;

    @JsonSerialize(using = MaskingSerializer.class)
    private String authorName;

    private String createdDate;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.authorName = (board.getAuthor() != null) ? board.getAuthor().getNickname() : null;
        this.createdDate = board.getCreatedDate().toString();
    }

}
