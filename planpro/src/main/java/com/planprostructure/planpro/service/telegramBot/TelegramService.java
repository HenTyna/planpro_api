package com.planprostructure.planpro.service.telegramBot;

import com.planprostructure.planpro.domain.telegramBot.TelegramBotUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public interface TelegramService {
    String registerUser(User user, long chatId);
    void updateUserPhone(long chatId, String phoneNumber);

    void connectTelegramUserToPlanProUser(Long chatI) throws Throwable;

    Object verifyTelegramUser(Long chatId) throws Throwable;
}
