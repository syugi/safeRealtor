package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.model.entity.VerificationCode;
import com.loadone.saferealtor.repository.UserRepository;
import com.loadone.saferealtor.repository.VerificationCodeRepository;
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
        verificationCodeRepository.deleteByPhoneNumber(phoneNumber);

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

    // 사용자 회원가입
    public boolean register(RegisterUserReqDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return false;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        userRepository.save(user);

        return true;
    }

    public boolean login(String username, String password){
        User user = userRepository.findByUsername(username);
        if(user != null){
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
