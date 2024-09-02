package com.loadone.saferealtor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public BaseException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public BaseException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public BaseException(ErrorCode errorCode, String customMessage, HttpStatus httpStatus) {
        super(customMessage);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return super.getMessage() != null ? super.getMessage() : errorCode.getMessage();
    }
}
