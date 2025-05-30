package com.planprostructure.planpro.service.users;

import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.users.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService {

    private final UserRepository userRepository;
    private final AuthHelper authHelper; // Inject AuthHelper

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile() throws Throwable { // Changed return type to UserProfileResponse
        // 1. Get the current user ID using the injected AuthHelper
        Long userId = authHelper.getCurrentUserId(); // Call the non-static method

        // 2. Fetch the user profile from the repository
        //    Using findById as typically it's by primary key.
        //    If findByUserByUserId is a custom method, ensure it's correct.
        Users profile = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        // 3. You don't need 'this.getUser()' here if 'profile' already holds the user data.
        //    'users' variable is redundant if 'profile' is the complete user object.
        //    If 'profile' is a different entity, you need to clarify its structure.
        //    Assuming 'profile' is the 'Users' object itself for this example.

        return UserProfileResponse.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .status(String.valueOf(profile.getStatus()))
                .role(String.valueOf(profile.getRole()))
                .phoneNumber(String.valueOf(profile.getPhoneNumber()))
                .build();
    }
}
