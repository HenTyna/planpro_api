package com.planprostructure.planpro.payload.telegram;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelegramUserInfoResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;

    @Builder
    public TelegramUserInfoResponse(Long id, String username, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
    }
}
