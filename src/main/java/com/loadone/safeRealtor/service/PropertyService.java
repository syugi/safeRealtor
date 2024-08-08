package com.loadone.safeRealtor.service;

import com.loadone.safeRealtor.model.Property;
import com.loadone.safeRealtor.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

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
