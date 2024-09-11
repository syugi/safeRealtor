package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.PropertyRequestDTO;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.repository.PropertyRepository;
import com.loadone.saferealtor.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    private final FileUtil fileUtil;

    public Property registerProperty(PropertyRequestDTO propertyRequest, List<MultipartFile> images) throws IOException {
        String newPropertyNumber = generateNextPropertyNumber();

        // DTO에서 Property 객체로 변환
        Property property = new Property();
        property.setPropertyNumber(newPropertyNumber);
        property.setPrice(propertyRequest.getPrice());
        property.setDescription(propertyRequest.getDescription());
        property.setType(propertyRequest.getType());
        property.setMaintenanceFee(propertyRequest.getMaintenanceFee());
        property.setParkingAvailable(propertyRequest.getParkingAvailable());
        property.setRoomType(propertyRequest.getRoomType());
        property.setFloor(propertyRequest.getFloor());
        property.setArea(propertyRequest.getArea());
        property.setRooms(propertyRequest.getRooms());
        property.setBathrooms(propertyRequest.getBathrooms());
        property.setDirection(propertyRequest.getDirection());
        property.setHeatingType(propertyRequest.getHeatingType());
        property.setElevatorAvailable(propertyRequest.getElevatorAvailable());
        property.setTotalParkingSlots(propertyRequest.getTotalParkingSlots());
        property.setEntranceType(propertyRequest.getEntranceType());
        property.setAvailableMoveInDate(propertyRequest.getAvailableMoveInDate());
        property.setBuildingUse(propertyRequest.getBuildingUse());
        property.setApprovalDate(propertyRequest.getApprovalDate());
        property.setFirstRegistrationDate(propertyRequest.getFirstRegistrationDate());
        property.setOptions(propertyRequest.getOptions());
        property.setSecurityFacilities(propertyRequest.getSecurityFacilities());
        property.setAddress(propertyRequest.getAddress());

        // 이미지 처리 (이미지 경로 저장)
        if (images != null && !images.isEmpty()) {
            List<String> imagePaths = fileUtil.uploadImages(images);
            property.setImageUrls(imagePaths); // 이미지 URL 리스트 설정
        }

        // 매물 정보 저장
        return propertyRepository.save(property);
    }

    // 매물 번호 생성 로직
    private String generateNextPropertyNumber() {
        Property lastProperty = propertyRepository.findTopByOrderByPropertyNumberDesc();
        String lastPropertyNumber = lastProperty != null ? lastProperty.getPropertyNumber() : null;

        if(lastPropertyNumber == null) {
            return "00001";
        }

        int lastNumber = Integer.parseInt(lastPropertyNumber);

        int nextNumber = lastNumber + 1;

        if(nextNumber > 99999){
            return String.valueOf(nextNumber);
        }

        return String.format("%05d", nextNumber);
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
