package com.loadone.saferealtor.exception;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        // 로그 기록
        log.error("BaseException occurred: {} {}, HTTP Status: {}", ex.getErrorCode(), ex.getMessage(), ex.getHttpStatus(), ex);

        ErrorResponse response = new ErrorResponse(ex.getErrorCode().name(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // 로그 기록
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
