package com.planprostructure.planpro.service.chat;

import com.planprostructure.planpro.domain.chat.Attachment;
import com.planprostructure.planpro.domain.chat.Message;
import com.planprostructure.planpro.domain.chat.MessageReceipt;
import com.planprostructure.planpro.domain.chatRepo.AttachmentRepository;
import com.planprostructure.planpro.domain.chatRepo.MessageReceiptRepository;
import com.planprostructure.planpro.domain.chatRepo.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService {
  private final MessageRepository messages; private final AttachmentRepository attachments; private final MessageReceiptRepository receipts; private final IdService ids; private final MessageSeqService seqs; private final SimpMessagingTemplate broker;
  public MessageService(MessageRepository m, AttachmentRepository a, MessageReceiptRepository r, IdService ids, MessageSeqService seqs, SimpMessagingTemplate broker) {
    this.messages = m; this.attachments = a; this.receipts = r; this.ids = ids; this.seqs = seqs; this.broker = broker;
  }

  public Page<Message> list(String conversationId, int page, int size) {
    return messages.findByConversationIdOrderByMessageSeqAsc(conversationId, PageRequest.of(page, size));
  }

  @Transactional
  public Message send(String conversationId, String senderId, String text, List<Attachment> atts, String replyTo, String mentionsJson) {
    var last = messages.findTopByConversationIdOrderByMessageSeqDesc(conversationId);
    long nextSeq = seqs.next(conversationId, last == null ? 0 : last.getMessageSeq());
    var msg = Message.builder()
        .id(ids.newId())
        .conversationId(conversationId)
        .messageSeq(nextSeq)
        .senderId(senderId)
        .text(text)
        .replyTo(replyTo)
        .mentionsJson(mentionsJson)
        .status(Message.Status.SENT)
        .build();
    messages.save(msg);
    if (atts != null) {
      for (Attachment a : atts) { a.setId(ids.newId()); a.setMessageId(msg.getId()); attachments.save(a); }
    }
    // Fanout WS event to /topic/conversations.{id}
    broker.convertAndSend("/topic/conversations." + conversationId, msg);
    return msg;
  }

  @Transactional
  public void markDelivered(String messageId, String userId) {
    receipts.save(MessageReceipt.builder().messageId(messageId).userId(userId).state(MessageReceipt.State.DELIVERED).at(Instant.now()).build());
    broker.convertAndSend("/topic/receipts." + messageId, "DELIVERED:" + userId);
  }

  @Transactional
  public void markReadUpTo(String conversationId, String userId, String messageId) {
    receipts.save(MessageReceipt.builder().messageId(messageId).userId(userId).state(MessageReceipt.State.READ).at(Instant.now()).build());
    broker.convertAndSend("/topic/conversations." + conversationId + ".reads", userId + ":" + messageId);
  }
} 