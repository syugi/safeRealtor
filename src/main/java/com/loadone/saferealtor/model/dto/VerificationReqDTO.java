package com.loadone.saferealtor.model.dto;

import lombok.Data;

@Data
public class VerificationReqDTO {
    private String phoneNumber;
    private String code;
}