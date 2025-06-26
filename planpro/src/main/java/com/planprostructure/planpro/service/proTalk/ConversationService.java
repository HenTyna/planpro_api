package com.planprostructure.planpro.service.proTalk;

import com.planprostructure.planpro.domain.proTalk.Conversations;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.payload.weTalk.ConversationResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationResponse> getUserConversations() throws Throwable;

    ConversationResponse getConversationById(Long conversationId) throws Throwable;

    void markConversationAsRead(Long conversationId) throws Throwable;

    long getUnreadMessageCount(Conversations conversations, Long users) throws Throwable;

    ConversationResponse createDirectConversation(Long userId) throws Throwable;

    long getUnreadMessageCounts(Long conversationId) throws Throwable;
}
