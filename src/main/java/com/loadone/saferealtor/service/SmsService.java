package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.SmsLog;
import com.loadone.saferealtor.repository.SmsLogRepository;
import com.loadone.saferealtor.util.SMSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class SmsService {

    private final SMSUtil smsUtil;
    private final SmsLogRepository smsLogRepository;

    public boolean sendSms(String phoneNumber, String message) {
        boolean success = false;
        String errorMessage = null;

        try {
            String result = smsUtil.sendSMS(phoneNumber, message);
            success = true;
            log.info("Sending SMS result: {}", result);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error("Failed to send SMS to {}: {}", phoneNumber, e.getMessage());
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
