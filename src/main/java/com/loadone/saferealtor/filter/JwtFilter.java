package com.loadone.saferealtor.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.exception.ErrorResponse;
import com.loadone.saferealtor.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // JWT 유틸리티 클래스

    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/sendVerificationCode",
            "/api/auth/verifyCode",
            "/api/auth/checkUserId",
            "/api/auth/refreshToken",
            "/api/properties",
            "/uploads"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 로그인, 회원가입, 인증번호 요청 API는 필터링하지 않음
        String path = request.getServletPath();
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String userId = null;

        try {

            // Authorization 헤더가 없거나 Bearer 토큰이 없는 경우 처리
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new BaseException(ErrorCode.MISSING_TOKEN);  // 토큰이 없는 경우 예외 발생
            }

            // 헤더에서 JWT 추출
            token = authorizationHeader.substring(7);

            // JWT 유효성 검사
            if (!jwtUtil.validateToken(token)) {
                throw new BaseException(ErrorCode.INVALID_TOKEN);  // 토큰이 유효하지 않은 경우 예외 발생
            }

            // JWT에서 사용자 ID 추출
            userId = jwtUtil.extractUserId(token);

            // SecurityContext에 인증 정보가 없고, JWT가 유효한 경우
            if (userId != null || SecurityContextHolder.getContext().getAuthentication() == null) {
                // JWT에서 권한 정보 추출
                SimpleGrantedAuthority authority = jwtUtil.extractRole(token);

                // 사용자 정보를 바탕으로 Authentication 객체 생성
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userId, null, List.of(authority));

                // 인증 정보 SecurityContext에 설정
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (BaseException e) {
            log.error("JWT Filter Error: {}", e.getErrorCode().getMessage());
            handleException(response, e);
            return;
        } catch (ExpiredJwtException e) {
            log.error("JWT Filter Error:  Token expired");
            handleException(response, new BaseException(ErrorCode.EXPIRED_TOKEN));
            return;
        } catch (Exception e) {
            log.error("JWT Filter Error: {}", e.getMessage());
            handleException(response, new BaseException(ErrorCode.INVALID_TOKEN));
            return;
        }

        // 다음 필터 실행
        chain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, BaseException be) throws IOException {
        log.error("JWT Filter Error: {} - {}", be.getErrorCode().name(), be.getMessage());

        response.setStatus(be.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답 생성 및 전송
        ErrorResponse errorResponse = new ErrorResponse(be.getErrorCode().name(), be.getMessage());

        // ObjectMapper를 사용하여 객체를 JSON으로 직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
