package com.loadone.safeRealtor.service;

import com.loadone.safeRealtor.model.entity.ChatMessage;
import com.loadone.safeRealtor.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessagesByInquiryId(Long inquiryId) {
        return chatMessageRepository.findByInquiryId(inquiryId);
    }
}
