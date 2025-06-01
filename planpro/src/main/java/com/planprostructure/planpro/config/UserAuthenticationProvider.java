package com.planprostructure.planpro.config;


import com.planprostructure.planpro.components.common.api.StatusCode;
import com.planprostructure.planpro.exception.BusinessException;
import com.planprostructure.planpro.service.password.PasswordEncryption;
import com.planprostructure.planpro.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncryption passwordEncryption;

    public UserAuthenticationProvider(
            @Qualifier("userAuthProvider") AuthenticationManager authenticationManager,
            PasswordEncryption passwordEncryption
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncryption = passwordEncryption;
    }

    public Authentication authenticate(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(StatusCode.BAD_REQUEST, "Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException(StatusCode.BAD_REQUEST, "Password cannot be empty");
        }

        try {
            // Pass the raw password directly to the authentication manager
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            if (auth == null || !auth.isAuthenticated()) {
                throw new BadCredentialsException("Authentication failed");
            }

            return auth;
        } catch (UsernameNotFoundException ex) {
            throw new BusinessException(StatusCode.USER_NOT_FOUND, "User not found: " + username);
        } catch (BadCredentialsException e) {
            throw new BusinessException(StatusCode.BAD_CREDENTIALS, "Invalid username or password");
        } catch (DisabledException e) {
            throw new BusinessException(StatusCode.USER_DISABLED, "User account is disabled");
        } catch (Exception e) {
            throw new BusinessException(StatusCode.AUTHENTICATION_FAILED, "Authentication error occurred");
        }
    }
}
