package com.loadone.saferealtor.util;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretStr;

    @Value("${jwt.access-token-exp-time}")
    private int accessTokenExpTime;

    @Value("${jwt.refresh-token-exp-time}")
    private int refreshTokenExpTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        try {
            this.secretKey = Keys.hmacShaKeyFor(secretStr.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Failed to generate JWT secret key : "+e.getMessage());
            throw new BaseException(ErrorCode.JWT_SECRET_KEY_GENERATION_FAILED);
        }
    }

    /**
     * Access Token 생성
     * @param userInfo
     * @return Access Token String
     */
    public String createAccessToken(UserInfoDTO userInfo) {
        return createToken(userInfo, accessTokenExpTime);
    }

    /**
     * Refresh Token 생성
     * @param userInfo
     * @return Refresh Token String
     */
    public String createRefreshToken(UserInfoDTO userInfo) {
        return createToken(userInfo, refreshTokenExpTime);
    }

    /**
     * JWT 생성
     * @param userInfo
     * @param expirationMinutes
     * @return JWT String
     */
    private String createToken(UserInfoDTO userInfo, int expirationMinutes) {

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("userId", userInfo.getUserId());
        claimsMap.put("role", userInfo.getRoleName());

        Instant now = Instant.now();
        Instant expiration = now.plus(Duration.ofMinutes(expirationMinutes));

        return Jwts.builder()
                .header().type("JWT")
                .and()
                .subject(userInfo.getUserId())
                .claims(claimsMap)
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰 유효성 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token) {

        try {
            Claims claims = extractAllClaims(token);  // 토큰에서 클레임 추출
            return !isTokenExpired(claims);
        } catch (ExpiredJwtException e) {
            throw new BaseException(ErrorCode.EXPIRED_TOKEN);  // 토큰 만료
        } catch (JwtException | IllegalArgumentException e) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);  // 기타 JWT 관련 문제
        }
    }

    /**
     * JWT에서 username 추출
     * @param token
     * @return username
     */
    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    // JWT에서 권한(roles) 추출
    public SimpleGrantedAuthority extractRole(String token) {
        Claims claims = extractAllClaims(token);
        String role = claims.get("role", String.class);  // JWT 클레임에서 roles 추출
        return new SimpleGrantedAuthority(role);
    }


    // 토큰 만료 여부 확인
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    // 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
