package com.planprostructure.planpro.domain.proTalk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.planprostructure.planpro.components.MessageMetadataConverter;
import com.planprostructure.planpro.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_messages", indexes = {
        @Index(name = "idx_message_conversation", columnList = "conversation_id"),
        @Index(name = "idx_message_sender", columnList = "sender_id"),
        @Index(name = "idx_message_sent_at", columnList = "sentAt")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversations conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserChat sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Instant sentAt;

    @Convert(converter = MessageMetadataConverter.class)
    @Column(columnDefinition = "jsonb")
    private MessageMetadata metadata;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MessageStatus status;
}