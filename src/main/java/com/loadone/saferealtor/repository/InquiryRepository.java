package com.loadone.saferealtor.repository;

import com.loadone.saferealtor.model.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUserId(Long userId);
    List<Inquiry> findByPropertyId(Long propertyId);
}
