package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.InquiryReqDTO;
import com.loadone.saferealtor.model.entity.Inquiry;
import com.loadone.saferealtor.model.entity.Property;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.InquiryRepository;
import com.loadone.saferealtor.repository.PropertyRepository;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    // 문의 저장
    public Inquiry saveInquiry(InquiryReqDTO request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Inquiry inquiry = new Inquiry();
        inquiry.setUser(user);
        inquiry.setInquiryContent(request.getInquiryContent());
        inquiry.setDetailRequest(request.getDetailRequest());
        inquiry.setInquiryDateTime(LocalDateTime.now());

        // 매물이 선택된 경우 처리
        if (request.getPropertyNumbers() != null && !request.getPropertyNumbers().isEmpty()) {
            List<Property> selectedProperties = propertyRepository.findByPropertyNumberIn(request.getPropertyNumbers());
            inquiry.setProperties(selectedProperties);
        }

        return inquiryRepository.save(inquiry);
    }
}
