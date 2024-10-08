package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.PropertyDTO;
import com.loadone.saferealtor.model.dto.PropertyResDTO;
import com.loadone.saferealtor.model.entity.Agent;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.AgentRepository;
import com.loadone.saferealtor.repository.PropertyRepository;
import com.loadone.saferealtor.repository.UserRepository;
import com.loadone.saferealtor.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;

    private final FileUtil fileUtil;

    public PropertyResDTO registerProperty(PropertyDTO propertyDTO, List<MultipartFile> images) throws IOException {
        boolean imageUploadSuccess = true;
        String message = "매물 등록에 성공했습니다.";

        String newPropertyNumber = generateNextPropertyNumber();

        String userId = propertyDTO.getRegisteredUserId();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Agent agent = agentRepository.findByUserId(user.getId())  // User로 Agent 조회
                .orElseThrow(() -> new BaseException(ErrorCode.AGENT_NOT_FOUND, "해당 유저는 중개사가 아닙니다."));

        // DTO에서 Property 객체로 변환
        Property property = propertyDTO.toEntity();
        property.setAgent(agent);
        property.setPropertyNumber(newPropertyNumber);

        // 이미지 처리 (S3에 이미지 업로드 후 URL 저장)
        if (images != null && !images.isEmpty()) {
            try {
                List<String> imagePaths = fileUtil.uploadImages(images);
                property.setImageUrls(imagePaths); // 이미지 URL 리스트 설정
            } catch (Exception e){
                log.error("Failed to upload images: {}", e.getMessage(), e);
                imageUploadSuccess = false;
                if (e.getCause() instanceof BaseException) {
                    message = e.getCause().getMessage();
                } else {
                    message = ErrorCode.FILE_UPLOAD_ERROR.getMessage();
                }
            }
        }

        // 매물 정보 저장
        propertyRepository.save(property);

        PropertyDTO resPropertyDTO = new PropertyDTO(property);

        return new PropertyResDTO(resPropertyDTO, imageUploadSuccess, message);
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

    public Page<Property> getProperties(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow(() -> new BaseException(ErrorCode.PROPERTY_NOT_FOUND));
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
