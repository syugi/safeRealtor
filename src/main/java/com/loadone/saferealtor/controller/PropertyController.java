package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.PropertyDTO;
import com.loadone.saferealtor.model.entity.Favorite;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.service.FavoriteService;
import com.loadone.saferealtor.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final FavoriteService favoriteService;

    // 매물 등록
    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Property> registerProperty(@RequestPart("property") PropertyDTO propertyDTO,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Property property = propertyService.registerProperty(propertyDTO, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(property);
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_TO_REGISTER_PROPERTY,e);
        }
    }

    // 매물 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Property> getProperty(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        return ResponseEntity.ok().body(property);
    }

    @GetMapping("/realtor-list")
    public ResponseEntity<List<PropertyDTO>> getPropertiesForRealtor(@RequestParam int page, @RequestParam int perPage) {
        // 공인중개사는 userId 필요 없음
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("registeredAt").descending());
        Page<Property> propertyPage = propertyService.getProperties(pageable);
        List<PropertyDTO> propertyResponseList = propertyPage.stream()
                .map(PropertyDTO::new) // 찜 여부 없이 매물 정보만 리턴
                .toList();
        return ResponseEntity.ok(propertyResponseList);
    }

    // 매물 목록 조회
    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getProperties(@RequestParam String userId, @RequestParam int page, @RequestParam int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("registeredAt").descending());
        Page<Property> propertyPage = propertyService.getProperties(pageable);
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);

        List<PropertyDTO> propertyResDTOS = propertyPage.stream()
                .map(property -> {
                    boolean isFavorite = favorites.stream()
                            .anyMatch(favorite -> favorite.getPropertyId().equals(property.getId()));
                    return new PropertyDTO(property, isFavorite);
                })
                .toList();

        return ResponseEntity.ok().body(propertyResDTOS);
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
