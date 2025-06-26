package com.planprostructure.planpro.controller;

import com.planprostructure.planpro.components.common.api.ProPlanRestController;
import com.planprostructure.planpro.service.telegramBot.TelegramService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wb/v1/telegram")
@RequiredArgsConstructor
@Tag(name = "Telegram Bot", description = "Telegram Bot API")
public class TelegramController extends ProPlanRestController {
    private final TelegramService telegramService;

    @PutMapping("/connect-telegram/{chatId}")
    public Object connectTelegramUserToPlanProUser(@PathVariable Long chatId) throws Throwable {
        telegramService.connectTelegramUserToPlanProUser(chatId);
        return ok();
    }

    @GetMapping("/verify-telegram/{chatId}")
    public Object verifyTelegramUser(@PathVariable Long chatId) throws Throwable {
        return ok(telegramService.verifyTelegramUser(chatId));
    }

    @GetMapping("/get-telegram-user-info")
    public Object getTelegramUserInfo() throws Throwable {
        return ok(telegramService.getTelegramUserByChatId());
    }

}
