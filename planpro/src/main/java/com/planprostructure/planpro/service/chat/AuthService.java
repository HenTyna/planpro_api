// package com.planprostructure.planpro.service.chat;

// import com.planprostructure.planpro.domain.users.UserRepository;
// import com.planprostructure.planpro.payload.dto.AuthDtos;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.Map;

// @Service
// public class AuthService {
//   private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt; private final IdService ids;
//   public AuthService(UserRepository u, PasswordEncoder e, JwtService j, IdService ids) { this.users = u; this.encoder = e; this.jwt = j; this.ids = ids; }

//   @Transactional
//   public User register(AuthDtos.RegisterRequest req) {
//     var u = User.builder()
//         .id(ids.newId())
//         .username(req.username())
//         .email(req.email())
//         .displayName(req.displayName())
//         .passwordHash(encoder.encode(req.password()))
//         .build();
//     return users.save(u);
//   }

//   public AuthDtos.TokenResponse login(AuthDtos.LoginRequest req) {
//     var u = users.findByUsername(req.username()).orElseThrow();
//     if (!encoder.matches(req.password(), u.getPasswordHash())) throw new RuntimeException("Bad credentials");
//     var access = jwt.createAccessToken(u.getUsername(), Map.of("uid", u.getId()));
//     var refresh = jwt.createRefreshToken(u.getUsername());
//     return new AuthDtos.TokenResponse(access, refresh);
//   }

//   public AuthDtos.TokenResponse refresh(String refreshToken) {
//     var claims = jwt.parse(refreshToken).getPayload();
//     if (!"refresh".equals(claims.get("type"))) throw new RuntimeException("Invalid token type");
//     var username = claims.getSubject();
//     var u = users.findByUsername(username).orElseThrow();
//     var access = jwt.createAccessToken(u.getUsername(), Map.of("uid", u.getId()));
//     var newRefresh = jwt.createRefreshToken(u.getUsername());
//     return new AuthDtos.TokenResponse(access, newRefresh);
//   }
// } 