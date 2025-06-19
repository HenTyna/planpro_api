package com.planprostructure.planpro.payload.proTalk;

import com.planprostructure.planpro.domain.proTalk.MessageMetadata;
import com.planprostructure.planpro.domain.proTalk.Messages;
import com.planprostructure.planpro.enums.MessageStatus;

import java.time.Instant;

public record MessageResponse(
        Long id,
        Long conversationId,
        UserResponse sender,
        String content,
        Instant sentAt,
        MessageStatus status,
        MessageMetadata metadata
) {
    public static MessageResponse fromEntity(Messages message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                UserResponse.fromEntity(message.getSender()),
                message.getContent(),
                message.getSentAt(),
                message.getStatus(),
                message.getMetadata()
        );
    }
}
