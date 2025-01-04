package com.demo.community.board.service;

import com.demo.community.account.entity.Account;
import com.demo.community.account.repository.AccountRepository;
import com.demo.community.account.role.AccountRole;
import com.demo.community.board.dto.*;
import com.demo.community.board.entity.Attachment;
import com.demo.community.board.entity.Board;
import com.demo.community.board.entity.Comment;
import com.demo.community.board.repository.BoardRepository;
import com.demo.community.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final AttachmentService attachmentService;

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepository.findAll(pageable);

        return boards.stream()
                .map(BoardResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoard(Long id, int commentPage, int commentSize) {
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + id));
        List<Comment> comments = commentRepository.findCommentsByBoardId(id, PageRequest.of(commentPage, commentSize));

        BoardDetailResponseDto boardDetailResponseDto = new BoardDetailResponseDto(board);
        boardDetailResponseDto.setFiles(getAttachmentResponseDtos(board.getAttachments()));
        boardDetailResponseDto.setComments(getCommentResponseDtos(comments));
        return boardDetailResponseDto;
    }

    @Transactional
    public Board createBoard(BoardRequestDto boardRequestDto, List<MultipartFile> files, String currentUserEmail) throws IOException {
        Account account = accountRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No account exists."));

        Board board = boardRequestDto.toEntity();
        board.setAuthor(account);
        Board savedBoard = boardRepository.save(board);
        attachmentService.uploadAttachments(savedBoard, files);

        return savedBoard;
    }

    @Transactional
    public void updateBoard(Long id, BoardRequestDto boardRequestDto, List<MultipartFile> files, String currentUserEmail) throws IOException {
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + id));

        checkPermission(currentUserEmail, board.getAuthor());
        attachmentService.deleteAttachments(boardRequestDto.getDeleteFileIds());
        attachmentService.uploadAttachments(board, files);

        board.setTitle(boardRequestDto.getTitle());
        board.setContent(boardRequestDto.getContent());
        boardRepository.save(board);
    }

    public void deleteBoard(Long id, String currentUserEmail) {
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + id));

        checkPermission(currentUserEmail, board.getAuthor());
        boardRepository.delete(board);
    }

    private List<CommentResponseDto> getCommentResponseDtos(List<Comment> comments) {
        if (comments != null) {
            return comments.stream()
                    .map(CommentResponseDto::new)
                    .toList();
        }
        return Collections.emptyList();
    }

    private List<AttachmentResponseDto> getAttachmentResponseDtos(List<Attachment> attachments) {
        if (attachments != null) {
            return attachments.stream()
                    .map(AttachmentResponseDto::new)
                    .toList();
        }
        return Collections.emptyList();
    }

    private void checkPermission(String currentUserEmail, Account author) {
        Account currentAccount = accountRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No account exists."));
        if (!hasPermission(currentAccount, author)) {
            throw new AccessDeniedException("Access denied.");
        }
    }

    private boolean hasPermission(Account currentAccount, Account author) {
        return currentAccount.equals(author) || currentAccount.getRoles().contains(AccountRole.ADMIN);
    }

}
