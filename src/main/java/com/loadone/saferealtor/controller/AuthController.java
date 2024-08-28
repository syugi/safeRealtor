package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.PhoneNumberReqDTO;
import com.loadone.saferealtor.model.dto.VerificationReqDTO;
import com.loadone.saferealtor.service.AuthService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> sendVerificationCode(@RequestBody PhoneNumberReqDTO request) {
        authService.sendVerificationCode(request.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

    /* 인증번호 확인 */
    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationReqDTO request) {
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
}
