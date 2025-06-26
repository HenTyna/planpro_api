package com.planprostructure.planpro.service.proTalk;

import com.planprostructure.planpro.payload.weTalk.MessageResponse;
import com.planprostructure.planpro.payload.weTalk.SendMessageRequest;

import java.util.List;

public interface MessageService {

    List<MessageResponse> getConversationMessages(Long conversationId);

    MessageResponse sendMessage(Long conversationId, SendMessageRequest request);

    MessageResponse editMessage(Long messageId, String content);

    void deleteMessage(Long messageId);

}
