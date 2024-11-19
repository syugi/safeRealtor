package com.loadone.saferealtor.model.dto;

import com.loadone.saferealtor.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    Long userSeq;
    String userId;
    String name;
    String phoneNumber;
    String email;
    String role;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public UserDTO(User user) {
        this.userSeq = user.getUserSeq();
        this.userId = user.getUserId();
        this.name = user.getUserId();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.role = user.getRoleName();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
