package com.planprostructure.planpro.service.websocket;

import com.planprostructure.planpro.domain.proTalk.Conversations;
import com.planprostructure.planpro.domain.proTalk.Message;
import com.planprostructure.planpro.payload.websocket.ChatMessageEvent;
import com.planprostructure.planpro.payload.websocket.MessageDeleteEvent;
import com.planprostructure.planpro.payload.websocket.MessageEditEvent;
import com.planprostructure.planpro.payload.websocket.TypingEvent;

public interface WebSocketService {
    
    void broadcastNewMessage(Message message);
    
    void broadcastMessageEdit(Message message);
    
    void broadcastMessageDelete(Long messageId, Long conversationId);
    
    void broadcastTypingEvent(TypingEvent typingEvent);
    
    void sendToUser(Long userId, Object event);
    
    void sendToConversation(Long conversationId, Object event);
} 