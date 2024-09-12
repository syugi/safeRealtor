package com.loadone.saferealtor.model.dto;

import com.loadone.saferealtor.model.entity.PropertyType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PropertyReqDTO {
    private String propertyNumber;
    private String price;
    private String description;
    private PropertyType type;
    private String maintenanceFee;
    private Boolean parkingAvailable;
    private String roomType;
    private String floor;
    private BigDecimal area;
    private Integer rooms;
    private Integer bathrooms;
    private String direction;
    private String heatingType;
    private Boolean elevatorAvailable;
    private Integer totalParkingSlots;
    private String entranceType;
    private String availableMoveInDate;
    private String buildingUse;
    private LocalDate approvalDate;
    private LocalDate firstRegistrationDate;
    private String options;
    private String securityFacilities;
    private String address;
}
