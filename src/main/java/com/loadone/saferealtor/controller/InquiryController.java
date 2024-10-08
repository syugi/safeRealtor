package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.InquiryReqDTO;
import com.loadone.saferealtor.model.entity.Inquiry;
import com.loadone.saferealtor.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    // 문의 등록
    @PostMapping("/submit")
    public ResponseEntity<Inquiry> submitInquiry(@RequestBody InquiryReqDTO request) {
        Inquiry inquiry = inquiryService.saveInquiry(request);
        return ResponseEntity.ok(inquiry);
    }

    // 문의 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<Inquiry  >> getInquiryList() {
        List<Inquiry> inquiries = inquiryService.getInquiryList();
        return ResponseEntity.ok(inquiries);
    }

}
