package com.demo.community.login.dto;

import com.demo.community.account.entity.Account;
import com.demo.community.account.role.AccountRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {

    @NotBlank(message = "Email cannot be empty.")
    @Size(max = 255, message = "Email must be 255 characters or less.")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(max = 255, message = "Password must be 255 characters or less.")
    private String password;

    @Size(max = 100, message = "Nickname must be 100 characters or less.")
    private String nickname;

    private Set<AccountRole> roles = new HashSet<>(List.of(AccountRole.USER));

    public Account toEntity() {
        return Account.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .roles(roles)
                .build();
    }

}
