package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.Favorite;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.repository.FavoriteRepository;
import com.loadone.saferealtor.repository.PropertyRepository;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public void addFavorite(String userId, Long propertyId) {
        if(favoriteRepository.findByUserIdAndPropertyId(userId, propertyId).isEmpty()){
            favoriteRepository.save(new Favorite(userId, propertyId));
        }
    }

    public void removeFavorite(String userId, Long propertyId) {
        favoriteRepository.findByUserIdAndPropertyId(userId, propertyId)
                .ifPresent(favoriteRepository::delete);
    }

    public Page<Property> getFavoriteProperties(String userId, Pageable pageable) {
        // 페이징된 Favorite 목록 가져오기
        Page<Favorite> favoritePage = favoriteRepository.findByUserId(userId, pageable);

        // Favorite에서 Property ID 목록 추출
        List<Long> propertyIds = favoritePage.getContent().stream()
                .map(Favorite::getPropertyId)
                .toList();

        // 페이징된 Property 목록 가져오기
        return propertyRepository.findAllByIdIn(propertyIds, pageable);
    }

    public List<Favorite> getFavoritesByUserId(String userId) {
        return favoriteRepository.findByUserId(userId);
    }
}
