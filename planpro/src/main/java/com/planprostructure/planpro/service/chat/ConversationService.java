package com.planprostructure.planpro.service.chat;

import com.planprostructure.planpro.domain.chat.Conversation;
import com.planprostructure.planpro.domain.chat.ConversationParticipant;
import com.planprostructure.planpro.domain.chatRepo.ConversationParticipantRepository;
import com.planprostructure.planpro.domain.chatRepo.ConversationRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.payload.chat.ParticipantWithUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationService {
  private final ConversationRepository conversations; 
  private final ConversationParticipantRepository participants; 
  private final UserRepository userRepository;
  private final IdService ids;
  public ConversationService(ConversationRepository c, ConversationParticipantRepository p, 
                           UserRepository userRepo, IdService ids) { 
    this.conversations = c; 
    this.participants = p; 
    this.userRepository = userRepo;
    this.ids = ids; 
  }

  @Transactional
  public Conversation create(String creatorId, String type, List<String> members, String title) {
    var conv = Conversation.builder()
        .id(ids.newId())
        .type(Conversation.ConversationType.valueOf(type.toUpperCase()))
        .title(title)
        .createdBy(creatorId)
        .build();
    conversations.save(conv);
    for (int i = 0; i < members.size(); i++) {
      var role = members.get(i).equals(creatorId) ? ConversationParticipant.Role.OWNER : ConversationParticipant.Role.MEMBER;
      participants.save(ConversationParticipant.builder()
          .conversationId(conv
            .getId())
            .userId(members.get(i))
            .role(role)
            .joinedAt(Instant.now())
            .build());
    }
    return conv;
  }

  public List<ConversationParticipant> getParticipants(String conversationId) {
    return participants.findByConversationId(conversationId);
  }

  @Transactional(readOnly = true)
  public List<ParticipantWithUserInfo> getParticipantsWithUserInfo(String conversationId) {
    List<ConversationParticipant> participantList = participants.findByConversationId(conversationId);
    
    return participantList.stream()
        .map(participant -> {
            // Convert userId string to Long and fetch user details
            Long userId = Long.valueOf(participant.getUserId());
            Users user = userRepository.findById(userId)
                .orElse(null);
            
            return ParticipantWithUserInfo.builder()
                .participant(participant)
                .userInfo(user != null ? ParticipantWithUserInfo.UserInfo.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build() : null)
                .build();
        })
        .collect(Collectors.toList());
  }
} 