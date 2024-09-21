package com.loadone.saferealtor.repository;

import com.loadone.saferealtor.model.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Property findTopByOrderByPropertyNumberDesc();

    // propertyNumber로 매물 조회
    List<Property> findByPropertyNumberIn(List<String> propertyNumbers);

    Page<Property> findAllByIdIn(List<Long> propertyIds, Pageable pageable);
}
