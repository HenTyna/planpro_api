package com.planprostructure.planpro.controller;

import com.planprostructure.planpro.components.common.api.ProPlanRestController;
import com.planprostructure.planpro.payload.MultiSortBuilder;
import com.planprostructure.planpro.payload.proTalk.*;
import com.planprostructure.planpro.service.proTalk.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wb/v1/chat")
@RequiredArgsConstructor
public class ProTalkController extends ProPlanRestController {

    private final ChatService chatService;

//    @PostMapping("/users")
//    public Object createUser(@RequestParam Long phoneNumber, @RequestParam String username){
//        chatService.createUser(phoneNumber, username);
//        return ok();
//    }

    @GetMapping("/users")
    public Object getAllUsers() {
        return ok(chatService.getAllUsers());
    }

    @PostMapping("/conversations")
    public Object createConversation(@RequestBody ConversationRequest request) {
        return ok(chatService.createConversation(request));
    }

    @GetMapping("/conversations/{userId}")
    public Object getUserConversations(@PathVariable Long userId) {
        return ok(chatService.getUserConversations(userId));
    }

    @GetMapping("/conversations/{conversationId}")
    public Object getConversation(@PathVariable Long conversationId) {
        return ok(chatService.getConversation(conversationId));
    }

    @PostMapping("/messages")
    public Object sendMessage(@RequestBody MessageRequest request) {
        return ok(chatService.sendMessage(request));
    }

    @GetMapping("/messages/{conversationId}")
    public Object getConversationMessages(@PathVariable Long conversationId) {
        return ok(chatService.getConversationMessages(conversationId));
    }

    @PatchMapping("/messages/status")
    public Object updateMessageStatus(@RequestBody StatusUpdateRequest request) {
        return ok(chatService.updateMessageStatus(request));
    }

    @PostMapping("/messages/reactions")
    public Object addReaction(@RequestBody ReactionRequest request) {
        return ok(chatService.addReaction(request));
    }
}
