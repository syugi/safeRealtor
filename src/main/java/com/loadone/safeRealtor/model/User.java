package com.loadone.safeRealtor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role; //구분 (0:관리자, 1:공인중개사, 2:사용자)

    @OneToOne(mappedBy = "user")
    private Agent agent;

    @OneToMany(mappedBy = "user")
    private Set<Favorite> favorites;

    @OneToMany(mappedBy = "user")
    private Set<Inquiry> inquiries;
}
