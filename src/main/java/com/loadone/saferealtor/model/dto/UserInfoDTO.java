package com.loadone.saferealtor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserInfoDTO {
    private String userId;
    private String roleName;
}
