package com.planprostructure.planpro.domain.proTalk;

import com.planprostructure.planpro.domain.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.conversation = :conversation and m.messageStatus = '1' ORDER BY m.createdAt ASC")
    List<Message> findByConversationOrderByCreatedAtDesc(Conversations conversation);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation = :conversation " +
            "AND m.sender != :user AND m.createdAt > :after")
    long countByConversationAndSenderNotAndCreatedAtAfter(
            @Param("conversation") Conversations conversation,
            @Param("user") Users user,
            @Param("after") LocalDateTime after);

    long countByConversation(Conversations conversation);

    Optional<Message> findTopByConversationOrderByCreatedAtDesc(Conversations conversation);
}

