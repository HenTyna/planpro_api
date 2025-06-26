package com.planprostructure.planpro.payload.weTalk;

import com.planprostructure.planpro.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private String content;
    private MessageType messageType;
    private String fileUrl;
    private Long replyToMessageId;
}