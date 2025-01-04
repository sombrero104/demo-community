package com.demo.community.jwt;

import com.demo.community.account.entity.Account;
import com.demo.community.account.role.AccountRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private static final String CLAIM_ROLES = "roles";
    
    private final JwtProperties jwtProperties;

    public String generateToken(Account account) {
        return Jwts.builder()
                .subject(account.getEmail())
                .claim(CLAIM_ROLES, account.getRoles())
                .issuedAt(new Date())
                .expiration(getExpiration())
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        parseClaims(token);
        return true;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String email = claims.getSubject();
        List<SimpleGrantedAuthority> authorities = extractRoles(claims);
        UserDetails userDetails = new User(email, "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    private List<SimpleGrantedAuthority> extractRoles(Claims claims) {
        return ((List<?>) claims.get(CLAIM_ROLES)).stream()
                .map(role -> new SimpleGrantedAuthority(AccountRole.ROLE_PREFIX + role))
                .toList();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date getExpiration() {
        return new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime());
    }

}
