package com.planprostructure.planpro.payload.proTalk;

import com.planprostructure.planpro.enums.MessageStatus;

public record StatusUpdateRequest(
        Long messageId,
        MessageStatus status
) {
}
