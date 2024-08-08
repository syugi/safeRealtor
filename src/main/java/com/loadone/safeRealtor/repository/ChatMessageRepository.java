package com.loadone.safeRealtor.repository;

import com.loadone.safeRealtor.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByInquiryId(Long inquiryId);
}
