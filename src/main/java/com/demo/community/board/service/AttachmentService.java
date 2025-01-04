package com.demo.community.board.service;

import com.demo.community.board.entity.Attachment;
import com.demo.community.board.entity.Board;
import com.demo.community.board.repository.AttachmentRepository;
import com.demo.community.board.repository.BoardRepository;
import com.demo.community.common.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

    @Value("${board.attachment.root-dir}")
    private String attachmentRootDir;

    private final BoardRepository boardRepository;

    private final AttachmentRepository attachmentRepository;

    @Transactional
    public void uploadAttachments(Board board, List<MultipartFile> files) throws IOException {
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                uploadAttachment(board, file);
            }
        }
    }

    public void uploadAttachment(Board board, MultipartFile file) throws IOException {
        if (file != null) {
            String filePath = FileUtil.uploadFile(attachmentRootDir, board.getId().toString(), file);
            Attachment attachment = new Attachment(file.getOriginalFilename(), filePath, board);
            attachmentRepository.save(attachment);
        }
    }

    @Transactional
    public void deleteAttachments(List<Long> deleteFileIds) throws IOException {
        List<Attachment> attachments = attachmentRepository.findAllById(deleteFileIds);
        for (Attachment attachment : attachments) {
            deleteAttachment(attachment);
        }
    }

    public void deleteAttachment(Attachment attachment) throws IOException {
        FileUtil.deleteFile(attachment.getFilePath(), attachment.getFileName());
        attachmentRepository.delete(attachment);
    }

    @Transactional(readOnly = true)
    public Resource downloadAttachment(Long boardId, Long id) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + boardId));
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + id));

        if (board != attachment.getBoard()) {
            throw new IllegalArgumentException("Not a valid attachment for this board.");
        }
        return FileUtil.loadAsResource(attachment.getFilePath());
    }

}
