package com.planprostructure.planpro.payload.weTalk;

import org.springframework.beans.factory.annotation.Value;

public interface IGetMyContacts {
    @Value("#{target.user_id}")
    Long getUserId();

    @Value("#{target.username}")
    String getUsername();

    @Value("#{target.avatar_url}")
    String getAvatarUrl();

    @Value("#{target.user_created_at}")
    String getMessageDate();

    @Value("#{target.conversation_id}")
    Long getConversationId();

    @Value("#{target.last_message}")
    String getLastMessage();

    @Value("#{target.last_message_time}")
    String getLastMessageTime();
}
