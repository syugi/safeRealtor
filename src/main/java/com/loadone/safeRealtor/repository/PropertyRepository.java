package com.loadone.safeRealtor.repository;

import com.loadone.safeRealtor.model.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
