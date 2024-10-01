package com.loadone.saferealtor.model.dto;


import lombok.Data;

@Data
public class TokenReqDTO {
    private String accessToken;
    private String refreshToken;
}
