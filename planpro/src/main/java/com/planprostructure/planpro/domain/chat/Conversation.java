package com.planprostructure.planpro.domain.chat;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "conversations")
public class Conversation {
  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ConversationType type; // DIRECT, GROUP

  private String title;
  private String photoUrl;

  private String createdBy;

  @CreationTimestamp
  private Instant createdAt;

  public enum ConversationType { DIRECT, GROUP }
} 