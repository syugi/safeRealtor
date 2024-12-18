package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.RegisterAgentReqDTO;
import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.entity.Agent;
import com.loadone.saferealtor.model.entity.Role;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.AgentRepository;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    // 중개사 등록
    @Transactional
    public Agent registerAgent(RegisterAgentReqDTO request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new BaseException(ErrorCode.DUPLICATED_USER_ID);
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BaseException(ErrorCode.DUPLICATED_PHONE_NUMBER);
        }

        User user = authService.registerUser(RegisterUserReqDTO.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build(), Role.ROLE_AGENT);

        Agent agent = new Agent();
        agent.setUser(user);
        agent.setLicenseNumber(request.getLicenseNumber());
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Agent agent) {
        Agent existingAgent = agentRepository.findById(agent.getAgentSeq()).orElseThrow();
        existingAgent.setLicenseNumber(agent.getLicenseNumber());
        return agentRepository.save(existingAgent);
    }
}
