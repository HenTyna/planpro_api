package com.planprostructure.planpro.config;

import com.planprostructure.planpro.domain.security.SecurityUser;
import com.planprostructure.planpro.properties.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtConfig;
    private final JwtDecoder jwtDecoder;

    public long getExpireIn() {
        return jwtConfig.expiration().getSeconds();
    }
    private Instant extractExpiration(String token) {
        return jwtDecoder.decode(token).getExpiresAt();
    }

    public Map<String, Object> extractAllClaims(String token) {
        return new LinkedHashMap<>(jwtDecoder.decode(token).getClaims());
    }

    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).isBefore(Instant.now());
        } catch (ExpiredJwtException e) {
            return true; // Token is expired
        } catch (JwtException e) {
            LoggerFactory.getLogger(JwtUtil.class).error("Invalid JWT token: {}", e.getMessage());
            return true; // Token is invalid
        }
    }

    public String doGenerateToken(SecurityUser securityUser) {
        if (securityUser == null) {
            throw new IllegalArgumentException("SecurityUser cannot be null");
        }

        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", Objects.requireNonNull(securityUser.getUserId(), "User ID cannot be null"));
            claims.put("username", Objects.requireNonNull(securityUser.getUsername(), "Username cannot be null"));
            claims.put("roles", Objects.requireNonNull(securityUser.getAuthorities(), "Authorities cannot be null"));
            claims.put("iat", Instant.now().getEpochSecond());
            claims.put("exp", Instant.now().plusSeconds(getExpireIn()).getEpochSecond());
            claims.put("jti", UUID.randomUUID().toString());

            JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                    .subject(securityUser.getUsername())
                    .claims(c ->c.putAll(claims))
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(getExpireIn()))
                    .build();
            return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        } catch (Exception e) {
            throw new JwtException("Error generating JWT token", e);
        }
    }
}