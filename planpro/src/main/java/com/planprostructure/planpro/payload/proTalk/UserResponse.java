package com.planprostructure.planpro.payload.proTalk;

import com.planprostructure.planpro.domain.proTalk.UserChat;
import com.planprostructure.planpro.domain.users.Users;

public record UserResponse(
        Long id,
        String username
) {
    public static UserResponse fromEntity(UserChat users) {
        return new UserResponse(
                users.getId(),
                users.getUsername()
        );
    }
}
