package com.planprostructure.planpro.service.auth;

import com.planprostructure.planpro.config.JwtUtil;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.enums.Role;
import com.planprostructure.planpro.enums.StatusUser;
import com.planprostructure.planpro.payload.auth.AuthRequest;
import com.planprostructure.planpro.payload.auth.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements  AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new RuntimeException("Username already exists");

        var user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(StatusUser.ACTIVE);

        userRepository.save(user);
    }

    @Override
    public void login(LoginRequest request) {
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials");

        return jwtUtil.generateToken(user.getUsername());
    }
}
