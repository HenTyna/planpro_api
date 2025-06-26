//package com.planprostructure.planpro.payload.proTalk;
//
//import com.planprostructure.planpro.domain.proTalk.UserChat;
//import com.planprostructure.planpro.domain.users.Users;
//
//public record UserResponse(
//        Long id,
//        String username,
//        String profilePicture
//) {
//    public static UserResponse fromEntity(UserChat users) {
//        return new UserResponse(
//                users.getId(),
//                users.getUsername(),
//                users.getProfilePicture() != null ? users.getProfilePicture() : "default-profile.png"
//        );
//    }
//}
