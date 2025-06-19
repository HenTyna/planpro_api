package com.planprostructure.planpro.payload.proTalk;

import java.util.Set;

public record ConversationRequest(
        boolean isGroup,
        String name,
        Set<Long> memberIds
) {
}
