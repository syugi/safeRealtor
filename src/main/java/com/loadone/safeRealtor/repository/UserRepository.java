package com.loadone.safeRealtor.repository;

import com.loadone.safeRealtor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
