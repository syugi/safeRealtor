package com.loadone.saferealtor.repository;

import com.loadone.saferealtor.model.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
