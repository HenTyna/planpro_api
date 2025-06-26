//package com.planprostructure.planpro.service.proTalk;
//
//import com.planprostructure.planpro.domain.proTalk.*;
//import com.planprostructure.planpro.domain.users.UserRepository;
//import com.planprostructure.planpro.enums.MessageStatus;
//import com.planprostructure.planpro.helper.AuthHelper;
//import com.planprostructure.planpro.payload.proTalk.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class ChatServiceImpl implements ChatService{
//
//    private final ConversationRepository conversationRepository;
//    private final UserChatRepository userRepository;
//    private final MessageRepository messageRepository;
//    private final UserRepository usersRepository;
//
////    @Override
////    public UserResponse createUser(Long phoneNumber, String username) { //check again
////        var user = usersRepository.findByUserIdAndUsername(phoneNumber, username)
////                .orElseThrow(() -> new RuntimeException("User not found with Phone No.: " + phoneNumber));
////        UserChat userChat = new UserChat();
////        userChat.setUserId(user.getId());
////        userChat.setUsername(user.getUsername());
////        return UserResponse.fromEntity(userRepository.save(userChat));
////    }
//
//    @Override
//    public List<UserResponse> getAllUsers() {
//        List<UserChat> users = userRepository.findAll();
//        if (users.isEmpty()) {
//            throw new RuntimeException("No users found");
//        }
//        return users.stream()
//                .filter(owner -> !owner.getUserId().equals(AuthHelper.getUserId())) // Exclude current user
//                .map(UserResponse::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public ConversationResponse createConversation(ConversationRequest request) {
//        Set<UserChat> members = userRepository.findAllById(request.memberIds())
//                .stream()
//                .collect(Collectors.toSet());
//
//        if (members.size() != request.memberIds().size()) {
//            throw new RuntimeException("Some users not found for the provided IDs");
//        }
//
//        Conversations conversations = new Conversations();
//        conversations.setGroup(request.isGroup());
//        conversations.setName(request.name());
//        conversations.setMembers(members);
//
//        return ConversationResponse.fromEntity(
//                conversationRepository.save(conversations)
//        );
//    }
//
//    @Override
//    public List<ConversationResponse> getUserConversations(Long userId) {
//        return conversationRepository.findByMembers_Id(userId).stream()
//                .map(ConversationResponse::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public ConversationResponse getConversation(Long conversationId) {
//        return conversationRepository.findById(conversationId)
//                .map(ConversationResponse::fromEntity)
//                .orElseThrow(() -> new RuntimeException("Conversation not found with id: " + conversationId));
//    }
//
//    @Override
//    public MessageResponse sendMessage(MessageRequest request) {
//        Conversations conversations = conversationRepository.findById(request.conversationId()).orElseThrow(
//                () -> new RuntimeException("Conversation not found with id: " + request.conversationId())
//        );
//        UserChat sender = userRepository.findById(request.senderId()).orElseThrow(
//                () -> new RuntimeException("User not found with id: " + request.senderId())
//        );
//
//        Messages message = new Messages();
//        message.setConversation(conversations);
//        message.setSender(sender);
//        message.setContent(request.content());
//        message.setSentAt(Instant.now());
//        message.setStatus(MessageStatus.SENT);
//
//        return MessageResponse.fromEntity(
//                messageRepository.save(message)
//        );
//    }
//
//    @Override
//    public List<MessageResponse> getConversationMessages(Long conversationId) {
//        return messageRepository.findByConversationId(conversationId)
//                .stream()
//                .map(MessageResponse::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public MessageResponse updateMessageStatus(StatusUpdateRequest request) {
//        Messages messages = messageRepository.findById(request.messageId())
//                .orElseThrow(() -> new RuntimeException("Message not found with id: " + request.messageId()));
//        messages.setStatus(request.status());
//        return MessageResponse.fromEntity(
//                messageRepository.save(messages)
//        );
//    }
//
//    @Override
//    public MessageResponse addReaction(ReactionRequest request) {
//        Messages message = messageRepository.findById(request.messageId())
//                .orElseThrow(() -> new RuntimeException("Message not found with id: " + request.messageId()));
//
//        MessageMetadata metadata = message.getMetadata() != null ? message.getMetadata() : new MessageMetadata();
//        if (metadata.getReactions() == null) {
//            metadata.setReactions(new ArrayList<>());
//        }
//        metadata.getReactions().add(request.reaction());
//        message.setMetadata(metadata);
//        return MessageResponse.fromEntity(
//                messageRepository.save(message)
//        );
//    }
//
//    @Override
//    public void deleteMessage(Long messageId) {
//
//    }
//}