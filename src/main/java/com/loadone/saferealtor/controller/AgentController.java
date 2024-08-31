package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.RegisterAgentReqDTO;
import com.loadone.saferealtor.model.entity.Agent;
import com.loadone.saferealtor.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    // 중개사 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerAgent(@RequestBody RegisterAgentReqDTO request) {
        if (agentService.registerAgent(request)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
