package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.model.entity.VerificationCode;
import com.loadone.saferealtor.repository.UserRepository;
import com.loadone.saferealtor.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isPhoneNumberRegistered(String phoneNumber) {
        // 전화번호로 사용자를 조회하여 이미 등록된 사용자인지 확인
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    // 인증번호 발송
    public ResponseEntity<?> sendVerificationCode(String phoneNumber) {
        // 새로운 인증 코드 생성 및 저장
        String code = generateVerificationCode();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhoneNumber(phoneNumber);
        verificationCode.setCode(code);
        verificationCode.setRequestedAt(LocalDateTime.now());
        verificationCodeRepository.save(verificationCode);

        //SMS 전송
        smsService.sendSms(phoneNumber, "[안부] 인증번호는 ["+code+"]입니다.");

        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }

    // 인증번호 생성
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    // 사용자명 중복 확인
    public boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    // 사용자 회원가입
    public boolean register(RegisterUserReqDTO request, int userRole) {

        if (userRepository.existsByUserId(request.getUserId())) {
            throw new BaseException(ErrorCode.DUPLICATED_USER_ID, "이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUserId(request.getUserId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(userRole);
        userRepository.save(user);

        return true;
    }

    public boolean login(String userId, String password){
        Optional<User> user = userRepository.findByUserId(userId);
        if(!user.isPresent()) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND, "아이디 혹은 비밀번호를 다시 확인해 주세요.", HttpStatus.UNAUTHORIZED);
        }
        return passwordEncoder.matches(password, user.get().getPassword());
    }
}
