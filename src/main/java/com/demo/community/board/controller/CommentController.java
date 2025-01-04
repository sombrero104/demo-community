package com.demo.community.board.controller;

import com.demo.community.account.annotation.CurrentUserName;
import com.demo.community.board.service.CommentService;
import com.demo.community.board.dto.CommentRequestDto;
import com.demo.community.board.dto.CommentResponseDto;
import com.demo.community.board.entity.Comment;
import com.demo.community.common.dto.CommonResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CommentResponseDto>> getCommentList(@PathVariable Long boardId,
                                                                   @RequestParam(value = "page", defaultValue = "0") int commentPage,
                                                                   @RequestParam(value = "size", defaultValue = "5") int commentSize) {
        List<CommentResponseDto> comments = commentService.getCommentList(boardId, commentPage, commentSize);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @PreAuthorize("hasRole('SPECIALIST')")
    public ResponseEntity<CommonResponseDto> createComment(@PathVariable Long boardId,
                                                           @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                           @CurrentUserName String currentUserEmail) {
        Comment comment = commentService.createComment(boardId, commentRequestDto, currentUserEmail);
        return ResponseEntity.created(URI.create("/api/board/" + boardId + "/comment/" + comment.getId()))
                .body(new CommonResponseDto("Successfully created."));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SPECIALIST')")
    public ResponseEntity<CommonResponseDto> updateComment(@PathVariable Long id,
                                                           @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                           @CurrentUserName String currentUserEmail) {
        commentService.updateComment(id, commentRequestDto, currentUserEmail);
        return ResponseEntity.ok().body(new CommonResponseDto("Successfully updated."));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SPECIALIST')")
    public ResponseEntity<CommonResponseDto> deleteComment(@PathVariable Long id,
                                                           @CurrentUserName String currentUserEmail) {
        commentService.deleteComment(id, currentUserEmail);
        return ResponseEntity.ok().body(new CommonResponseDto("Successfully deleted."));
    }

}
