package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.dto.RegisterAgentReqDTO;
import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.entity.Agent;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.AgentRepository;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    // 중개사 등록
    public boolean registerAgent(RegisterAgentReqDTO request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("이미 사용중 아이디 입니다.");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("이미 회원가입된 전화번호입니다.");
        }

        boolean isRegister = authService.register(RegisterUserReqDTO.builder()
                .userId(request.getUserId())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build(), User.ROLE_AGENT);

        if (!isRegister) {
            throw new RuntimeException("회원가입에 실패하였습니다.");
        }

        User savedUser = userRepository.findByUserId(request.getUserId()).orElseThrow();

        Agent agent = new Agent();
        agent.setUser(savedUser);
        agent.setEmail(request.getEmail());
        agent.setLicenseNumber(request.getLicenseNumber());
        agent.setName(request.getName());
        agentRepository.save(agent);

        return true;
    }

    public Agent updateAgent(Agent agent) {
        Agent existingAgent = agentRepository.findById(agent.getId()).orElseThrow();
        existingAgent.setName(agent.getName());
        existingAgent.setLicenseNumber(agent.getLicenseNumber());
        existingAgent.setEmail(agent.getEmail());
        return agentRepository.save(existingAgent);
    }
}
