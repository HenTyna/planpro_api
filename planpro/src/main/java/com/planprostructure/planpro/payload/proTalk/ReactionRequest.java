package com.planprostructure.planpro.payload.proTalk;

public record ReactionRequest(
        Long messageId,
        String reaction
) {
}
