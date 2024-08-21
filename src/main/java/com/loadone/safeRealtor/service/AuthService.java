package com.loadone.safeRealtor.service;

import com.loadone.safeRealtor.model.entity.VerificationCode;
import com.loadone.safeRealtor.repository.UserRepository;
import com.loadone.safeRealtor.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 인증번호 발송
    public void sendVerificationCode(String phoneNumber) {
        String code = generateVerificationCode();
        smsService.sendSms(phoneNumber, "Your verification code is " + code);

        VerificationCode verificationCode = new VerificationCode(phoneNumber, code);
        verificationCodeRepository.save(verificationCode);
    }

    // 인증번호 생성
    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(999999));
    }

    // 인증번호 확인
    public boolean verifyCode(String phoneNumber, String verificationCode) {
        Optional<VerificationCode> storedCode = verificationCodeRepository.findByPhoneNumber(phoneNumber);
        return storedCode.isPresent() && storedCode.get().getCode().equals(verificationCode);
    }

    // 사용자명 중복 확인
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}
