package com.planprostructure.planpro.domain.proTalk;

import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.enums.ContactStatus;
import com.planprostructure.planpro.payload.weTalk.IGetMyContacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // Find contacts where user is the current user and status matches
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND c.status = :status AND c.contactStatus = '1'")
    List<Contact> findByUserAndStatus(@Param("userId") Long userId,
                                      @Param("status") ContactStatus status);

    // Find pending requests where contactUser is the current user
    @Query("SELECT c FROM Contact c WHERE c.contactUser.id = :contactUserId AND c.status = :status AND c.contactStatus = '1'")
    List<Contact> findByContactUserAndStatus(@Param("contactUserId") Long contactUserId,
                                             @Param("status") ContactStatus status);

    // Check if contact exists between two users
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Contact c " +
            "WHERE c.user.id = :userId AND c.contactUser.id = :contactUserId AND c.contactStatus = '1'")
    boolean existsByUserAndContactUser(@Param("userId") Long userId,
                                       @Param("contactUserId") Long contactUserId);

    // Find specific contact between two users
    Optional<Contact> findByUserAndContactUser(Users user, Users contactUser);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND c.status = 'ACCEPTED'")
    List<Contact> findAcceptedContactsByUserId(@Param("userId") Long userId);

    @Query("SELECT cp.conversation FROM ConversationParticipant cp " +
            "WHERE cp.user.id = :userId1 AND cp.conversation IN " +
            "(SELECT cp2.conversation FROM ConversationParticipant cp2 WHERE cp2.user.id = :userId2) " +
            "AND cp.conversation.type = 'DIRECT'")
    Optional<Conversations> findDirectConversationBetweenUsers(@Param("userId1") Long userId1,
                                                               @Param("userId2") Long userId2);

    @Query(value = """
                SELECT DISTINCT u.id                                                  AS user_id,
                                u.username                                            AS username,
                                u.profile_image_url                                   AS avatar_url,
                                TO_CHAR(u.created_at, 'YYYY-MM-DD')                   AS user_created_at,
                                c.id                                                  AS conversation_id,
                                last_msg.content                                      AS last_message,
                                TO_CHAR(last_msg.created_at, 'HH24:MI:SS') AS last_message_time
                FROM conversation_participants cp
                         JOIN conversations c ON c.id = cp.conversation_id
                         JOIN conversation_participants cp2 ON cp2.conversation_id = c.id AND cp2.user_id != :currentUserId
                         JOIN tb_user u ON u.id = cp2.user_id
                         LEFT JOIN LATERAL (
                    SELECT m.content, m.created_at
                    FROM messages m
                    WHERE m.conversation_id = c.id
                    ORDER BY m.created_at DESC
                    LIMIT 1
                    ) last_msg ON true
                WHERE cp.user_id = :currentUserId
                  AND c.type = 'DIRECT'
            """, nativeQuery = true)
    List<IGetMyContacts> findContactsWithConversationInfo(@Param("currentUserId") Long currentUserId);
}