package com.planprostructure.planpro.payload.weTalk;

import com.planprostructure.planpro.domain.proTalk.Contact;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.enums.ContactStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {
    private Long id;
    private Long userId;
    private String userDisplayName;
    private String userAvatarUrl;
    private Long contactUserId;
    private String contactUserDisplayName;
    private String contactUserAvatarUrl;
    private ContactStatus status;
    private LocalDateTime addedAt;

    public static ContactResponse fromContact(Contact contact, Users currentUser) {
        Users otherUser = contact.getUser().equals(currentUser) ?
                contact.getContactUser() : contact.getUser();

        return ContactResponse.builder()
                .id(contact.getId())
                .userId(contact.getUser().getId())
                .contactUserId(contact.getContactUser().getId())
                .status(ContactStatus.valueOf(contact.getStatus().name()))
                .userDisplayName(contact.getUser().getUsername())
                .userAvatarUrl(contact.getUser().getProfileImageUrl())
                .contactUserDisplayName(otherUser.getUsername())
                .addedAt(contact.getAddedAt())
                .build();
    }
}