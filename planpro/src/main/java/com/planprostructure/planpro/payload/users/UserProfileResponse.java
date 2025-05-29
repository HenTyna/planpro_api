package com.planprostructure.planpro.payload.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String role; // e.g., "USER", "ADMIN", etc.
    private String status; // e.g., "ACTIVE", "INACTIVE", etc.

    @Builder
    public UserProfileResponse(Long id, String username, String email, String phoneNumber, String role, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
    }
}
