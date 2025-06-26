package com.planprostructure.planpro.service.proTalk;

import com.planprostructure.planpro.domain.proTalk.ConversationRepository;
import com.planprostructure.planpro.domain.proTalk.Conversations;
import com.planprostructure.planpro.domain.proTalk.Message;
import com.planprostructure.planpro.domain.proTalk.MessageRepository;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.enums.Status;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.weTalk.MessageResponse;
import com.planprostructure.planpro.payload.weTalk.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Override
    public List<MessageResponse> getConversationMessages(Long conversationId) {
        Long currentUserId = AuthHelper.getUserId();

        // 1. Get the conversation with participants loaded
        Conversations conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // 2. Proper participant check using user ID comparison
        boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(p -> p.getUser().getId().equals(currentUserId));

        if (!isParticipant) {
            throw new RuntimeException("You are not a member of this conversation");
        }

        // 3. Get and return messages
        return messageRepository.findByConversationOrderByCreatedAtDesc(conversation)
                .stream()
                .map(MessageResponse::fromMessage)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(Long conversationId, SendMessageRequest request) {
        Long currentUserId = AuthHelper.getUserId();

        // 1. Get the conversation with participants loaded
        Conversations conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // 2. Proper participant check using user ID comparison
        boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(p -> p.getUser().getId().equals(currentUserId));

        if (!isParticipant) {
            throw new RuntimeException("You are not a member of this conversation");
        }

        // 3. Get the full user entity
        Users sender = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Build and save the message
        Message message = Message.builder()
                .conversation(conversation)
                .content(request.getContent())
                .sender(sender)  // Using full user entity
                .messageType(request.getMessageType())
                .fileUrl(request.getFileUrl())
                .messageStatus(Status.NORMAL)
                .build();

        // 5. Handle reply if exists
        if (request.getReplyToMessageId() != null) {
            Message replyToMessage = messageRepository.findById(request.getReplyToMessageId())
                    .orElseThrow(() -> new RuntimeException("Reply message not found"));
            message.setReplyTo(replyToMessage);
        }

        message = messageRepository.save(message);

        // 6. Update conversation timestamp
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return MessageResponse.fromMessage(message);
    }

    @Override
    public MessageResponse editMessage(Long messageId, String content) {
        Long currentUserId = AuthHelper.getUserId();
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new RuntimeException("Message not found")
        );

        if (!message.getSender().getId().equals(currentUserId)) {
            throw new RuntimeException("You are not the sender of this message");
        }

        message.setContent(content);
        message.setIsEdited(true);
        message.setUpdatedAt(LocalDateTime.now());
        message = messageRepository.save(message);

        // Update conversation's updatedAt
        Conversations conversation = message.getConversation();
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return MessageResponse.fromMessage(message);
    }

    @Override
    public void deleteMessage(Long messageId) {
        Long currentUserId = AuthHelper.getUserId();
        Message message = messageRepository.findById(messageId).orElseThrow(
                () -> new RuntimeException("Message not found")
        );
        if (!message.getSender().equals(currentUserId)) {
            throw new IllegalArgumentException("You can only delete your own messages");
        }
        Conversations conversation = message.getConversation();
        if (conversation.getMessages().stream()
                .max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                .map(lastMessage -> lastMessage.equals(message))
                .orElse(false)) {
            conversation.setUpdatedAt(LocalDateTime.now());
            conversationRepository.save(conversation);
        }
        message.setMessageStatus(Status.DISABLE);
        messageRepository.save(message);
    }
}
