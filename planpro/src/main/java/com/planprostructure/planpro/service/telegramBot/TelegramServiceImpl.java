package com.planprostructure.planpro.service.telegramBot;

import com.planprostructure.planpro.components.common.api.StatusCode;
import com.planprostructure.planpro.domain.telegramBot.TelegramBotUser;
import com.planprostructure.planpro.domain.telegramBot.TelegramBotUserRepository;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.enums.BotState;
import com.planprostructure.planpro.exception.BusinessException;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.telegram.TelegramMainResponse;
import com.planprostructure.planpro.payload.telegram.TelegramUserInfoResponse;
import com.planprostructure.planpro.utils.Regex;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final TelegramBotUserRepository telegramBotUserRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(TelegramServiceImpl.class);

    @Override
    @Transactional
    public String registerUser(User user, long chatId) {
        return telegramBotUserRepository.findByChatId(chatId)
                .map(existingUser -> handleExistingUser(existingUser, user))
                .orElseGet(() -> createNewUser(user, chatId));
    }


    @Override
    @Transactional
    public void updateUserPhone(long chatId, String phoneNumber) {
        telegramBotUserRepository.findByChatId(chatId)
                .ifPresent(user -> {
                    user.setPhoneNumber(phoneNumber);
                    telegramBotUserRepository.save(user);
                });
    }

    @Override
    public void connectTelegramUserToPlanProUser(Long chatId) {
        Long userId = AuthHelper.getUserId();
        var telegramUserOpt = telegramBotUserRepository.findByChatId(chatId);
        if (telegramUserOpt.isPresent()) {
            TelegramBotUser telegramUser = telegramUserOpt.get();
            telegramUser.setUserId(userId);
           var saveTelegram =  telegramBotUserRepository.save(telegramUser);
           Long savedTeleId = saveTelegram.getChatId();
           if (savedTeleId != null){
               userRepository.findById(userId).ifPresent(user -> {
                     user.setTelegramId(savedTeleId);
                     userRepository.save(user);
               });
           }
            logger.info("Connected Telegram user {} to PlanPro user {}", chatId, userId);
        } else {
            logger.warn("No Telegram user found with chat ID: {}", chatId);
        }
    }

    @Override
    public Object verifyTelegramUser(Long chatId) {
        var telegramUserOpt = telegramBotUserRepository.findByChatId(chatId).
                orElseThrow(() -> new BusinessException(StatusCode.NOT_FOUND));
        return TelegramUserInfoResponse.builder().
                id(telegramUserOpt.getChatId()).
                firstName(telegramUserOpt.getFirstName()).
                lastName(telegramUserOpt.getLastName()).
                username(telegramUserOpt.getUsername()).
                build();
    }


    private String createNewUser(User telegramUser, long chatId) {
        TelegramBotUser newUser = new TelegramBotUser();
        newUser.setChatId(chatId);
        newUser.setUsername(telegramUser.getUserName());
        newUser.setFirstName(telegramUser.getFirstName());
        newUser.setLastName(telegramUser.getLastName());
        newUser.setActive(true);
        newUser.setCurrentState(BotState.STARTED.name());

        try {
            telegramBotUserRepository.save(newUser);
            return messageRegistrationSuccess(telegramUser, chatId);
        } catch (DataIntegrityViolationException e) {
            logger.warn("Duplicate user registration attempted: {}", newUser.getChatId());
            return "👋 Welcome back! You’ve already registered. (cached)";
        }

    }

    private String messageRegistrationSuccess(User telegramUser, long chatId) {
        return  "👋 Welcome, " + telegramUser.getFirstName() + "!\n\n" +
                "🔑 Your Chat ID: " + chatId + "\n" +
                "📝 Please use this Chat ID to link your Telegram account with the web app.\n\n" +
                "👉 Let's get started!";
    }

    private String handleExistingUser(TelegramBotUser existingUser, User telegramUser) {
        boolean updated = false;

        if (!Objects.equals(existingUser.getFirstName(), telegramUser.getFirstName())) {
            existingUser.setFirstName(telegramUser.getFirstName());
            updated = true;
        }
        if (!Objects.equals(existingUser.getLastName(), telegramUser.getLastName())) {
            existingUser.setLastName(telegramUser.getLastName());
            updated = true;
        }
        if (!Objects.equals(existingUser.getUsername(), telegramUser.getUserName())) {
            existingUser.setUsername(telegramUser.getUserName()); // may still be null; it's fine
            updated = true;
        }

//        existingUser.setLastActiveAt(java.time.Instant.now());
        updated = true;

        if (updated) {
            telegramBotUserRepository.save(existingUser);
        }

        return "Welcome back, " + telegramUser.getFirstName() +"!";
    }

}
