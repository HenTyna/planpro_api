package com.planprostructure.planpro.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.username = ?1")
    List<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByResetToken(String token);
    @Query("SELECT u FROM Users u WHERE u.id = ?1 AND u.username = ?2") //check again
    Optional<Users> findByUserIdAndUsername(Long id, String username);

    @Query("SELECT u FROM Users u WHERE u.phoneNumber = ?1")
    List<Users> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE Users u SET u.username = ?1, u.email = ?2, u.firstName = ?3, u.lastName = ?4, " +
            "u.phoneNumber = ?5,  u.dateOfBirth = ?6, u.profileImageUrl = ?7 WHERE u.id = ?8")
    int updateProfile(String username, String email, String firstName, String lastName,
                      String phoneNumber, String dob, String imageUrl, Long userId);
}
