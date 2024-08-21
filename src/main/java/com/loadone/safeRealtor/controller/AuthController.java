package com.loadone.safeRealtor.controller;

import com.loadone.safeRealtor.model.dto.PhoneNumberRequest;
import com.loadone.safeRealtor.model.dto.RegisterRequest;
import com.loadone.safeRealtor.model.dto.VerificationRequest;
import com.loadone.safeRealtor.model.entity.User;
import com.loadone.safeRealtor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /* 인증번호 발송 */
    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody PhoneNumberRequest request) {
        authService.sendVerificationCode(request.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

    /* 인증번호 확인 */
    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest request) {
        boolean isValid = authService.verifyCode(request.getPhoneNumber(), request.getVerificationCode());
        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /* 사용자명 중복 확인 */
    @GetMapping("/checkUsername")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean isAvailable = authService.isUsernameAvailable(username);
        return ResponseEntity.ok().body(Collections.singletonMap("available", isAvailable));
    }

    /* 회원가입 */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (authService.register(request)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
