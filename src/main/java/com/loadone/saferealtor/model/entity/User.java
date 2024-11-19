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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 사용
    private Long userSeq;

    @Column(nullable = false, unique = true, length = 50) // 아이디는 유니크하고 필수
    private String userId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column
    private String name;

    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; //구분 (ROLE_ADMIN: 관리자, ROLE_AGENT: 중개사, ROLE_USER: 사용자)

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // 활성 상태 (기본값 true)

    @Column
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private Set<Inquiry> inquiries;

    @CreatedDate
    private LocalDateTime createdAt;  // 회원 가입일

    @LastModifiedDate
    private LocalDateTime updatedAt;  // 최근 정보 수정일

    public String getRoleName(){
        return this.role.name();
    }
    public String getRoleDisplayName() { return this.role.getDisplayName();}
}
