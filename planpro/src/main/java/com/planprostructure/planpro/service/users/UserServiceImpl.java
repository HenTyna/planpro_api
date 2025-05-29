package com.planprostructure.planpro.service.users;

import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.users.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService {

    private final UserRepository userRepository;

    private Users getUser(){
        return userRepository.findByUsername(AuthHelper.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Object getProfile() throws Throwable {
        Long userId = AuthHelper.getCurrentUserId();
        var profile = userRepository.findByUserByUserId(userId).orElseThrow(() -> new Throwable("User not found"));
        var users = this.getUser();
        return UserProfileResponse.builder()
                .id(users.getId())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .status(String.valueOf(profile.getStatus()))
                .role(String.valueOf(profile.getRole()))
                .phoneNumber(String.valueOf(profile.getPhoneNumber()))
                .build();
    }
}
