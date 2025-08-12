package com.planprostructure.planpro.domain.chatRepo;

import com.planprostructure.planpro.domain.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
  Page<Message> findByConversationIdOrderByMessageSeqAsc(String conversationId, Pageable pageable);
  Message findTopByConversationIdOrderByMessageSeqDesc(String conversationId);
} 