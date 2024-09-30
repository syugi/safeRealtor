package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.*;
import com.loadone.saferealtor.model.entity.Role;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /* 인증번호 발송 */
    @PostMapping("/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestBody PhoneNumberReqDTO request) {
        String phoneNumber = request.getPhoneNumber();

        // 이미 가입된 번호인지 확인
        if (authService.isPhoneNumberRegistered(phoneNumber)) {
            throw new BaseException(ErrorCode.DUPLICATED_PHONE_NUMBER);
        }

        try {
            authService.sendVerificationCode(phoneNumber);
            return ResponseEntity.ok("인증번호가 발송 되었습니다.");
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_TO_SEND_VERIFICATION_CODE, e);
        }
    }

    /* 인증번호 확인 */
    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationReqDTO request) {
        authService.verifyCode(request.getPhoneNumber(), request.getCode());
        return ResponseEntity.ok("인증번호가 확인 되었습니다.");
    }

    /* 사용자명 중복 확인 */
    @GetMapping("/checkUserId")
    public ResponseEntity<String> checkUserId(@RequestParam String userId) {
        try {
            authService.validateUserId(userId);
            return ResponseEntity.ok().body("사용 가능한 아이디 입니다.");
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.INVALID_USER_ID, e);
        }
    }

    /* 회원가입 */
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResDTO> register(@RequestBody RegisterUserReqDTO request) {

        // 이미 가입된 번호인지 확인
        if (authService.isPhoneNumberRegistered(request.getPhoneNumber())) {
            throw new BaseException(ErrorCode.DUPLICATED_PHONE_NUMBER);
        }

        try {
            User user = authService.registerUser(request, Role.USER);

            RegisterUserResDTO registerUserResDTO = new RegisterUserResDTO(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(registerUserResDTO);
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.REGISTRATION_FAILED, e);
        }
    }

    /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO request) {
        try {
            LoginResDTO loginResDTO = authService.login(request.getUserId(), request.getPassword());
            return ResponseEntity.ok(loginResDTO);
        } catch (BaseException be) {
            log.error(request+ " "+ be.getMessage());
            throw new BaseException(ErrorCode.INVALID_ID_PASSWORD);
        } catch (Exception e) {
            log.error(request+ " "+ e.getMessage());
            throw new BaseException(ErrorCode.FAILED_TO_LOGIN);
        }
    }
}
