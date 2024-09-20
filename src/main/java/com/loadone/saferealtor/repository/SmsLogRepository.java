package com.loadone.saferealtor.repository;

import com.loadone.saferealtor.model.entity.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsLogRepository extends JpaRepository<SmsLog, Long> {
    List<SmsLog> findByPhoneNumber(String phoneNumber);
}