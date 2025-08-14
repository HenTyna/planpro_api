package com.planprostructure.planpro.domain.chatRepo;

import com.planprostructure.planpro.domain.chat.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactsRepository extends JpaRepository<Contacts, String> {
    
    @Query("SELECT c FROM Contacts c WHERE c.userId = :userId")
    List<Contacts> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT c FROM Contacts c WHERE c.userId = :userId AND c.phone = :phone")
    Optional<Contacts> findByUserIdAndPhone(@Param("userId") Long userId, @Param("phone") String phone);
    
    @Query("SELECT c FROM Contacts c WHERE c.userId = :userId AND c.id = :contactId")
    Optional<Contacts> findByUserIdAndContactId(@Param("userId") Long userId, @Param("contactId") String contactId);
    
    @Query("SELECT c FROM Contacts c WHERE c.phone = :phone")
    List<Contacts> findByPhone(@Param("phone") String phone);
} 