package com.loadone.saferealtor.exception;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

@Log4j2
@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    public BaseException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    public BaseException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage());
        log.error("BaseException occurred: {} {}, HTTP Status: {}", errorCode, errorCode.getMessage(), errorCode.getHttpStatus(), e);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    @Override
    public String getMessage() {
        return super.getMessage() != null ? super.getMessage() : errorCode.getMessage();
    }
}
