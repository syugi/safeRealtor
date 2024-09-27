package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.model.dto.PropertyDTO;
import com.loadone.saferealtor.model.entity.PropertyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
public class PropertyServiceTest {

    @Autowired
    private PropertyService propertyService;

    @Test
    public void TestRegisterProperty(){

        for(int i=0; i<20; i++) {
            List<MultipartFile> images = new ArrayList<>();

            PropertyDTO propertyDTO = new PropertyDTO();
            propertyDTO.setAgentId(Long.valueOf(1));
            propertyDTO.setPrice("1억 "+i+"천만원");
            propertyDTO.setTitle(i+"번째 매물");
            propertyDTO.setDescription(i+"번째 매물입니다.");
            propertyDTO.setType(PropertyType.월세);
            propertyDTO.setMaintenanceFee("10만원");
            propertyDTO.setParkingAvailable(true);
            propertyDTO.setRoomType("오피스텔");
            propertyDTO.setFloor("1층/10층");
            propertyDTO.setArea(new BigDecimal(30));
            propertyDTO.setRooms(1);
            propertyDTO.setBathrooms(1);
            propertyDTO.setDirection("남향");
            propertyDTO.setHeatingType("개별난방");
            propertyDTO.setElevatorAvailable(true);
            propertyDTO.setTotalParkingSlots(10);
            propertyDTO.setEntranceType("계단식");
            propertyDTO.setAvailableMoveInDate(LocalDate.now().toString());
            propertyDTO.setBuildingUse("주거");
            propertyDTO.setApprovalDate(LocalDate.now());
            propertyDTO.setFirstRegistrationDate(LocalDate.now());
            propertyDTO.setOptions("에어컨,냉장고,세탁기");
            propertyDTO.setSecurityFacilities("CCTV,경비원");
            propertyDTO.setAddress("서울시 강남구 역삼동 123-4");

            try {
                propertyService.registerProperty(propertyDTO, images);
            } catch (BaseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}