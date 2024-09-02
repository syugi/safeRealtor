package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.RegisterAgentReqDTO;
import com.loadone.saferealtor.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/register")
    public ResponseEntity<?> registerAgent(@RequestBody RegisterAgentReqDTO request) {
        agentService.registerAgent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
