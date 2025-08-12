package com.planprostructure.planpro.domain.chatRepo;

import com.planprostructure.planpro.domain.chat.MessageReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageReceiptRepository extends JpaRepository<MessageReceipt, MessageReceipt.PK> {
  List<MessageReceipt> findByMessageId(String messageId);
} 