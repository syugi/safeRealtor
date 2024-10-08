package com.loadone.saferealtor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LoginResDTO {
    private String userId;
    private String role;
    private String roleDisplayName;
    private String accessToken;
    private String refreshToken;
}
