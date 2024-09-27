package com.loadone.saferealtor.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
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

    @OneToMany(mappedBy = "user")
    private Set<Inquiry> inquiries;

    @CreatedDate
    private LocalDateTime createdAt;  // 회원 가입일

    @LastModifiedDate
    private LocalDateTime updatedAt;  // 최근 정보 수정일
}
