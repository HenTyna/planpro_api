package com.planprostructure.planpro.domain.chatRepo;

import com.planprostructure.planpro.domain.chat.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {} 