package com.planprostructure.planpro.service.websocket;

import com.planprostructure.planpro.payload.websocket.TypingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypingIndicatorService {

    private final WebSocketService webSocketService;
    
    // Map to track typing users: conversationId -> Map<userId, lastTypingTime>
    private final Map<Long, Map<Long, LocalDateTime>> typingUsers = new ConcurrentHashMap<>();
    
    private static final int TYPING_TIMEOUT_SECONDS = 5;

    public void startTyping(Long conversationId, Long userId, String username) {
        typingUsers.computeIfAbsent(conversationId, k -> new ConcurrentHashMap<>())
                .put(userId, LocalDateTime.now());
        
        TypingEvent event = TypingEvent.builder()
                .conversationId(conversationId)
                .userId(userId)
                .username(username)
                .isTyping(true)
                .build();
        
        webSocketService.broadcastTypingEvent(event);
        log.debug("User {} started typing in conversation {}", username, conversationId);
    }

    public void stopTyping(Long conversationId, Long userId, String username) {
        Map<Long, LocalDateTime> users = typingUsers.get(conversationId);
        if (users != null) {
            users.remove(userId);
            if (users.isEmpty()) {
                typingUsers.remove(conversationId);
            }
        }
        
        TypingEvent event = TypingEvent.builder()
                .conversationId(conversationId)
                .userId(userId)
                .username(username)
                .isTyping(false)
                .build();
        
        webSocketService.broadcastTypingEvent(event);
        log.debug("User {} stopped typing in conversation {}", username, conversationId);
    }

    @Scheduled(fixedRate = 2000) // Check every 2 seconds
    public void cleanupExpiredTypingIndicators() {
        LocalDateTime now = LocalDateTime.now();
        
        typingUsers.forEach((conversationId, users) -> {
            users.entrySet().removeIf(entry -> {
                LocalDateTime lastTyping = entry.getValue();
                if (lastTyping.plusSeconds(TYPING_TIMEOUT_SECONDS).isBefore(now)) {
                    // Send stop typing event
                    TypingEvent event = TypingEvent.builder()
                            .conversationId(conversationId)
                            .userId(entry.getKey())
                            .isTyping(false)
                            .build();
                    
                    webSocketService.broadcastTypingEvent(event);
                    log.debug("Auto-stopped typing indicator for user {} in conversation {}", 
                            entry.getKey(), conversationId);
                    return true;
                }
                return false;
            });
            
            // Remove empty conversation entries
            if (users.isEmpty()) {
                typingUsers.remove(conversationId);
            }
        });
    }

    public Map<Long, LocalDateTime> getTypingUsers(Long conversationId) {
        return typingUsers.getOrDefault(conversationId, new ConcurrentHashMap<>());
    }
} 