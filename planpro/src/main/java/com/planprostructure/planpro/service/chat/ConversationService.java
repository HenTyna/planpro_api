package com.planprostructure.planpro.service.chat;

import com.planprostructure.planpro.domain.chat.Conversation;
import com.planprostructure.planpro.domain.chat.ConversationParticipant;
import com.planprostructure.planpro.domain.chatRepo.ConversationParticipantRepository;
import com.planprostructure.planpro.domain.chatRepo.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ConversationService {
  private final ConversationRepository conversations; private final ConversationParticipantRepository participants; private final IdService ids;
  public ConversationService(ConversationRepository c, ConversationParticipantRepository p, IdService ids) { this.conversations = c; this.participants = p; this.ids = ids; }

  @Transactional
  public Conversation create(String creatorId, String type, List<String> members, String title) {
    var conv = Conversation.builder()
        .id(ids.newId())
        .type(Conversation.ConversationType.valueOf(type.toUpperCase()))
        .title(title)
        .createdBy(creatorId)
        .build();
    conversations.save(conv);
    for (int i = 0; i < members.size(); i++) {
      var role = members.get(i).equals(creatorId) ? ConversationParticipant.Role.OWNER : ConversationParticipant.Role.MEMBER;
      participants.save(ConversationParticipant.builder()
          .conversationId(conv.getId()).userId(members.get(i)).role(role).joinedAt(Instant.now()).build());
    }
    return conv;
  }

  public List<ConversationParticipant> getParticipants(String conversationId) {
    return participants.findByConversationId(conversationId);
  }
} 