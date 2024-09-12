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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

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

        //SMS 전송
        return smsService.sendSms(phoneNumber, "[안부] 인증번호는 ["+code+"]입니다.");
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

    // 인증번호 생성
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    // 사용자 아이디 유효성 체크
    public boolean isUserIdAvailable(String userId) {
        if(userRepository.existsByUserId(userId)){
            throw new BaseException(ErrorCode.DUPLICATED_USER_ID);
        }

        return true;
    }

    // 사용자 회원가입
    public User register(RegisterUserReqDTO request, int userRole) {
        if(!isUserIdAvailable(request.getUserId())){
            throw new BaseException(ErrorCode.INVALID_USER_ID);
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

        return new LoginResDTO(user.getId(), user.getUserId(), user.getRole());
    }
}
