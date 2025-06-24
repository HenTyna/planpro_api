package com.planprostructure.planpro.domain.proTalk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Messages, Long> {

    @Query("SELECT m FROM Messages m WHERE m.conversation.id = :conversationId ORDER BY m.sentAt DESC")
    List<Messages> findByConversationId(@Param("conversationId") Long conversationId);

    // Optional: Add this if you need ascending order
    @Query("SELECT m FROM Messages m WHERE m.conversation.id = :conversationId ORDER BY m.sentAt ASC")
    Page<Messages> findByConversationIdAsc(@Param("conversationId") Long conversationId, Pageable pageable);
}
