package com.planprostructure.planpro.controller;

import com.planprostructure.planpro.components.common.api.ProPlanRestController;
import com.planprostructure.planpro.payload.weTalk.ContactRequest;
import com.planprostructure.planpro.payload.weTalk.SendMessageRequest;
import com.planprostructure.planpro.service.proTalk.ContactService;
import com.planprostructure.planpro.service.proTalk.ConversationService;
import com.planprostructure.planpro.service.proTalk.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wb/v1/chat")
@RequiredArgsConstructor
@Tag(name = "ProTalk", description = "ProTalk API for managing contacts, conversations, and messages")
public class ProTalkController extends ProPlanRestController {

    private final ContactService chatService;
    private final ConversationService conversationService;
    private final MessageService messageService;


    @GetMapping("/contacts")
    public Object getUserContacts() throws Throwable{
        return ok(chatService.getUserContacts());
    }

    @GetMapping("/contacts/request")
    public Object getPendingContactsRequest() throws Throwable{
        return ok(chatService.getPendingContactsRequest());
    }

    @PatchMapping("/contacts/{contactId}/accept")
    public Object acceptContactRequest(@PathVariable Long contactId) throws Throwable {
        return ok(chatService.acceptContactRequest(contactId));
    }

    @PatchMapping("/contacts/{contactId}/reject")
    public Object rejectContactRequest(@PathVariable Long contactId) throws Throwable {
        return ok(chatService.rejectContactRequest(contactId));
    }

    @PostMapping("/contacts")
    public Object addContact(@RequestBody ContactRequest request) throws Throwable {
        chatService.addContact(request);
        return ok();
    }

    @GetMapping("/my_contacts")
    public Object getMyContacts() throws Throwable {
        return ok(chatService.getMyContact());
    }


    // Conversation Endpoints

    @GetMapping("/conversations")
    public Object getUserConversations() throws Throwable {
        return ok(conversationService.getUserConversations());
    }

    @GetMapping("/conversations/{conversationId}")
    public Object getConversationById(@PathVariable Long conversationId)  throws Throwable  {
        return ok(conversationService.getConversationById(conversationId));
    }

    @GetMapping("/conversations/unread_count/{conversationId}")
    public Object getUnreadMessageCount(@PathVariable Long conversationId) throws Throwable {
        return ok(conversationService.getUnreadMessageCounts(conversationId));
    }

    @PostMapping("/conversations/{userId}")
    public Object createConversationDirect(@PathVariable Long userId) throws Throwable {
        conversationService.createDirectConversation(userId);
        return ok();
    }

    @PostMapping("/conversations/as_read/{conversationId}")
    public Object markConversationAsRead(@PathVariable Long conversationId) throws Throwable {
        conversationService.markConversationAsRead(conversationId);
        return ok();
    }


    // Message Endpoints
    @GetMapping("/messages/{conversationId}")
    public Object getConversationMessages(@PathVariable Long conversationId) throws Throwable {
        return ok(messageService.getConversationMessages(conversationId));
    }
    @PostMapping("/messages/{conversationId}")
    public Object sendMessage(@PathVariable Long conversationId, @RequestBody SendMessageRequest request) throws Throwable {
        messageService.sendMessage(conversationId, request);
        return ok();
    }

    @DeleteMapping("/messages/{messageId}")
    public Object deleteMessage(@PathVariable Long messageId) throws Throwable {
        messageService.deleteMessage(messageId);
        return ok();
    }

}
