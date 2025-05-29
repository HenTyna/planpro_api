package com.planprostructure.planpro.service.auth;

import com.planprostructure.planpro.payload.auth.AuthRequest;
import com.planprostructure.planpro.payload.auth.LoginRequest;

public interface AuthService {
    void register(AuthRequest request) throws Throwable;
    Object login(LoginRequest request) throws Throwable;
}
