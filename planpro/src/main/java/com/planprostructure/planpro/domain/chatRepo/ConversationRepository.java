package com.planprostructure.planpro.domain.chatRepo;

import com.planprostructure.planpro.domain.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, String> {} 