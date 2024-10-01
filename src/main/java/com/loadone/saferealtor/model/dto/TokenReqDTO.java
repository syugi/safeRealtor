package com.loadone.saferealtor.model.dto;


import lombok.Data;

@Data
public class TokenReqDTO {
    private String userId;
    private String refreshToken;
}
