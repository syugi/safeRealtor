package com.loadone.saferealtor.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class InquiryReqDTO {
    private String userId;                // 문의한 사용자 ID
    private String inquiryContent;        // 문의 내용
    private String detailRequest;         // 상세 요청사항
    private List<String> propertyNumbers; // 선택한 매물 번호 목록 (없을 수 있음)
}
