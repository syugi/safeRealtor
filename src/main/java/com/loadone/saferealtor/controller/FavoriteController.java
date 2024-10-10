package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.FavoriteReqDTO;
import com.loadone.saferealtor.model.dto.PageReqDTO;
import com.loadone.saferealtor.model.dto.PagingResultDTO;
import com.loadone.saferealtor.model.dto.PropertyDTO;
import com.loadone.saferealtor.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PagingResultDTO<PropertyDTO>> getFavoriteProperties(@PathVariable String userId, @RequestParam int page, @RequestParam int perPage) {

        PageReqDTO pageReqDTO = PageReqDTO.builder().page(page).perPage(perPage).build();
        Pageable pageable = pageReqDTO.getPageable();

        PagingResultDTO<PropertyDTO> response = favoriteService.getFavoriteProperties(userId, pageable);

        return ResponseEntity.ok(response);
    }
}