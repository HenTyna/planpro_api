package com.planprostructure.planpro.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByResetToken(String token);
    Optional<String> findRoleById(Long id);

    @Query("SELECT u FROM Users u WHERE u.id = ?1")
    Optional<Users> findByUserByUserId(Long userId);
}
