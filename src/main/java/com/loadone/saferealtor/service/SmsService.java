package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.SmsLog;
import com.loadone.saferealtor.repository.SmsLogRepository;
import com.loadone.saferealtor.util.SMSUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
    private final SMSUtil smsUtil;
    private final SmsLogRepository smsLogRepository;

    public boolean sendSms(String phoneNumber, String message) {
        boolean success = false;
        String errorMessage = null;

        try {
            String result = smsUtil.sendSMS(phoneNumber, message);
            success = true;
            logger.info("Sending SMS result: {}", result);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            logger.error("Failed to send SMS to {}: {}", phoneNumber, e.getMessage());
        }

        // 발송 내역 저장
        SmsLog smsLog = SmsLog.builder()
                .phoneNumber(phoneNumber)
                .message(message)
                .success(success)
                .errorMessage(errorMessage)
                .sentAt(LocalDateTime.now())
                .build();
        smsLogRepository.save(smsLog);

        return success;
    }
}
