package com.planprostructure.planpro.service.chat;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class IdService { public String newId() { return UUID.randomUUID().toString(); } } 