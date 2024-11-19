package com.loadone.saferealtor.model.dto;

import com.loadone.saferealtor.model.entity.Agent;
import com.loadone.saferealtor.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterAgentResDTO {
    private Long agentSeq;
    private String name;
    private String licenseNumber;
    private String email;
    private Long userSeq;
    private String userId;
    private String phoneNumber;
    private String role;

    public RegisterAgentResDTO(Agent agent){
        this.agentSeq = agent.getAgentSeq();
        this.licenseNumber = agent.getLicenseNumber();

        User user = agent.getUser();
        this.userSeq = user.getUserSeq();
        this.userId = user.getUserId();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRoleName();
    }
}
