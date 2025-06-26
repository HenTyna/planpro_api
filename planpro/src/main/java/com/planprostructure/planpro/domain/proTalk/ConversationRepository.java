package com.planprostructure.planpro.domain.proTalk;

import com.planprostructure.planpro.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversations, Long> {

    // Option 1: Use User ID in query (recommended)
    @Query("SELECT c FROM Conversations c JOIN c.participants p WHERE p.user.id = :userId ORDER BY c.updatedAt DESC")
    List<Conversations> findAllByParticipant(@Param("userId") Long userId);

    // Option 2: Or if you prefer to work with Users entity
    @Query("SELECT c FROM Conversations c JOIN c.participants p WHERE p.user = :user ORDER BY c.updatedAt DESC")
    List<Conversations> findAllByParticipant(@Param("user") Users user);

    @Query("SELECT c FROM Conversations c " +
            "JOIN c.participants p1 " +
            "JOIN c.participants p2 " +
            "WHERE p1.user.id = :userId1 AND p2.user.id = :userId2 AND c.type = 'DIRECT'")
    Optional<Conversations> findDirectConversationBetweenUsers(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2);
}

