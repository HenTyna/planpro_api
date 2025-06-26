package com.planprostructure.planpro.payload.weTalk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyContactResponse {
    private Long userId;
    private Long conversationId;
    private String username;
    private String avatarUrl;
    private String lastMessage;
    private String lastMessageTime;
    private String messageDate;

}
