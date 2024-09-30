package com.loadone.saferealtor.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoDTO {
    private String userId;
    private int role;
}
