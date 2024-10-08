package com.loadone.saferealtor.model.dto;

import com.loadone.saferealtor.model.entity.Agent;
import com.loadone.saferealtor.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterAgentResDTO {
    private Long agentId;
    private String name;
    private String licenseNumber;
    private String email;
    private Long id;
    private String userId;
    private String phoneNumber;
    private String role;



    public RegisterAgentResDTO(Agent agent){
        this.agentId = agent.getId();
        this.name = agent.getName();
        this.licenseNumber = agent.getLicenseNumber();
        this.email = agent.getEmail();

        User user = agent.getUser();
        this.id = user.getId();
        this.userId = user.getUserId();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRoleName();
    }
}
