package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.dto.PagingResultDTO;
import com.loadone.saferealtor.model.dto.PropertyDTO;
import com.loadone.saferealtor.model.entity.Favorite;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public void addFavorite(String userId, Long propertySeq) {
        if(favoriteRepository.findByUserIdAndPropertySeq(userId, propertySeq).isEmpty()){
            favoriteRepository.save(new Favorite(userId, propertySeq));
        }
    }

    public void removeFavorite(String userId, Long propertyId) {
        favoriteRepository.findByUserIdAndPropertySeq(userId, propertyId)
                .ifPresent(favoriteRepository::delete);
    }

    public PagingResultDTO<PropertyDTO> getFavoriteProperties(String userId, Pageable pageable) {

        Page<Favorite> favoritePage = favoriteRepository.findByUserId(userId, pageable);

        // Favorite에서 Property 추출
        List<PropertyDTO> propertyResponseList = favoritePage.stream()
                .map(favorite -> {
                    Property property = favorite.getProperty();
                    return new PropertyDTO(property, true);
                })
                .toList();

        return new PagingResultDTO<>(propertyResponseList,
                favoritePage.getTotalPages(),
                favoritePage.getNumber(),
                favoritePage.getTotalElements()
                );
    }

    public List<Favorite> getFavoritesByUserId(String userId) {
        return favoriteRepository.findByUserId(userId);
    }
}
