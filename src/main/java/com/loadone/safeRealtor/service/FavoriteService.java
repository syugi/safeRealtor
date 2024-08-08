package com.loadone.safeRealtor.service;

import com.loadone.safeRealtor.model.Favorite;
import com.loadone.safeRealtor.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public void removeFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
