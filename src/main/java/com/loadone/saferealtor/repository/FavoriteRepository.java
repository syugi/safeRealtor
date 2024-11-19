package com.loadone.saferealtor.repository;

import com.loadone.saferealtor.model.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(String userId);

    @EntityGraph(attributePaths = {"property.imageUrls"})
    Page<Favorite> findByUserId(String userId, Pageable pageable);

    Optional<Favorite> findByUserIdAndPropertySeq(String userId, Long propertySeq);
}
