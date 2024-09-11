package com.loadone.saferealtor.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String propertyNumber; // 매물 번호 (고유 식별자)

    @Column(nullable = false)
    private String price;  // 매물의 가격

    @Column(nullable = false)
    private String description;  // 매물에 대한 간단한 설명

    @Enumerated(EnumType.STRING)
    private PropertyType type;  // 매물의 종류 (전세, 월세, 매매)

    private String maintenanceFee;  // 관리비

    private Boolean parkingAvailable;  // 주차 가능 여부

    private String roomType;  // 방 종류 (예: 아파트, 원룸, 빌라)

    private String floor;  // 해당층/건물층 (예: "5/15")

    private BigDecimal area;  // 전용면적 (단위: 평방미터)

    private Integer rooms;  // 방 개수

    private Integer bathrooms;  // 욕실 개수

    private String direction;  // 방의 방향 (예: 남향, 북향)

    private String heatingType;  // 난방 종류 (예: 개별난방, 중앙난방)

    private Boolean elevatorAvailable;  // 엘리베이터 여부

    private Integer totalParkingSlots;  // 총 주차 대수

    private String entranceType;  // 현관 유형 (예: 복도식, 계단식)

    private String availableMoveInDate;  // 입주 가능일

    private String buildingUse;  // 건축물의 용도

    private LocalDate approvalDate;  // 사용 승인일

    private LocalDate firstRegistrationDate;  // 최초 등록일

    private String options;  // 매물에 포함된 옵션 (예: 에어컨, 냉장고)

    private String securityFacilities;  // 보안/안전 시설 (예: CCTV, 경비실)

    private String address;

    @ElementCollection // 값 타입 컬렉션 선언
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id")) // 매핑될 테이블 설정
    @Column(name = "image_url") // 이미지 URL 컬럼
    private List<String> imageUrls;  // 이미지 URL 리스트

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @OneToMany(mappedBy = "property")
    private Set<Favorite> favorites;

    @OneToMany(mappedBy = "property")
    private Set<Inquiry> inquiries;
}
