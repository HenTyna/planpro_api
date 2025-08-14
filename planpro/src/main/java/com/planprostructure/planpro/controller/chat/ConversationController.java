package com.planprostructure.planpro.controller.chat;

import com.planprostructure.planpro.service.chat.ConversationService;
import com.planprostructure.planpro.components.common.api.ProPlanRestController;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.dto.ConversationDtos;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController extends ProPlanRestController{
  private final ConversationService service;
  public ConversationController(ConversationService s){ this.service = s; }

  @PostMapping
  public Object create(@Valid @RequestBody ConversationDtos.CreateRequest req, Authentication auth) {
    String username = AuthHelper.getUsername();
    return service.create(username, req.type(), req.members(), req.title());
  }

  @GetMapping("/{id}/participants")
  public Object participants(@PathVariable String id) { return service.getParticipants(id); }
} 