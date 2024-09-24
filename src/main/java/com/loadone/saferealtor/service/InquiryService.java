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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class InquiryService {

    @Value("${agent.phone-number}")
    private String agentPhoneNumber;

    private final InquiryRepository inquiryRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final SmsService smsService;


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

        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        // 문자 발송 (문자 발송 실패해도 저장에는 영향 X)
        try {
            if (agentPhoneNumber == null || agentPhoneNumber.isEmpty()) {
                throw new BaseException(ErrorCode.MISSING_AGENT_PHONE_NUMBER);
            }

            String message = buildSmsMessage(savedInquiry); // 문자 내용 생성
            smsService.sendSms(agentPhoneNumber, message); // 문자 발송
        } catch (Exception e) {
            // 문자 발송 실패 시 로그 출력, 예외 처리 (문의는 이미 저장됨)
            log.error("Failed to send SMS for inquiry {}: {}", savedInquiry.getId(), e.getMessage());
        }

        return savedInquiry;
    }

    // SMS 메시지 생성 메서드
    private String buildSmsMessage(Inquiry inquiry) {
        String customerPhoneNumber = inquiry.getUser().getPhoneNumber();
        String inquiryContent = inquiry.getInquiryContent();
        String detailRequest = inquiry.getDetailRequest() != null ? inquiry.getDetailRequest() : "상세 요청 없음";

        // 매물 번호 리스트 생성 (선택적으로 포함)
        String propertyNumbers = inquiry.getProperties() != null && !inquiry.getProperties().isEmpty()
                ? inquiry.getProperties().stream()
                .map(Property::getPropertyNumber)
                .collect(Collectors.joining(", "))
                : "매물 선택 없음";

        return String.format("[안부] 새로운 문의가 도착했습니다.\n\n#고객 핸드폰 번호: %s \n\n#매물 번호: %s\n\n#문의 내용: %s\n\n#상세 요청사항: %s",
                customerPhoneNumber, propertyNumbers,inquiryContent, detailRequest );
    }
}
