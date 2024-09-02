package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.RegisterAgentReqDTO;
import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.entity.Agent;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.AgentRepository;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
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
    public void registerAgent(RegisterAgentReqDTO request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new BaseException(ErrorCode.DUPLICATED_USER_ID);
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BaseException(ErrorCode.DUPLICATED_PHONE_NUMBER);
        }

        boolean isRegister = authService.register(RegisterUserReqDTO.builder()
                .userId(request.getUserId())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build(), User.ROLE_AGENT);

        if (!isRegister) {
            throw new BaseException(ErrorCode.REGISTRATION_FAILED);
        }

        try {
            User savedUser = userRepository.findByUserId(request.getUserId()).orElseThrow();

            Agent agent = new Agent();
            agent.setUser(savedUser);
            agent.setEmail(request.getEmail());
            agent.setLicenseNumber(request.getLicenseNumber());
            agent.setName(request.getName());
            agentRepository.save(agent);
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        } catch (JpaSystemException e) {
            throw new BaseException(ErrorCode.DATABASE_ERROR);
        }

    }

    public Agent updateAgent(Agent agent) {
        Agent existingAgent = agentRepository.findById(agent.getId()).orElseThrow();
        existingAgent.setName(agent.getName());
        existingAgent.setLicenseNumber(agent.getLicenseNumber());
        existingAgent.setEmail(agent.getEmail());
        return agentRepository.save(existingAgent);
    }
}
