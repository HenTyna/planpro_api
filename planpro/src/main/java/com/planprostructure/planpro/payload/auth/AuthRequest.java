package com.planprostructure.planpro.payload.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {
    @NotNull
    @JsonProperty("user_name")
    private String username;

    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("password")
    private String password;
}
