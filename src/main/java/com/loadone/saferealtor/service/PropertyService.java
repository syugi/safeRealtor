package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public Property registerProperty(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Property updateProperty(Long id, Property updatedProperty) {
        return propertyRepository.findById(id)
                .map(existingProperty -> {
                    existingProperty.setPropertyNumber(updatedProperty.getPropertyNumber());
                    existingProperty.setPrice(updatedProperty.getPrice());
                    existingProperty.setDescription(updatedProperty.getDescription());
                    existingProperty.setType(updatedProperty.getType());
                    existingProperty.setMaintenanceFee(updatedProperty.getMaintenanceFee());
                    existingProperty.setParkingAvailable(updatedProperty.getParkingAvailable());
                    existingProperty.setRoomType(updatedProperty.getRoomType());
                    existingProperty.setFloor(updatedProperty.getFloor());
                    existingProperty.setArea(updatedProperty.getArea());
                    existingProperty.setRooms(updatedProperty.getRooms());
                    existingProperty.setBathrooms(updatedProperty.getBathrooms());
                    existingProperty.setDirection(updatedProperty.getDirection());
                    existingProperty.setHeatingType(updatedProperty.getHeatingType());
                    existingProperty.setElevatorAvailable(updatedProperty.getElevatorAvailable());
                    existingProperty.setTotalParkingSlots(updatedProperty.getTotalParkingSlots());
                    existingProperty.setEntranceType(updatedProperty.getEntranceType());
                    existingProperty.setAvailableMoveInDate(updatedProperty.getAvailableMoveInDate());
                    existingProperty.setBuildingUse(updatedProperty.getBuildingUse());
                    existingProperty.setApprovalDate(updatedProperty.getApprovalDate());
                    existingProperty.setFirstRegistrationDate(updatedProperty.getFirstRegistrationDate());
                    existingProperty.setOptions(updatedProperty.getOptions());
                    existingProperty.setSecurityFacilities(updatedProperty.getSecurityFacilities());
                    existingProperty.setAddress(updatedProperty.getAddress());
                    return propertyRepository.save(existingProperty);
                })
                .orElseThrow(() -> new BaseException(ErrorCode.PROPERTY_NOT_FOUND));
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
}
