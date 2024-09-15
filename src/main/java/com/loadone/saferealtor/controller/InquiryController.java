package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.InquiryReqDTO;
import com.loadone.saferealtor.model.entity.Inquiry;
import com.loadone.saferealtor.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping("/submit")
    public ResponseEntity<Inquiry> submitInquiry(@RequestBody InquiryReqDTO request) {
        Inquiry inquiry = inquiryService.saveInquiry(request);
        return ResponseEntity.ok(inquiry);
    }
}
