package com.planprostructure.planpro.service.password;

import com.planprostructure.planpro.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncryptionImpl implements PasswordEncryption {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String getPassword(String password) throws Exception {
        // Only decrypt, don't encode - let Spring Security handle the encoding
        return PasswordUtils.decrypt(password);
    }
}
