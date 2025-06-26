package com.planprostructure.planpro.service.proTalk;

import com.planprostructure.planpro.domain.proTalk.*;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.enums.ChatType;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.weTalk.ConversationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public List<ConversationResponse> getUserConversations() {
        Long currentUserId = AuthHelper.getUserId();

        return conversationRepository.findAllByParticipant(currentUserId)
                .stream()
                .map(conversation -> {
                    long unreadCount = getUnreadMessageCount(conversation, currentUserId);
                    return ConversationResponse.fromConversation(conversation, unreadCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationResponse getConversationById(Long conversationId) {
        Long currentUserId = AuthHelper.getUserId();

        Conversations conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!conversationParticipantRepository.existsByConversationAndUserId(conversation, currentUserId)) {
            throw new RuntimeException("Access denied: Not a participant");
        }

        long unreadCount = getUnreadMessageCount(conversation, currentUserId);
        return ConversationResponse.fromConversation(conversation, unreadCount);
    }

    @Override
    @Transactional
    public void markConversationAsRead(Long conversationId) {

        var conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        ConversationParticipant participant = conversationParticipantRepository
                .findByConversationAndUserId(conversation.getId(), AuthHelper.getUserId())
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        participant.setLastReadAt(LocalDateTime.now());
        conversationParticipantRepository.save(participant);
    }

    @Override
    public long getUnreadMessageCount(Conversations conversation, Long userId) {
        return conversationParticipantRepository
                .findByConversationAndUserId(conversation.getId(), AuthHelper.getUserId())
                .map(participant -> {
                    LocalDateTime lastReadAt = participant.getLastReadAt();
                    if (lastReadAt == null) {
                        return messageRepository.countByConversation(conversation);
                    }
                    return messageRepository.countByConversationAndSenderNotAndCreatedAtAfter(
                            conversation, Users.builder().id(userId).build(), lastReadAt
                    );
                })
                .orElse(0L);
    }

    @Override
    @Transactional
    public ConversationResponse createDirectConversation(Long userId) {
        Long currentUserId = AuthHelper.getUserId();

        if (currentUserId.equals(userId)) {
            throw new RuntimeException("Cannot start a conversation with yourself");
        }

        // Verify the other user exists
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        return conversationRepository.findDirectConversationBetweenUsers(currentUserId, userId)
                .map(conversation -> {
                    long unreadCount = getUnreadMessageCountSafe(conversation, currentUserId);
                    return ConversationResponse.fromConversation(conversation, unreadCount);
                })
                .orElseGet(() -> {
                    Users currentUser = userRepository.getReferenceById(currentUserId);
                    Users otherUser = userRepository.getReferenceById(userId);

                    Conversations newConversation = Conversations.builder()
                            .type(ChatType.DIRECT)
                            .createdBy(currentUser)
                            .build();

                    conversationRepository.save(newConversation);

                    // Add both participants
                    conversationParticipantRepository.saveAll(List.of(
                            ConversationParticipant.builder()
                                    .conversation(newConversation)
                                    .user(currentUser)
                                    .build(),
                            ConversationParticipant.builder()
                                    .conversation(newConversation)
                                    .user(otherUser)
                                    .build()
                    ));

                    return ConversationResponse.fromConversation(newConversation, 0L);
                });
    }

    @Override
    public long getUnreadMessageCounts(Long conversationId) throws Throwable {
        Long currentUserId = AuthHelper.getUserId();
        Users currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Conversations conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!conversationParticipantRepository.existsByConversationAndUser(conversation, currentUser)) {
            throw new RuntimeException("You are not a participant in this conversation");
        }

        return conversationParticipantRepository.findByConversationAndUser(conversation, currentUser)
                .map(participant -> {
                    LocalDateTime lastReadAt = participant.getLastReadAt();
                    if (lastReadAt == null) {
                        return messageRepository.countByConversation(conversation);
                    }
                    return messageRepository.countByConversationAndSenderNotAndCreatedAtAfter(
                            conversation, currentUser, lastReadAt
                    );
                }).orElse(0L);
    }

    private long getUnreadMessageCountSafe(Conversations conversation, Long userId) {
        try {
            return getUnreadMessageCount(conversation, userId);
        } catch (Exception e) {
            return 0L; // or log this
        }
    }
}
