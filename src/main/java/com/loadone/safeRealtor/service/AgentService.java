package com.loadone.safeRealtor.service;

import com.loadone.safeRealtor.model.Agent;
import com.loadone.safeRealtor.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    public Agent registerAgent(Agent agent) {
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Agent agent) {
        Agent existingAgent = agentRepository.findById(agent.getId()).orElseThrow();
        existingAgent.setName(agent.getName());
        existingAgent.setLicenseNumber(agent.getLicenseNumber());
        existingAgent.setPhoneNumber(agent.getPhoneNumber());
        existingAgent.setEmail(agent.getEmail());
        return agentRepository.save(existingAgent);
    }
}
