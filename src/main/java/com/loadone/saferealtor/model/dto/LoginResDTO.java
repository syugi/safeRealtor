package com.loadone.saferealtor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResDTO {
    private Long id;
    private String userId;
    private int role;
}
