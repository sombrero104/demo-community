package com.demo.community.login.service;

import com.demo.community.account.entity.Account;
import com.demo.community.account.repository.AccountRepository;
import com.demo.community.common.dto.CommonResponseDto;
import com.demo.community.account.exception.AccountAlreadyExistsException;
import com.demo.community.jwt.JwtTokenProvider;
import com.demo.community.login.dto.JoinRequestDto;
import com.demo.community.login.dto.LoginRequestDto;
import com.demo.community.login.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Account account = accountRepository.findByEmail(loginRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No account exists."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), account.getPassword())) {
           throw new BadCredentialsException("The password is incorrect.");
        }

        String accessToken = jwtTokenProvider.generateToken(account);
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }

    public CommonResponseDto join(JoinRequestDto joinRequestDto) {
        if (accountRepository.findByEmail(joinRequestDto.getEmail()).isPresent()) {
            throw new AccountAlreadyExistsException(joinRequestDto.getEmail());
        }

        joinRequestDto.setPassword(passwordEncoder.encode(joinRequestDto.getPassword()));
        Account account = accountRepository.save(joinRequestDto.toEntity());
        return new CommonResponseDto("Successfully signed up: " + account.getEmail());
    }

}
