package com.loadone.safeRealtor.repository;

import com.loadone.safeRealtor.model.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUserId(Long userId);
    List<Inquiry> findByPropertyId(Long propertyId);
}
