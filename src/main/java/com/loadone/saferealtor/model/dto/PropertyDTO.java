package com.loadone.saferealtor.model.dto;

import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.model.entity.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO {
    private Long id;  // 매물 ID
    private String propertyNumber;  // 매물 번호
    private String price;  // 매물의 가격
    private String title; // 매물 제목
    private String description;  // 매물 설명
    private PropertyType type;  // 매물 종류 (전세, 월세, 매매)
    private String maintenanceFee;  // 관리비
    private Boolean parkingAvailable;  // 주차 가능 여부
    private String roomType;  // 방 종류
    private String floor;  // 해당층/건물층
    private BigDecimal area;  // 전용면적
    private Integer rooms;  // 방 개수
    private Integer bathrooms;  // 욕실 개수
    private String direction;  // 방의 방향
    private String heatingType;  // 난방 종류
    private Boolean elevatorAvailable;  // 엘리베이터 여부
    private Integer totalParkingSlots;  // 총 주차 대수
    private String entranceType;  // 현관 유형
    private String availableMoveInDate;  // 입주 가능일
    private String buildingUse;  // 건축물 용도
    private LocalDate approvalDate;  // 사용 승인일
    private LocalDate firstRegistrationDate;  // 최초 등록일
    private String options;  // 매물에 포함된 옵션
    private String securityFacilities;  // 보안/안전 시설
    private String address;  // 주소
    private List<String> imageUrls;  // 이미지 URL 리스트
    private Boolean isFavorite;  // 찜 여부
    private LocalDateTime registeredAt;  // 등록일시


    private Long agentId;  // 중개인 ID

    public PropertyDTO(Property property) {
        this.id = property.getId();
        this.propertyNumber = property.getPropertyNumber();
        this.price = property.getPrice();
        this.title = property.getTitle();
        this.description = property.getDescription();
        this.type = property.getType();
        this.maintenanceFee = property.getMaintenanceFee();
        this.parkingAvailable = property.getParkingAvailable();
        this.roomType = property.getRoomType();
        this.floor = property.getFloor();
        this.area = property.getArea();
        this.rooms = property.getRooms();
        this.bathrooms = property.getBathrooms();
        this.direction = property.getDirection();
        this.heatingType = property.getHeatingType();
        this.elevatorAvailable = property.getElevatorAvailable();
        this.totalParkingSlots = property.getTotalParkingSlots();
        this.entranceType = property.getEntranceType();
        this.availableMoveInDate = property.getAvailableMoveInDate();
        this.buildingUse = property.getBuildingUse();
        this.approvalDate = property.getApprovalDate();
        this.firstRegistrationDate = property.getFirstRegistrationDate();
        this.options = property.getOptions();
        this.securityFacilities = property.getSecurityFacilities();
        this.address = property.getAddress();
        this.imageUrls = property.getImageUrls();
        this.isFavorite = false; // 찜 여부
        this.registeredAt = property.getRegisteredAt();
    }

    public PropertyDTO(Property property, boolean isFavorite) {
        this(property);
        this.isFavorite = isFavorite; // 찜 여부
    }

    public Property toEntity() {
        return Property.builder()
                .propertyNumber(propertyNumber)
                .price(price)
                .title(title)
                .description(description)
                .type(type)
                .maintenanceFee(maintenanceFee)
                .parkingAvailable(parkingAvailable)
                .roomType(roomType)
                .floor(floor)
                .area(area)
                .rooms(rooms)
                .bathrooms(bathrooms)
                .direction(direction)
                .heatingType(heatingType)
                .elevatorAvailable(elevatorAvailable)
                .totalParkingSlots(totalParkingSlots)
                .entranceType(entranceType)
                .availableMoveInDate(availableMoveInDate)
                .buildingUse(buildingUse)
                .approvalDate(approvalDate)
                .firstRegistrationDate(firstRegistrationDate)
                .options(options)
                .securityFacilities(securityFacilities)
                .address(address)
                .build();
    }
}
