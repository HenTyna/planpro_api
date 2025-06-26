package com.planprostructure.planpro.payload.weTalk;

import com.planprostructure.planpro.domain.proTalk.ConversationParticipant;
import com.planprostructure.planpro.domain.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantResponse {
    private Long userId;
    private String username;
    private String displayName;
    private String avatarUrl;
    private LocalDateTime joinedAt;
    private LocalDateTime lastReadAt;

    public static ParticipantResponse fromParticipant(ConversationParticipant participant) {
        Users user = participant.getUser();
        return ParticipantResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .displayName(user.getUsername())
                .avatarUrl(user.getProfileImageUrl())
                .joinedAt(participant.getJoinedAt())
                .lastReadAt(participant.getLastReadAt())
                .build();
    }
}