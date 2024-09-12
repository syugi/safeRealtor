package com.loadone.saferealtor.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "property_id", nullable = false)
    private Long propertyId;

    public Favorite(String userId, Long propertyId) {
        this.userId = userId;
        this.propertyId = propertyId;
    }
}
