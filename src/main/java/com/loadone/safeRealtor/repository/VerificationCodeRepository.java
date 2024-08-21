package com.loadone.safeRealtor.repository;

import com.loadone.safeRealtor.model.entity.User;
import com.loadone.safeRealtor.model.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByPhoneNumber(String phoneNumber);
}