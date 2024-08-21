package com.loadone.safeRealtor.model.dto;

import lombok.Data;

@Data
public class VerificationRequest {
    private String phoneNumber;
    private String verificationCode;
}