package com.loadone.safeRealtor.service;

import com.loadone.safeRealtor.model.ChatMessage;
import com.loadone.safeRealtor.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessagesByInquiryId(Long inquiryId) {
        return chatMessageRepository.findByInquiryId(inquiryId);
    }
}
