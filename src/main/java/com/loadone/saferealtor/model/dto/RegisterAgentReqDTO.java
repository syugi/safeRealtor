package com.loadone.saferealtor.model.dto;
import lombok.Data;

@Data
public class RegisterAgentReqDTO {
    private String name;
    private String licenseNumber;
    private String email;
    private String userId;
    private String password;
    private String phoneNumber;
}
