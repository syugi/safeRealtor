package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.PropertyRequestDTO;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    // 매물 등록
    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Property> registerProperty(@RequestPart("property") PropertyRequestDTO propertyRequest,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Property property = propertyService.registerProperty(propertyRequest, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(property);
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_TO_REGISTER_PROPERTY,e);
        }
    }

    // 매물 조회
    @GetMapping("/{id}")
    public ResponseEntity<Property> getProperty(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        return ResponseEntity.ok().body(property);
    }

    // 매물 목록 조회
    @GetMapping
    public ResponseEntity<Iterable<Property>> getAllProperties() {
        Iterable<Property> properties = propertyService.getAllProperties();
        return ResponseEntity.ok().body(properties);
    }

    // 매물 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property updatedProperty) {
        try {
            Property property = propertyService.updateProperty(id, updatedProperty);
            return ResponseEntity.ok().body(property);
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_TO_UPDATE_PROPERTY, e);
        }
    }

    // 매물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok().body("삭제되었습니다.");
    }
}
