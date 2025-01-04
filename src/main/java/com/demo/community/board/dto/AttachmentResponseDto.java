package com.demo.community.board.dto;

import com.demo.community.board.entity.Attachment;
import lombok.Data;

@Data
public class AttachmentResponseDto {

    private Long id;

    private String fileName;

    public AttachmentResponseDto(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
    }

}
