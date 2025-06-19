package com.planprostructure.planpro.payload.proTalk;

public record UserRequest(
        Long userId,
        String username
) {
}
