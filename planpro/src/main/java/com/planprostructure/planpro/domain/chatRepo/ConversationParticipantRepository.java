package com.planprostructure.planpro.domain.chatRepo;

import com.planprostructure.planpro.domain.chat.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, ConversationParticipant.PK> {
  List<ConversationParticipant> findByUserId(String userId);
  List<ConversationParticipant> findByConversationId(String conversationId);
} 