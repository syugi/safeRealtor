package com.loadone.saferealtor.service;

import com.loadone.saferealtor.util.SMSUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
    private final SMSUtil smsUtil;

    public boolean sendSms(String phoneNumber, String message) {
        try {
            smsUtil.sendSMS(phoneNumber, message);
            logger.info("Sending SMS to {}: {}", phoneNumber, message);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", phoneNumber, e.getMessage());
            return false;
        }
    }
}
