package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.LoginReqDTO;
import com.loadone.saferealtor.model.dto.PhoneNumberReqDTO;
import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.dto.VerificationReqDTO;
import com.loadone.saferealtor.model.entity.VerificationCode;
import com.loadone.saferealtor.repository.VerificationCodeRepository;
import com.loadone.saferealtor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final VerificationCodeRepository verificationCodeRepository;

    /* 인증번호 발송 */
    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody PhoneNumberReqDTO request) {
        authService.sendVerificationCode(request.getPhoneNumber());
        return ResponseEntity.ok().body("발송되었습니다.");
    }

    /* 인증번호 확인 */
    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationReqDTO request) {
        final int MAX_MINUTES = 3;
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findTopByPhoneNumberOrderByRequestedAtDesc(request.getPhoneNumber());

        if (verificationCode.isEmpty() || !verificationCode.get().getCode().equals(request.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 일치하지 않습니다.");
        }

        if (ChronoUnit.MINUTES.between(verificationCode.get().getRequestedAt(), LocalDateTime.now()) >= MAX_MINUTES) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 만료되었습니다.");
        }

        return ResponseEntity.ok("인증번호가 확인 되었습니다.");
    }

    /* 사용자명 중복 확인 */
    @GetMapping("/checkUsername")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean isAvailable = authService.isUsernameAvailable(username);
        if(isAvailable){
            return ResponseEntity.ok().body("사용 가능한 사용자명입니다.");
        } else {
            return ResponseEntity.badRequest().body("이미 사용중인 사용자명입니다.");
        }
    }

    /* 회원가입 */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserReqDTO request) {
        if (authService.register(request)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDTO request) {
        boolean authenticated = authService.login(request.getUsername(), request.getPassword());
        if (authenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}
