package com.planprostructure.planpro.domain.chat;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "message_receipts")
@IdClass(MessageReceipt.PK.class)
public class MessageReceipt {
  @Id
  private String messageId;
  @Id
  private String userId;
  @Id
  @Enumerated(EnumType.STRING)
  private State state; // DELIVERED, READ

  private Instant at;

  public enum State { DELIVERED, READ }

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class PK implements Serializable { private String messageId; private String userId; private State state; }
} 