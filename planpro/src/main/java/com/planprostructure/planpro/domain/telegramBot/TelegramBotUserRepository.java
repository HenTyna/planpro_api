package com.planprostructure.planpro.domain.telegramBot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TelegramBotUserRepository extends JpaRepository<TelegramBotUser, Long> {
    @Query("SELECT t FROM TelegramBotUser t WHERE t.chatId = ?1")
    Optional<TelegramBotUser> findByChatId(Long chatId);
    Optional<TelegramBotUser> findByUserId(Long userId);
}
