package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public Property addProperty(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow();
    }

    public Property updateProperty(Property property) {
        Property existingProperty = propertyRepository.findById(property.getId()).orElseThrow();
        existingProperty.setTitle(property.getTitle());
        existingProperty.setDescription(property.getDescription());
        existingProperty.setAddress(property.getAddress());
        existingProperty.setPrice(property.getPrice());
        return propertyRepository.save(existingProperty);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
}
