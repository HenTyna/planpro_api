package com.planprostructure.planpro.payload.proTalk;

import com.planprostructure.planpro.domain.proTalk.Conversations;

import java.util.Set;
import java.util.stream.Collectors;

//public record ConversationResponse(
//        Long id,
//        boolean isGroup,
//        String name,
//        Set<UserResponse> members,
//        MessageResponse lastMessage
//
//) {
//    public static ConversationResponse fromEntity(Conversations conversation) {
//        return new ConversationResponse(
//                conversation.getId(),
//                conversation.isGroup(),
//                conversation.getName(),
//                conversation.getMembers().stream()
//                        .map(UserResponse::fromEntity)
//                        .collect(Collectors.toSet()),
//                conversation.getMessages().isEmpty() ? null :
//                        MessageResponse.fromEntity(conversation.getMessages().get(
//                                conversation.getMessages().size() - 1))
//        );
//    }
//}
