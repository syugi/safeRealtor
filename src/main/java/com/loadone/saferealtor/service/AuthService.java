package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.*;
import com.loadone.saferealtor.model.entity.*;
import com.loadone.saferealtor.repository.UserRepository;
import com.loadone.saferealtor.repository.VerificationCodeRepository;
import com.loadone.saferealtor.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String KAKAO_PREFIX = "kakao_";

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

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
            log.info("Verification code sent to {}: {}", phoneNumber, code);
            return true;
        } else {
            log.error("Failed to send verification code to {}", phoneNumber);
            throw new BaseException(ErrorCode.FAILED_TO_SEND_VERIFICATION_CODE);
        }
    }

    // 인증번호 생성
    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    // 인증번호 확인
    public boolean verifyCode(String phoneNumber, String code) {
        final int MAX_MINUTES = 3;
        VerificationCode verificationCode = verificationCodeRepository
                .findTopByPhoneNumberOrderByRequestedAtDesc(phoneNumber)
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_VERIFICATION_CODE, "인증번호가 존재하지 않습니다."));

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

        // 아이디는 4~20자의 영문자와 숫자로 구성, 특수문자 제외
        String userIdPattern = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{4,20}$";
        if (!Pattern.matches(userIdPattern, userId)) {
            throw new BaseException(ErrorCode.INVALID_USER_ID_FORMAT);
        }

        if (userRepository.existsByUserId(userId)) {
            throw new BaseException(ErrorCode.DUPLICATED_USER_ID);
        }
    }

    // 비밀번호 검증 메서드
    public void validatePassword(String password) {
        // // 비밀번호는 최소 4~20자, 숫자, 대문자, 소문자, 특수문자 포함
        // String passwordPattern =
        // "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
        String passwordPattern = "^[A-Za-z\\d@$!%*?&]{4,20}$";
        if (!Pattern.matches(passwordPattern, password)) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }

    // 사용자 회원가입
    public User registerUser(RegisterUserReqDTO request, Role role) {

        try {
            validateUserId(request.getUserId());
            validatePassword(request.getPassword());
        } catch (BaseException e) {
            log.error("Failed to register user: {}", e.getMessage());
            throw e;
        }

        User user = User.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .signupType(SignupType.EMAIL)
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    public LoginResDTO login(String userId, String password) {

        try {
            // 아이디, 비밀번호로 인증
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userId, password));

            // 인증 정보에서 사용자 정보를 가져옴
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            UserInfoDTO userInfo = UserInfoDTO.builder()
                    .userId(user.getUserId())
                    .roleName(user.getRoleName())
                    .build();

            String accessToken = jwtUtil.createAccessToken(userInfo);
            String refreshToken = jwtUtil.createRefreshToken(userInfo);

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return LoginResDTO.builder()
                    .userId(user.getUserId())
                    .role(user.getRoleName())
                    .roleDisplayName(user.getRoleDisplayName())
                    .phoneNumber(user.getPhoneNumber())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (BadCredentialsException e) {
            // 비밀번호 불일치 등의 인증 실패 처리
            log.error("Failed to login: {}", e.getMessage());
            throw new BaseException(ErrorCode.INVALID_ID_PASSWORD);
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("Failed to login: {}", e.getMessage());
            throw new BaseException(ErrorCode.FAILED_TO_LOGIN, e);
        }
    }

    public String refreshAccessToken(String clientId, String refreshToken) {

        // 리프레시 토큰 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String userId = jwtUtil.extractUserId(refreshToken);

        // 요청한 사용자 아이디와 리프레시 토큰에 포함된 사용자 아이디가 일치하는지 확인
        if (!clientId.equals(userId)) {
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        // 저장된 리프레시 토큰과 요청된 리프레시 토큰이 일치하는지 확인
        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 새로운 액세스 토큰 생성
        UserInfoDTO userInfo = UserInfoDTO.builder()
                .userId(user.getUserId())
                .roleName(user.getRoleName())
                .build();
        return jwtUtil.createAccessToken(userInfo);
    }

    public LoginResDTO kakaoLogin(String code) {
        try {
            // 1. 카카오 액세스 토큰 받기
            String kakaoAccessToken = getKakaoAccessToken(code);

            // 2. 카카오 사용자 정보 받기
            KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);

            // 3. 카카오 ID로 회원가입 되어있는지 확인
            User user = userRepository.findByUserId(KAKAO_PREFIX + kakaoUserInfo.getId())
                    .orElseGet(() -> registerKakaoUser(kakaoUserInfo));

            // 4. JWT 토큰 생성 및 반환
            UserInfoDTO userInfo = UserInfoDTO.builder()
                    .userId(user.getUserId())
                    .roleName(user.getRoleName())
                    .build();

            String accessToken = jwtUtil.createAccessToken(userInfo);
            String refreshToken = jwtUtil.createRefreshToken(userInfo);

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return LoginResDTO.builder()
                    .userId(user.getUserId())
                    .role(user.getRoleName())
                    .roleDisplayName(user.getRoleDisplayName())
                    .phoneNumber(user.getPhoneNumber())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (BaseException e) {
            log.error("Failed to login with Kakao: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to login with Kakao: {}", e.getMessage());
            throw new BaseException(ErrorCode.FAILED_TO_LOGIN, "카카오 로그인 실패");
        }
    }

    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        String requestBody = String.format(
                "grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
                kakaoClientId, kakaoClientSecret, code, kakaoRedirectUri);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                KakaoTokenResponse.class);

        if (response.getBody() == null) {
            throw new BaseException(ErrorCode.FAILED_TO_LOGIN, "카카오 로그인 실패");
        }

        return response.getBody().getAccessToken();
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                requestEntity,
                KakaoUserInfo.class);

        if (response.getBody() == null) {
            throw new BaseException(ErrorCode.FAILED_TO_LOGIN, "카카오 사용자 정보 조회 실패");
        }

        return response.getBody();
    }

    private User registerKakaoUser(KakaoUserInfo kakaoUserInfo) {
        User user = User.builder()
                .userId(KAKAO_PREFIX + kakaoUserInfo.getId())
                .name(kakaoUserInfo.getNickname())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .signupType(SignupType.KAKAO)
                .role(Role.ROLE_USER)
                .socialId(String.valueOf(kakaoUserInfo.getId()))
                .email(kakaoUserInfo.getEmail())
                .build();

        return userRepository.save(user);
    }
}
