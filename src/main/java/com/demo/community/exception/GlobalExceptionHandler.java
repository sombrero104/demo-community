package com.demo.community.exception;

import com.demo.community.common.dto.CommonResponseDto;
import com.demo.community.account.exception.AccountAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<? extends Exception>, HttpStatus> exceptionStatusMap = new HashMap<>();

    static {
        exceptionStatusMap.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(AccountAlreadyExistsException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(ResponseStatusException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(AuthenticationException.class, HttpStatus.UNAUTHORIZED);
        exceptionStatusMap.put(UsernameNotFoundException.class, HttpStatus.UNAUTHORIZED);
        exceptionStatusMap.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CommonResponseDto> handleException(Exception e) {
        HttpStatus status = exceptionStatusMap.getOrDefault(e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(new CommonResponseDto(e.getMessage()), status);
    }

}
