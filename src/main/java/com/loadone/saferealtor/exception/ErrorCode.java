package com.loadone.saferealtor.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //서버 오류
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    //데이터베이스 오류
    DATABASE_ERROR("데이터베이스 오류가 발생했습니다."),
    DATA_INTEGRITY_VIOLATION("데이터 무결성 오류가 발생했습니다."),

    //인증
    INVALID_ID_PASSWORD("아이디 혹은 비밀번호가 일치하지 않습니다."),
    INVALID_VERIFICATION_CODE("인증번호가 일치하지 않습니다."),
    EXPIRED_VERIFICATION_CODE("인증번호가 만료되었습니다."),
    FAILED_TO_SEND_VERIFICATION_CODE("인증번호 발송에 실패하였습니다."),
    FAILED_TO_REGISTER_USER("사용자 등록에 실패하였습니다."),
    FAILED_TO_LOGIN("로그인에 실패하였습니다."),
    ACCESS_DENIED("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

    //토큰
    MISSING_TOKEN("토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    JWT_SECRET_KEY_GENERATION_FAILED("JWT 비밀키 생성에 실패하였습니다."),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    FAILED_TO_REFRESH_TOKEN("토큰 갱신에 실패하였습니다."),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다."),

    //사용자
    INVALID_USER_ID("사용 불가능한 아이디 입니다."),
    INVALID_USER_ID_FORMAT("아이디는 4~20자의 영문 대소문자, 숫자로만 입력 가능합니다."),
    DUPLICATED_USER_ID("이미 사용중인 아이디입니다."),
    DUPLICATED_PHONE_NUMBER("이미 회원가입된 전화번호입니다."),
    REGISTRATION_FAILED("회원가입에 실패하였습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    //INVALID_PASSWORD_FORMAT("비밀번호는 8~20자의 영문 대소문자, 숫자, 특수문자로만 입력 가능합니다."),
    INVALID_PASSWORD_FORMAT("비밀번호는 4~20자의 영문 대소문자, 숫자로만 입력 가능합니다."),

    //중개사
    FAILED_TO_REGISTER_AGENT("중개사 등록에 실패하였습니다."),
    AGENT_NOT_FOUND("중개사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    //매물
    FAILED_TO_REGISTER_PROPERTY("매물 등록에 실패하였습니다."),
    PROPERTY_NOT_FOUND("매물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAILED_TO_UPDATE_PROPERTY("매물 수정에 실패하였습니다."),

    //파일
    INVALID_FILE_COUNT("파일 갯수가 너무 많습니다.(최대 5개)"),
    INVALID_FILE_SIZE("파일 크기가 너무 큽니다."),

    //SMS
    FAILED_TO_SEND_SMS("SMS 발송에 실패하였습니다."),
    MISSING_AGENT_PHONE_NUMBER("대표 중개사 전화번호가 설정되지 않았습니다."),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
