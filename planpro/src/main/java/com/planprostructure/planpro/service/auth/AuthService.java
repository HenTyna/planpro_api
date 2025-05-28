package com.planprostructure.planpro.service.auth;

import com.planprostructure.planpro.payload.auth.AuthRequest;
import com.planprostructure.planpro.payload.auth.LoginRequest;

public interface AuthService {
    void register(AuthRequest request);
    void login(LoginRequest request);
}
