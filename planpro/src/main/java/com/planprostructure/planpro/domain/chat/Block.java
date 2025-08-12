package com.planprostructure.planpro.domain.chat;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "blocks")
@IdClass(Block.PK.class)
public class Block {
  @Id
  private String userId;
  @Id
  private String blockedUserId;
  private Instant createdAt;

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class PK implements Serializable { private String userId; private String blockedUserId; }
} 