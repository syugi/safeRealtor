package com.loadone.saferealtor.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String licenseNumber;

    private String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference // 직렬화 시 역방향 참조를 방지
    private User user;

    @OneToMany(mappedBy = "agent")
    private Set<Property> properties;
}
