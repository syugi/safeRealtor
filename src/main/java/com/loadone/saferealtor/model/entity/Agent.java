package com.loadone.saferealtor.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agentSeq;

    private String licenseNumber;

    @Column(name = "agency_name", length = 100)
    private String agencyName; // 소속 사무소 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING; // 승인 상태

    @OneToOne
    @JoinColumn(name = "user_seq")
    @JsonBackReference // 직렬화 시 역방향 참조를 방지
    private User user;

    @OneToMany(mappedBy = "agent")
    private Set<Property> properties;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ApprovalStatus {
        PENDING,  // 승인 대기 중
        APPROVED, // 승인됨
        REJECTED  // 승인 거부됨
    }
}
