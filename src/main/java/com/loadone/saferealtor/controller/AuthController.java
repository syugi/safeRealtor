package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.LoginReqDTO;
import com.loadone.saferealtor.model.dto.PhoneNumberReqDTO;
import com.loadone.saferealtor.model.dto.RegisterUserReqDTO;
import com.loadone.saferealtor.model.dto.VerificationReqDTO;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        boolean isAvailable = authService.isUserIdAvailable(userId);
        if(isAvailable){
            return ResponseEntity.ok().body("사용 가능한 아이디 입니다.");
        } else {
            throw new BaseException(ErrorCode.INVALID_USER_ID);
        }
    }

    /* 회원가입 */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserReqDTO request) {
        try {
            User user = authService.register(request, User.ROLE_USER);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.REGISTRATION_FAILED, e);
        }
    }

    /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginReqDTO request) {

        try {
            boolean authenticated = authService.login(request.getUserId(), request.getPassword());
            if (authenticated) {
                return ResponseEntity.ok("로그인 성공");
            } else {
                throw new BaseException(ErrorCode.UNAUTHORIZED, "아이디 혹은 비밀번호를 다시 확인해 주세요.");
            }
        } catch (BaseException be) {
            throw new BaseException(be.getErrorCode(), "아이디 혹은 비밀번호를 다시 확인해 주세요.");
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_TO_LOGIN);
        }
    }
}
