package com.demo.community.board.controller;

import com.demo.community.account.annotation.CurrentUserName;
import com.demo.community.board.dto.BoardDetailResponseDto;
import com.demo.community.board.dto.BoardRequestDto;
import com.demo.community.board.dto.BoardResponseDto;
import com.demo.community.board.entity.Board;
import com.demo.community.board.service.BoardService;
import com.demo.community.common.dto.CommonResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequestMapping("/api/board")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BoardResponseDto>> getBoardList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        List<BoardResponseDto> boards = boardService.getBoardList(page, size);
        return ResponseEntity.ok().body(boards);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BoardDetailResponseDto> getBoard(@PathVariable Long id,
                                                           @RequestParam(value = "commentPage", defaultValue = "0") int commentPage,
                                                           @RequestParam(value = "commentSize", defaultValue = "5") int commentSize) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoard(id, commentPage, commentSize);
        return ResponseEntity.ok().body(boardDetailResponseDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommonResponseDto> createBoard(@RequestPart(value = "board") @Valid BoardRequestDto boardRequestDto,
                                                         @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                         @CurrentUserName String currentUserEmail) throws IOException {
        Board board = boardService.createBoard(boardRequestDto, files, currentUserEmail);
        return ResponseEntity.created(URI.create("/api/board/" + board.getId()))
                .body(new CommonResponseDto("Successfully created."));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommonResponseDto> updateBoard(@PathVariable Long id,
                                                         @RequestPart(value = "board") @Valid BoardRequestDto boardRequestDto,
                                                         @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                         @CurrentUserName String currentUserEmail) throws IOException {
        boardService.updateBoard(id, boardRequestDto, files, currentUserEmail);
        return ResponseEntity.ok().body(new CommonResponseDto("Successfully updated."));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommonResponseDto> deleteBoard(@PathVariable Long id,
                                                         @CurrentUserName String currentUserEmail) {
        boardService.deleteBoard(id, currentUserEmail);
        return ResponseEntity.ok().body(new CommonResponseDto("Successfully deleted."));
    }

}
