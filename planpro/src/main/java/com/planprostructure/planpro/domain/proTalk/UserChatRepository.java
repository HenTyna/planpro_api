//package com.planprostructure.planpro.domain.proTalk;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.Optional;
//
//public interface UserChatRepository extends JpaRepository<UserChat, Long> {
//    @Query("SELECT u FROM UserChat u WHERE u.userId = ?1")
//    UserChat findByUserId(Long userId);
//}
