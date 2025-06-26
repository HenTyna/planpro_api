//package com.planprostructure.planpro.domain.proTalk;
//
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//@Table(name = "tb_user_chat")
//@NoArgsConstructor
//@Setter
//@Getter
//public class UserChat {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "usr_id")
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String username;
//
//    @Column(name = "user_id", nullable = false, unique = true)
//    private Long userId;
//
//    @Column(name = "phone_number")
//    private Integer phoneNumber;
//
//    @Column(name = "profile_image_url")
//    private String profilePicture;
//
//    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
//    private Set<Conversations> conversations = new HashSet<>();
//
//    @Builder
//    public UserChat(Long id, String username) {
//        this.id = id;
//        this.username = username;
//    }
//}