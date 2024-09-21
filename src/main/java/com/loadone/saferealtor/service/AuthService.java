package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.LoginResDTO;
import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.model.entity.VerificationCode;
import com.loadone.saferealtor.repository.UserRepository;
import com.loadone.saferealtor.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final VerificationCodeRepository verificationCodeRepository;
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean isPhoneNumberRegistered(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    // 인증번호 발송
    public boolean sendVerificationCode(String phoneNumber) {
        // 새로운 인증 코드 생성 및 저장
        String code = generateVerificationCode();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhoneNumber(phoneNumber);
        verificationCode.setCode(code);
        verificationCode.setRequestedAt(LocalDateTime.now());
        verificationCodeRepository.save(verificationCode);

        // SMS 전송
        String smsMessage = "[안부] 인증번호는 [" + code + "]입니다.";
        if (smsService.sendSms(phoneNumber, smsMessage)) {
            logger.info("Verification code sent to {}: {}", phoneNumber, code);
            return true;
        } else {
            logger.error("Failed to send verification code to {}", phoneNumber);
            throw new BaseException(ErrorCode.FAILED_TO_SEND_VERIFICATION_CODE);
        }
    }

    // 인증번호 생성
    private String generateVerificationCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    // 인증번호 확인
    public boolean verifyCode(String phoneNumber, String code) {
        final int MAX_MINUTES = 3;
        VerificationCode verificationCode = verificationCodeRepository.findTopByPhoneNumberOrderByRequestedAtDesc(phoneNumber).orElseThrow(() -> new BaseException(ErrorCode.INVALID_VERIFICATION_CODE, "인증번호가 존재하지 않습니다."));

        if (!verificationCode.getCode().equals(code)) {
            throw new BaseException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        if (LocalDateTime.now().isAfter(verificationCode.getRequestedAt().plusMinutes(MAX_MINUTES))) {
            throw new BaseException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        return true;
    }

    // 사용자 아이디 유효성 체크
    public void validateUserId(String userId) {

        // 아이디는 5~20자의 영문자와 숫자로 구성, 특수문자 제외
        String userIdPattern = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{5,20}$";
        if (!Pattern.matches(userIdPattern, userId)) {
            throw new BaseException(ErrorCode.INVALID_USER_ID_FORMAT);
        }

        if(userRepository.existsByUserId(userId)){
            throw new BaseException(ErrorCode.DUPLICATED_USER_ID);
        }
    }

    // 비밀번호 검증 메서드
    public void validatePassword(String password) {
//        // 비밀번호는 최소 8~20자, 숫자, 대문자, 소문자, 특수문자 포함
//        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
        String passwordPattern = "^[A-Za-z\\d@$!%*?&]{4,20}$";
        if (!Pattern.matches(passwordPattern, password)) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }

    // 사용자 회원가입
    public User register(RegisterUserReqDTO request, int userRole) {

        try {
            validateUserId(request.getUserId());
            validatePassword(request.getPassword());
        } catch (BaseException e) {
            throw e;
        }

        User user = new User();
        user.setUserId(request.getUserId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(userRole);
        return userRepository.save(user);
    }

    public LoginResDTO login(String userId, String password){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }

        return new LoginResDTO(user.getId(), user.getUserId(), user.getRole(), user.getPhoneNumber());
    }
}
