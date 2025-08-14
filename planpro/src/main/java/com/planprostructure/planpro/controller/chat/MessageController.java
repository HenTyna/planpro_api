package com.planprostructure.planpro.controller.chat;

import com.planprostructure.planpro.domain.chat.Attachment;
import com.planprostructure.planpro.service.chat.MessageService;
import com.planprostructure.planpro.payload.dto.MessageDtos;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
  private final MessageService messages;
  public MessageController(MessageService m){ this.messages = m; }

  @GetMapping("/conversations/{id}/messages")
  public Page<?> list(@PathVariable String id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int limit) {
    return messages.list(id, page, limit);
  }

  @PostMapping("/conversations/{id}/messages")
  public Object send(@PathVariable String id, @Valid @RequestBody MessageDtos.SendRequest req, Authentication auth) {
    String username = auth.getName();
    List<Attachment> atts = null;
    if (req.attachments() != null) {
      atts = req.attachments().stream().map(a -> Attachment.builder().type(a.type()).objectKey(a.objectKey()).width(a.width()).height(a.height()).build()).toList();
    }
    String mentionsJson = req.mentions() == null ? null : req.mentions().toString();
    return messages.send(id, username, req.text(), atts, req.replyTo(), mentionsJson);
  }

  @PostMapping("/messages/{messageId}/delivered")
  public Object delivered(@PathVariable String messageId, Authentication auth) { 
    messages.markDelivered(messageId, auth.getName()); 
    return "Message delivered";
  }

  @PostMapping("/conversations/{convId}/read")
  public Object readUpTo(@PathVariable String convId, @RequestParam String messageId, Authentication auth) { 
    messages.markReadUpTo(convId, auth.getName(), messageId); 
    return "Message read";
  }
} 