package com.loadone.saferealtor.repository;

import com.loadone.saferealtor.model.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByPhoneNumber(String phoneNumber);
}