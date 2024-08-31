package com.loadone.saferealtor.model.dto;

import lombok.Data;

@Data
public class RegisterUserReqDTO {
    private String userId;
    private String password;
    private String phoneNumber;
}