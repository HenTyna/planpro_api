package com.planprostructure.planpro.payload.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEditEvent {
    private String eventType = "MESSAGE_EDITED";
    private Long messageId;
    private Long conversationId;
    private String content;
    private LocalDateTime updatedAt;
} 