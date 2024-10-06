package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.PageReqDTO;
import com.loadone.saferealtor.model.dto.PropertyDTO;
import com.loadone.saferealtor.model.dto.PropertyResDTO;
import com.loadone.saferealtor.model.entity.Favorite;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.service.FavoriteService;
import com.loadone.saferealtor.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
//    @PreAuthorize("hasRole('ROLE_AGENT')")
    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PropertyResDTO> registerProperty(@RequestPart("property") PropertyDTO propertyDTO,
                                                           @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            PropertyResDTO propertyResDTO = propertyService.registerProperty(propertyDTO, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(propertyResDTO);
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

    // 매물 목록 조회 (중개사용)
    @PreAuthorize("hasRole('ROLE_AGENT')")
    @GetMapping("/realtor-list")
    public ResponseEntity<List<PropertyDTO>> getPropertiesForRealtor(@RequestParam int page, @RequestParam int perPage) {
        // 중개사는 userId 필요 없음
        PageReqDTO pageReqDTO = PageReqDTO.builder().page(page).perPage(perPage).build();
        Pageable pageable = pageReqDTO.getPageable();

        Page<Property> propertyPage = propertyService.getProperties(pageable);
        List<PropertyDTO> propertyResponseList = propertyPage.stream()
                .map(PropertyDTO::new) // 찜 여부 없이 매물 정보만 리턴
                .toList();
        return ResponseEntity.ok(propertyResponseList);
    }

    // 매물 목록 조회
    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getProperties(@RequestParam(required = false) String userId,
                                                           @RequestParam int page,
                                                           @RequestParam int perPage) {
        PageReqDTO pageReqDTO = PageReqDTO.builder().page(page).perPage(perPage).build();
        Pageable pageable = pageReqDTO.getPageable();

        // 매물 목록 가져오기
        Page<Property> propertyPage = propertyService.getProperties(pageable);

        List<PropertyDTO> propertyResDTOS;

        // userId가 비어있을 경우 favorites 체크 없이 목록 반환
        if (userId == null || userId.isEmpty()) {
            propertyResDTOS = propertyPage.stream()
                    .map(property -> new PropertyDTO(property, false)) // false로 기본 설정
                    .toList();
        } else {
            // userId가 있는 경우 favorites 체크 후 반환
            List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);

            propertyResDTOS = propertyPage.stream()
                    .map(property -> {
                        boolean isFavorite = favorites.stream()
                                .anyMatch(favorite -> favorite.getPropertyId().equals(property.getId()));
                        return new PropertyDTO(property, isFavorite);
                    })
                    .toList();
        }

        return ResponseEntity.ok().body(propertyResDTOS);
    }

    // 매물 수정
    @PreAuthorize("hasRole('ROLE_AGENT')")
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
    @PreAuthorize("hasRole('ROLE_AGENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok().body("삭제되었습니다.");
    }
}
