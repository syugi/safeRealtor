package com.loadone.saferealtor.repository;

import com.loadone.saferealtor.model.entity.Role;
import com.loadone.saferealtor.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> findByRole(Role role);
}
