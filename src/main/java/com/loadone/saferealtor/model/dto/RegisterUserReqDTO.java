package com.loadone.saferealtor.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserReqDTO {
    private String userId;
    private String password;
    private String phoneNumber;
    private int role;
}