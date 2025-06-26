package com.planprostructure.planpro.payload.weTalk;

import com.planprostructure.planpro.domain.proTalk.Conversations;
import com.planprostructure.planpro.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponse {
    private Long id;
    private ChatType type;
    private String title;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ParticipantResponse> participants;
    private long unreadCount;
    private MessageResponse lastMessage;

    public static ConversationResponse fromConversation(Conversations conversation, long unreadCount) {
        return ConversationResponse.builder()
                .id(conversation.getId())
                .type(ChatType.valueOf(conversation.getType().name()))
                .title(conversation.getTitle())
                .createdBy(conversation.getCreatedBy() != null ? conversation.getCreatedBy().getId() : null)
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .participants(conversation.getParticipants().stream()
                        .map(participant -> ParticipantResponse.fromParticipant(participant))
                        .collect(Collectors.toList()))
                .unreadCount(unreadCount)
                .lastMessage(conversation.getMessages().isEmpty() ? null :
                        MessageResponse.fromMessage(
                                new ArrayList<>(conversation.getMessages())
                                        .get(conversation.getMessages().size() - 1)
                        )
                )
                .build();
    }
}