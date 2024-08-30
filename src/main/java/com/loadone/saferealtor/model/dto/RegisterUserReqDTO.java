package com.loadone.saferealtor.model.dto;

import lombok.Data;

@Data
public class RegisterUserReqDTO {
    private String username;
    private String password;
    private String phoneNumber;
}