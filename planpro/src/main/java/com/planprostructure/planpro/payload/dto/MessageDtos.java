package com.planprostructure.planpro.payload.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class MessageDtos {
  public record SendRequest(@NotBlank String clientMsgId, String text, List<AttachmentRef> attachments, String replyTo, List<String> mentions) {}
  public record AttachmentRef(@NotBlank String type, @NotBlank String objectKey, Integer width, Integer height) {}
} 