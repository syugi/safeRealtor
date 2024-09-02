package com.loadone.saferealtor.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
    public boolean sendSms(String phoneNumber, String message) {
        System.out.println("Sending SMS to " + phoneNumber + ": " + message);
        return true;
    }
}
