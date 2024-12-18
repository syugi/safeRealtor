package com.loadone.saferealtor.model.dto;

import com.loadone.saferealtor.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUserResDTO {
    private long userSeq;
    private String userId;
    private String phoneNumber;
    private String role;

    public RegisterUserResDTO(User user){
        this.userSeq = user.getUserSeq();
        this.userId = user.getUserId();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRoleName();
    }
}
