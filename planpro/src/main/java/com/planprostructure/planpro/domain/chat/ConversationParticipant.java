package com.planprostructure.planpro.domain.chat;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity(name = "ChatConversationParticipant") @Table(name = "conversation_participants")
@IdClass(ConversationParticipant.PK.class)
public class ConversationParticipant {
  @Id
  private String conversationId;
  @Id
  private String userId;

  @Enumerated(EnumType.STRING)
  private Role role; // OWNER, ADMIN, MEMBER

  private boolean muted;
  private String lastReadMessageId;
  private Instant joinedAt;

  public enum Role { OWNER, ADMIN, MEMBER }

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class PK implements Serializable {
    private String conversationId; private String userId;
  }
} 