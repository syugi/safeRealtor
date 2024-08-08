package com.loadone.safeRealtor.repository;

import com.loadone.safeRealtor.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
}
