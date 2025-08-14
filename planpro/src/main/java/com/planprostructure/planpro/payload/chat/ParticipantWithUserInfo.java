package com.planprostructure.planpro.payload.chat;

import com.planprostructure.planpro.domain.chat.ConversationParticipant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantWithUserInfo {
    private ConversationParticipant participant;
    private UserInfo userInfo;
    
    @Data
    @Builder
    public static class UserInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private String username;
        private String profileImageUrl;
    }
} 