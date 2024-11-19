package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.PageReqDTO;
import com.loadone.saferealtor.model.dto.PagingResultDTO;
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
    @PreAuthorize("hasRole('ROLE_AGENT')")
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
    public ResponseEntity<PropertyDTO> getProperty(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(new PropertyDTO(property));
    }

    // 매물 조회
    @GetMapping("/list")
    public ResponseEntity<PagingResultDTO<PropertyDTO>> getPropertiesForRealtor(@RequestParam(required = false) String userId,
                                                                               @RequestParam int page,
                                                                               @RequestParam int perPage) {

        PageReqDTO pageReqDTO = PageReqDTO.builder().page(page).perPage(perPage).build();
        Pageable pageable = pageReqDTO.getPageable();

        Page<Property> propertyPage = propertyService.getProperties(pageable);

        List<PropertyDTO> propertyResponseList = propertyPage.stream()
                .map(PropertyDTO::new)
                .toList();

        // userId가 있을 경우 즐겨찾기 여부 추가
        if(userId != null && !userId.isEmpty()){
            List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
            propertyResponseList.forEach(propertyDTO -> {
                boolean isFavorite = favorites.stream()
                        .anyMatch(favorite -> favorite.getPropertySeq().equals(propertyDTO.getId()));
                propertyDTO.setIsFavorite(isFavorite);
            });
        }

        PagingResultDTO<PropertyDTO> response = new PagingResultDTO<>(
                propertyResponseList,
                propertyPage.getTotalPages(),
                propertyPage.getNumber(),
                propertyPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    // 매물 수정
    @PreAuthorize("hasRole('ROLE_AGENT')")
    @PutMapping(value ="/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PropertyDTO> updateProperty(
            @PathVariable Long id,
            @RequestPart("property") PropertyDTO propertyDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "imagesToDelete", required = false) List<String> imagesToDelete) {
        try {
            PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyDTO, newImages, imagesToDelete);
            return ResponseEntity.ok().body(updatedProperty);
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
