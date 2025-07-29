package com.planprostructure.planpro.controller;

import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.websocket.TypingEvent;
import com.planprostructure.planpro.service.websocket.TypingIndicatorService;
import com.planprostructure.planpro.service.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final WebSocketService webSocketService;
    private final TypingIndicatorService typingIndicatorService;
    private final UserRepository userRepository;

    @MessageMapping("/typing")
    public void handleTyping(@Payload TypingEvent typingEvent, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // Set user info from authentication
            Long currentUserId = AuthHelper.getUserId();
            typingEvent.setUserId(currentUserId);
            
            // Get user details
            Users user = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            typingEvent.setUsername(user.getUsername());
            
            // Handle typing indicator
            if (typingEvent.isTyping()) {
                typingIndicatorService.startTyping(
                    typingEvent.getConversationId(), 
                    currentUserId, 
                    user.getUsername()
                );
            } else {
                typingIndicatorService.stopTyping(
                    typingEvent.getConversationId(), 
                    currentUserId, 
                    user.getUsername()
                );
            }
            
            log.info("User {} {} in conversation {}", 
                    user.getUsername(), 
                    typingEvent.isTyping() ? "started typing" : "stopped typing", 
                    typingEvent.getConversationId());
        } catch (Exception e) {
            log.error("Error handling typing event: {}", e.getMessage(), e);
        }
    }

    @MessageMapping("/join-conversation")
    @SendToUser("/queue/join-confirmation")
    public String handleJoinConversation(@Payload Long conversationId, SimpMessageHeaderAccessor headerAccessor) {
        try {
            Long currentUserId = AuthHelper.getUserId();
            Users user = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            log.info("User {} joined conversation {}", user.getUsername(), conversationId);
            return "Successfully joined conversation " + conversationId;
        } catch (Exception e) {
            log.error("Error joining conversation: {}", e.getMessage(), e);
            return "Error joining conversation";
        }
    }

    @MessageMapping("/leave-conversation")
    @SendToUser("/queue/leave-confirmation")
    public String handleLeaveConversation(@Payload Long conversationId, SimpMessageHeaderAccessor headerAccessor) {
        try {
            Long currentUserId = AuthHelper.getUserId();
            Users user = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Stop typing if user was typing
            typingIndicatorService.stopTyping(conversationId, currentUserId, user.getUsername());
            
            log.info("User {} left conversation {}", user.getUsername(), conversationId);
            return "Successfully left conversation " + conversationId;
        } catch (Exception e) {
            log.error("Error leaving conversation: {}", e.getMessage(), e);
            return "Error leaving conversation";
        }
    }
} 