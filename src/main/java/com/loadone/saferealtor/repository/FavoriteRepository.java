package com.loadone.saferealtor.repository;

import com.loadone.saferealtor.model.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(String userId);
    Optional<Favorite> findByUserIdAndPropertyId(String userId, Long propertyId);
}
