package com.loadone.saferealtor.model.dto;

import com.loadone.saferealtor.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    Long id;
    String userId;
    String name;
    String phoneNumber;
    String role;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public UserDTO(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.name = user.getUserId();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRoleName();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
