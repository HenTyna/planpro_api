package com.planprostructure.planpro.service.chat;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// For demo only; production should use DB sequence per conversation or advisory locks.
@Service
public class MessageSeqService {
  private final ConcurrentHashMap<String, AtomicLong> map = new ConcurrentHashMap<>();
  public long next(String conversationId, long currentMax) {
    var atom = map.computeIfAbsent(conversationId, k -> new AtomicLong(currentMax));
    return atom.incrementAndGet();
  }
} 