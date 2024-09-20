package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.SmsLog;
import com.loadone.saferealtor.repository.SmsLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsLogService {

    @Autowired
    private SmsLogRepository smsLogRepository;

    public List<SmsLog> getAllSmsLogs() {
        return smsLogRepository.findAll();
    }

    public List<SmsLog> getSmsLogsByPhoneNumber(String phoneNumber) {
        return smsLogRepository.findByPhoneNumber(phoneNumber);
    }
}