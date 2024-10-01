package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.RegisterAgentReqDTO;
import com.loadone.saferealtor.model.dto.RegisterAgentResDTO;
import com.loadone.saferealtor.model.entity.Agent;
import com.loadone.saferealtor.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    // 중개사 등록
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<RegisterAgentResDTO> registerAgent(@RequestBody RegisterAgentReqDTO request) {
        try {
            Agent agent = agentService.registerAgent(request);

            RegisterAgentResDTO response = new RegisterAgentResDTO(agent);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_TO_REGISTER_AGENT,e);
        }
    }
}
