package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.FavoriteReqDTO;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<Void> addFavorite(@RequestBody FavoriteReqDTO request) {
        favoriteService.addFavorite(request.getUserId(), request.getPropertyId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteReqDTO request) {
        favoriteService.removeFavorite(request.getUserId(), request.getPropertyId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Property>> getFavoriteProperties(@PathVariable String userId) {
        List<Property> properties = favoriteService.getFavoriteProperties(userId);
        return ResponseEntity.ok(properties);
    }
}