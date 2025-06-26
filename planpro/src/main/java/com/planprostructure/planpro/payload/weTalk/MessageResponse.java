package com.planprostructure.planpro.payload.weTalk;

import com.planprostructure.planpro.domain.proTalk.Message;
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
public class MessageResponse {
    private Long id;
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

    public static MessageResponse fromMessage(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .senderDisplayName(message.getSender().getUsername())
                .senderAvatarUrl(message.getSender().getProfileImageUrl())
                .content(message.getContent())
                .messageType(MessageType.valueOf(message.getMessageType().name()))
                .fileUrl(message.getFileUrl())
                .replyToMessageId(message.getReplyTo() != null ? message.getReplyTo().getId() : null)
                .isEdited(message.getIsEdited())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}