package com.planprostructure.planpro.domain.chat;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "messages",
  indexes = {
    @Index(name = "idx_msgs_conv_seq", columnList = "conversationId,messageSeq"),
    @Index(name = "idx_msgs_conv_created", columnList = "conversationId,createdAt DESC")
  }
)
public class Message {
  @Id
  private String id;

  @Column(nullable = false)
  private String conversationId;

  @Column(nullable = true)
  private Long messageSeq; // monotonic per conversation

  @Column(nullable = false)
  private String senderId;

  @Column(length = 4000)
  private String text;

  private String replyTo; // message id

  @Column(columnDefinition = "text")
  private String mentionsJson; // JSON string array for simplicity

  private boolean edited;

  @CreationTimestamp
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

  @Enumerated(EnumType.STRING)
  private Status status; // SENT, DELETED

  public enum Status { SENT, DELETED }
} 