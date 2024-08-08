package com.loadone.safeRealtor.repository;

import com.loadone.safeRealtor.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
