package com.planprostructure.planpro.domain.proTalk;

import com.planprostructure.planpro.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {
    @Query("SELECT cp FROM ConversationParticipant cp WHERE cp.conversation.id = :conversationId AND cp.user.id = :userId")
    Optional<ConversationParticipant> findByConversationAndUserId(@Param("conversationId") Long conversationId,
                                                                  @Param("userId") Long userId);

    boolean existsByConversationAndUserId(Conversations conversation, Long userId);

//    boolean existsByConversationAndUser(Conversations conversation, Users user);

    @Query("SELECT CASE WHEN COUNT(cp) > 0 THEN true ELSE false END " +
            "FROM ConversationParticipant cp " +
            "WHERE cp.conversation = :conversation AND cp.user = :user")
    boolean existsByConversationAndUser(
            @Param("conversation") Conversations conversation,
            @Param("user") Users user);

    @Query("SELECT cp FROM ConversationParticipant cp WHERE cp.conversation = :conversation AND cp.user = :user")
    Optional<ConversationParticipant> findByConversationAndUser(
            @Param("conversation") Conversations conversation,
            @Param("user") Users user);
}
