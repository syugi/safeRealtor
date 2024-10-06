package com.loadone.saferealtor.exception;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

@Log4j2
@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    static final String ERROR = "[ERROR]";

    public BaseException(ErrorCode errorCode) {
        super(ERROR+errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    public BaseException(ErrorCode errorCode, String customMessage) {
        super(ERROR+customMessage);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    public BaseException(ErrorCode errorCode, Exception e) {
        super(ERROR+errorCode.getMessage());
        log.error("BaseException occurred: {} {}, HTTP Status: {}", errorCode, errorCode.getMessage(), errorCode.getHttpStatus(), e);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    @Override
    public String getMessage() {
        return super.getMessage() != null ? super.getMessage() : errorCode.getMessage();
    }
}
