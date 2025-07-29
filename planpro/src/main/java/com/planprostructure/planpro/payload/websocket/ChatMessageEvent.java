package com.planprostructure.planpro.payload.websocket;

import com.planprostructure.planpro.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageEvent {
    private String eventType = "NEW_MESSAGE";
    private Long messageId;
    private Long conversationId;
    private Long senderId;
    private String senderUsername;
    private String senderDisplayName;
    private String senderAvatarUrl;
    private String content;
    private MessageType messageType;
    private String fileUrl;
    private Long replyToMessageId;
    private boolean isEdited;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 