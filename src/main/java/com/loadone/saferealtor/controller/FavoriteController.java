package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.FavoriteReqDTO;
import com.loadone.saferealtor.model.dto.PropertyResDTO;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 찜하기
    @PostMapping("/add")
    public ResponseEntity<Void> addFavorite(@RequestBody FavoriteReqDTO request) {
        favoriteService.addFavorite(request.getUserId(), request.getPropertyId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 찜 취소
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteReqDTO request) {
        favoriteService.removeFavorite(request.getUserId(), request.getPropertyId());
        return ResponseEntity.ok().build();
    }

    // 찜한 매물 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<PropertyResDTO>> getFavoriteProperties(@PathVariable String userId, @RequestParam int page, @RequestParam int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<Property> propertyPage = favoriteService.getFavoriteProperties(userId, pageable);
        List<PropertyResDTO> propertyResDTOS = propertyPage.stream().map(
                property -> {
                    PropertyResDTO dto = new PropertyResDTO(property);
                    dto.setIsFavorite(true);
                    return dto;
                }).toList();
        return ResponseEntity.ok(propertyResDTOS);
    }
}