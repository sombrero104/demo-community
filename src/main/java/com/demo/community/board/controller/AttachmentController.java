package com.demo.community.board.controller;

import com.demo.community.board.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/api/board/{boardId}/attachment")
@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private static final String CONTENT_DISPOSITION_VALUE = "attachment; filename=\"%s\"";

    private final AttachmentService attachmentService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long boardId,
                                                       @PathVariable Long id) throws IOException {
        Resource resource = attachmentService.downloadAttachment(boardId, id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format(CONTENT_DISPOSITION_VALUE, resource.getFilename()))
                .body(resource);
    }

}
