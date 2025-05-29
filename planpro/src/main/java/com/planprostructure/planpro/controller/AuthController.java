package com.planprostructure.planpro.controller;


import com.planprostructure.planpro.components.common.api.ApiResponse;
import com.planprostructure.planpro.components.common.api.Common;
import com.planprostructure.planpro.components.common.api.ProPlanRestController;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.auth.AuthRequest;
import com.planprostructure.planpro.payload.auth.LoginRequest;
import com.planprostructure.planpro.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wb/v1/auth")
@RequiredArgsConstructor
public class AuthController extends ProPlanRestController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestHeader Map<String, String> headers,@Valid @RequestBody AuthRequest payload) throws Throwable{
        authService.register(payload);
        return ok(new Common(headers));
    }

    @GetMapping("/test")
    public String test() {
        return "Test successful";
    }

    @PostMapping("/login")
    public Object login(@RequestHeader Map<String, String> headers, @RequestBody @Valid LoginRequest payload) throws Throwable{
        return ok(authService.login(payload),new Common(headers));
    }
}
