package com.loadone.saferealtor.model.entity;

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

    public static final int ROLE_ADMIN = 0;
    public static final int ROLE_AGENT = 1;
    public static final int ROLE_USER = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private int role; //구분 (0:관리자, 1:공인중개사, 2:사용자)

    @OneToOne(mappedBy = "user")
    private Agent agent;

    @OneToMany(mappedBy = "user")
    private Set<Inquiry> inquiries;
}
