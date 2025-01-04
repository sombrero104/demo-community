package com.demo.community.board.service;

import com.demo.community.account.repository.AccountRepository;
import com.demo.community.account.entity.Account;
import com.demo.community.account.role.AccountRole;
import com.demo.community.board.dto.CommentRequestDto;
import com.demo.community.board.dto.CommentResponseDto;
import com.demo.community.board.entity.Board;
import com.demo.community.board.entity.Comment;
import com.demo.community.board.repository.BoardRepository;
import com.demo.community.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentList(Long boardId, int commentPage, int commentSize) {
        Pageable commentPageable = PageRequest.of(commentPage, commentSize);
        List<Comment> comments = commentRepository.findCommentsByBoardId(boardId, commentPageable);

        return comments.stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    public Comment createComment(Long boardId, CommentRequestDto commentRequestDto, String currentUserEmail) {
        Account account = accountRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No account exists."));
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + boardId));

        Comment comment = commentRequestDto.toEntity();
        comment.setBoard(board);
        comment.setAuthor(account);
        return commentRepository.save(comment);
    }

    public void updateComment(Long id, CommentRequestDto commentRequestDto, String currentUserEmail) {
        Account account = accountRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No account exists."));
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + id));

        if(account.equals(comment.getAuthor())) {
            comment.setContent(commentRequestDto.getContent());
            commentRepository.save(comment);
        } else {
            throw new AccessDeniedException("Access denied.");
        }
    }

    public void deleteComment(Long id, String currentUserEmail) {
        Account account = accountRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No account exists."));
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found with id: " + id));

        if(account.equals(comment.getAuthor()) || account.getRoles().contains(AccountRole.ADMIN)) {
            commentRepository.delete(comment);
        } else {
            throw new AccessDeniedException("Access denied.");
        }
    }

}
