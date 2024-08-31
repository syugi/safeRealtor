package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.model.entity.VerificationCode;
import com.loadone.saferealtor.repository.UserRepository;
import com.loadone.saferealtor.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
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
        user.setRole(User.ROLE_USER);
        userRepository.save(user);

        return true;
    }

    public boolean login(String username, String password){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return passwordEncoder.matches(password, user.get().getPassword());
        }
        return false;
    }
}
