package com.planprostructure.planpro.payload.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypingEvent {
    private String eventType = "TYPING";
    private Long conversationId;
    private Long userId;
    private String username;
    private boolean isTyping;
} 