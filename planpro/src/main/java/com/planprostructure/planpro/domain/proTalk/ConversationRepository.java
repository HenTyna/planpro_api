package com.planprostructure.planpro.domain.proTalk;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversations, Long> {
    List<Conversations> findByMembers_Id(Long userId);
}

