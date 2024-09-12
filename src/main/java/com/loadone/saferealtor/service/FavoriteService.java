package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.Favorite;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.repository.FavoriteRepository;
import com.loadone.saferealtor.repository.PropertyRepository;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Property> getFavoriteProperties(String userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return propertyRepository.findAllById(favorites.stream()
                .map(Favorite::getPropertyId)
                .toList());
    }

    public List<Favorite> getFavoritesByUserId(String userId) {
        return favoriteRepository.findByUserId(userId);
    }
}
