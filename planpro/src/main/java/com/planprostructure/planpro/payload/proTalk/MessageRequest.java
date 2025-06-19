package com.planprostructure.planpro.payload.proTalk;

public record MessageRequest(
        Long conversationId,
        String content,
        Long senderId
) {
}
