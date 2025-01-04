package com.demo.community.board.dto;

import com.demo.community.board.entity.Board;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BoardDetailResponseDto extends BoardResponseDto {

    private String content;

    private List<AttachmentResponseDto> files;

    private String modifiedDate;

    private List<CommentResponseDto> comments;

    public BoardDetailResponseDto() {
        super();
    }

    public BoardDetailResponseDto(Board board) {
        super(board);
        this.content = board.getContent();
        this.modifiedDate = board.getModifiedDate().toString();
    }

}
