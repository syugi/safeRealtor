package com.loadone.saferealtor.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;


@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class User {

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

    public String getRoleName() {
        return Role.getName(role);
    }
}
