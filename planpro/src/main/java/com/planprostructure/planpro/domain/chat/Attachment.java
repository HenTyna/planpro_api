package com.planprostructure.planpro.domain.chat;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "attachments")
public class Attachment {
  @Id
  private String id;
  private String messageId;
  private String type; // image|video|audio|file
  private String objectKey; // local path or S3 key
  private Integer sizeBytes;
  private Integer width;
  private Integer height;
  private String mime;
} 