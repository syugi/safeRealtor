package com.loadone.safeRealtor.model.dto;

import lombok.Data;

@Data
public class VerificationReqDTO {
    private String phoneNumber;
    private String verificationCode;
}