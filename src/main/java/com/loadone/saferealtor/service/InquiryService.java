package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.Inquiry;
import com.loadone.saferealtor.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Inquiry addInquiry(Inquiry inquiry) {
        return inquiryRepository.save(inquiry);
    }

    public List<Inquiry> getInquiriesByUserId(Long userId) {
        return inquiryRepository.findByUserId(userId);
    }

    public List<Inquiry> getInquiriesByPropertyId(Long propertyId) {
        return inquiryRepository.findByPropertyId(propertyId);
    }
}