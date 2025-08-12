package com.planprostructure.planpro.domain.chat;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "message_reactions")
@IdClass(MessageReaction.PK.class)
public class MessageReaction {
  @Id
  private String messageId;
  @Id
  private String userId;
  @Id
  private String emoji;
  private Instant at;

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class PK implements Serializable { private String messageId; private String userId; private String emoji; }
} 