package com.planprostructure.planpro.domain.chatRepo;

import com.planprostructure.planpro.domain.chat.MessageReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, MessageReaction.PK> {} 