package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.ChatMessage;
import com.loadone.saferealtor.repository.ChatMessageRepository;
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
