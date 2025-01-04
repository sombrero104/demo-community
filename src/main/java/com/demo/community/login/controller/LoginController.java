package com.demo.community.login.controller;

import com.demo.community.login.service.LoginService;
import com.demo.community.login.dto.LoginRequestDto;
import com.demo.community.login.dto.LoginResponseDto;
import com.demo.community.common.dto.CommonResponseDto;
import com.demo.community.login.dto.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = loginService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/join")
    public ResponseEntity<CommonResponseDto> join(@RequestBody JoinRequestDto joinRequestDto) {
        CommonResponseDto commonResponseDto = loginService.join(joinRequestDto);
        return ResponseEntity.created(URI.create("/api/login")).body(commonResponseDto);
    }

}
