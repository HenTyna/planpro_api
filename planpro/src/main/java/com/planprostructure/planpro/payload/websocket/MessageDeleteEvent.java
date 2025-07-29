package com.planprostructure.planpro.payload.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDeleteEvent {
    private String eventType = "MESSAGE_DELETED";
    private Long messageId;
    private Long conversationId;
} 