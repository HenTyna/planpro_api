package com.planprostructure.planpro.service.websocket;

import com.planprostructure.planpro.domain.proTalk.Message;
import com.planprostructure.planpro.domain.proTalk.Conversations;
import com.planprostructure.planpro.enums.MessageType;
import com.planprostructure.planpro.payload.websocket.ChatMessageEvent;
import com.planprostructure.planpro.payload.websocket.MessageDeleteEvent;
import com.planprostructure.planpro.payload.websocket.MessageEditEvent;
import com.planprostructure.planpro.payload.websocket.TypingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void broadcastNewMessage(Message message) {
        try {
            ChatMessageEvent event = ChatMessageEvent.builder()
                    .messageId(message.getId())
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

            // Send to conversation topic
            String destination = "/topic/conversation/" + message.getConversation().getId();
            messagingTemplate.convertAndSend(destination, event);
            
            log.info("Broadcasted new message {} to conversation {}", message.getId(), message.getConversation().getId());
        } catch (Exception e) {
            log.error("Error broadcasting new message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void broadcastMessageEdit(Message message) {
        try {
            MessageEditEvent event = MessageEditEvent.builder()
                    .messageId(message.getId())
                    .conversationId(message.getConversation().getId())
                    .content(message.getContent())
                    .updatedAt(message.getUpdatedAt())
                    .build();

            String destination = "/topic/conversation/" + message.getConversation().getId();
            messagingTemplate.convertAndSend(destination, event);
            
            log.info("Broadcasted message edit {} to conversation {}", message.getId(), message.getConversation().getId());
        } catch (Exception e) {
            log.error("Error broadcasting message edit: {}", e.getMessage(), e);
        }
    }

    @Override
    public void broadcastMessageDelete(Long messageId, Long conversationId) {
        try {
            MessageDeleteEvent event = MessageDeleteEvent.builder()
                    .messageId(messageId)
                    .conversationId(conversationId)
                    .build();

            String destination = "/topic/conversation/" + conversationId;
            messagingTemplate.convertAndSend(destination, event);
            
            log.info("Broadcasted message delete {} to conversation {}", messageId, conversationId);
        } catch (Exception e) {
            log.error("Error broadcasting message delete: {}", e.getMessage(), e);
        }
    }

    @Override
    public void broadcastTypingEvent(TypingEvent typingEvent) {
        try {
            String destination = "/topic/conversation/" + typingEvent.getConversationId();
            messagingTemplate.convertAndSend(destination, typingEvent);
            
            log.info("Broadcasted typing event for user {} in conversation {}", 
                    typingEvent.getUserId(), typingEvent.getConversationId());
        } catch (Exception e) {
            log.error("Error broadcasting typing event: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendToUser(Long userId, Object event) {
        try {
            String destination = "/user/" + userId + "/queue/messages";
            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/messages", event);
            
            log.info("Sent event to user {}", userId);
        } catch (Exception e) {
            log.error("Error sending event to user {}: {}", userId, e.getMessage(), e);
        }
    }

    @Override
    public void sendToConversation(Long conversationId, Object event) {
        try {
            String destination = "/topic/conversation/" + conversationId;
            messagingTemplate.convertAndSend(destination, event);
            
            log.info("Sent event to conversation {}", conversationId);
        } catch (Exception e) {
            log.error("Error sending event to conversation {}: {}", conversationId, e.getMessage(), e);
        }
    }
} 