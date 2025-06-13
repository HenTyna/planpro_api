package com.planprostructure.planpro.components.telegram;

import com.planprostructure.planpro.components.common.utils.ObjectUtils;
import com.planprostructure.planpro.logging.AppLogManager;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class MyTelegramBot extends TelegramLongPollingBot implements BotCommands {

    private static final Logger logger = LoggerFactory.getLogger(MyTelegramBot.class);

    @Value("${telegram-setting.username}")
    private String telegramBotUsername;

    @Value("${telegram-setting.enable}")
    private boolean telegramEnable;

    public MyTelegramBot(@Value("${telegram-setting.access-token}") String telegramBotToken) {
        super(telegramBotToken);
        if (telegramBotToken == null || telegramBotToken.isEmpty()) {
            throw new IllegalArgumentException("Telegram bot token must be provided");
        }
    }

    public boolean isEnabled() {
        return telegramEnable;
    }

    @Override
    public String getBotUsername() {
        return telegramBotUsername;
    }

    @Override
    public String getBotToken() {
        return super.getBotToken();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        long chatId;
        String username;

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            username = update.getMessage().getChat().getFirstName();
            String messageText = update.getMessage().getText();

            logger.info("Received message from {}: {}", username, messageText);

            if (messageText.equals("/start")) {
                sendMessage(chatId, username);
            } else {
                sendMessage(chatId, "I don't understand that command.");
            }
        }
    }

    public void sendMessage(long chatId, String username) {
        String text = "Congratulations " + username + "\n" +
                "You have successfully registered with PlanProBot! \n" +
                "Your user ID is: " + chatId + "\n" +
                "Powered by: PlanPro Management System \n\n"+
                "Thank you\n";

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        StringBuilder logRequest = new StringBuilder();
        logRequest.append("\n[Request]")
                .append("\n[Url] [POST ").append(MyTelegramBot.START_COMMAND).append("]")
                .append("\n[Body] [")
                .append(ObjectUtils.writerWithDefaultPrettyPrinter(message))
                .append("]\n");
        AppLogManager.info(MyTelegramBot.class, logRequest);

        try {
            Message response = execute(message);
            if (response != null) {
                logger.info("Message sent successfully to chat ID {}: {}", chatId, response.getText());
            } else {
                logger.warn("No response received for chat ID {}", chatId);
            }
        } catch (TelegramApiException e) {
            logger.error("Error sending message to chat ID {}: {}", chatId, e.getMessage());
        }


    }

    // Method to be called from your service when a reminder is set
    public void notifyUserAboutReminder(Long telegramChatId, String reminderDetails) {
        String message = "Reminder set successfully!\n" + reminderDetails;
        sendMessage(telegramChatId, message);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}