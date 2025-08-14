package com.planprostructure.planpro.service.auth;

import com.planprostructure.planpro.components.common.api.StatusCode;
import com.planprostructure.planpro.config.JwtUtil;
import com.planprostructure.planpro.config.UserAuthenticationProvider;
import com.planprostructure.planpro.domain.security.SecurityUser;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.enums.Role;
import com.planprostructure.planpro.enums.StatusUser;
import com.planprostructure.planpro.exception.BusinessException;
import com.planprostructure.planpro.payload.auth.AuthRequest;
import com.planprostructure.planpro.payload.auth.AuthResponse;
import com.planprostructure.planpro.payload.auth.LoginRequest;
import com.planprostructure.planpro.service.contacts.ContactService;
import com.planprostructure.planpro.service.password.PasswordEncryption;
import com.planprostructure.planpro.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImp implements  AuthService {
    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final PasswordEncryption passwordEncryption;
    private final ContactService contactService;

    @Override
    @Transactional
    public void register(AuthRequest request) throws Throwable {

        String rawPassword;
        try {
            rawPassword = passwordEncryption.getPassword(request.getPassword());
        } catch (Exception e) {
            throw new BusinessException(StatusCode.PASSWORD_MUST_BE_ENCRYPTED);
        }

        var users = Users.builder()
                .username(request.getUsername())
                .password(rawPassword)
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER)
                .status(StatusUser.ACTIVE)
                .build();
        Users savedUser = userRepository.save(users);

        // Create contacts for the new user if phone number is provided
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
            try {
                contactService.createContactsForNewUser(savedUser.getId().toString(), request.getPhoneNumber());
                log.info("Successfully created contacts for new user: {}", savedUser.getId());
            } catch (Exception e) {
                log.error("Failed to create contacts for new user: {}", savedUser.getId(), e);
                // Don't throw exception here as user registration should still succeed
            }
        }
    }

    @Override
    @Transactional
    public Object login(LoginRequest request) throws Throwable {

        if (request.getUsername() == null || request.getPassword() == null) {
            throw new BusinessException(StatusCode.BAD_REQUEST, "Username and password are required");
        }

        Authentication authentication = userAuthenticationProvider.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (securityUser == null) {
            throw new BusinessException(StatusCode.AUTHENTICATION_FAILED, "Authentication failed");
        }

        if (!securityUser.isEnabled()) {
            throw new BusinessException(StatusCode.USER_DISABLED, "User account is disabled");
        }

        String token = jwtUtil.doGenerateToken(securityUser);
        return new AuthResponse(
                token,
                "Bearer",
                jwtUtil.getExpireIn()
        );
    }
}
