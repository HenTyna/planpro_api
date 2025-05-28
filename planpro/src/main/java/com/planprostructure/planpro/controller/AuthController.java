package com.planprostructure.planpro.controller;


import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.auth.AuthRequest;
import com.planprostructure.planpro.payload.auth.LoginRequest;
import com.planprostructure.planpro.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wb/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered");
    }

    @GetMapping("/test")
    public Long test() {
        Long userId = AuthHelper.getCurrentUserId();
        return userId;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password must not be empty");
        }
        authService.login(request);
        return ResponseEntity.ok("User logged in successfully");
    }
}
