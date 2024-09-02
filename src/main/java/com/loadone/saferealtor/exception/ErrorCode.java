package com.loadone.saferealtor.exception;

public enum ErrorCode {
    //서버 오류
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다."),

    //데이터베이스 오류
    DATABASE_ERROR("데이터베이스 오류가 발생했습니다."),
    DATA_INTEGRITY_VIOLATION("데이터 무결성 오류가 발생했습니다."),

    //인증
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED("인증되지 않은 사용자입니다."),

    //사용자 오류
    DUPLICATED_USER_ID("이미 사용중인 아이디입니다."),
    DUPLICATED_PHONE_NUMBER("이미 회원가입된 전화번호입니다."),
    REGISTRATION_FAILED("회원가입에 실패하였습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
