package com.example.couponfcfs.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity <ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 인자 값 또는 권한 없음: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.INVALID_ARGUMENT.getCode(), ErrorCode.INVALID_ARGUMENT.getMessage()));

    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleBoardNotFoundException(DuplicateException e) {
        log.warn("중복 사용자 : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ErrorCode.DUPLICATE.getCode(), ErrorCode.DUPLICATE.getMessage()));
    }

    @ExceptionHandler(SoldOutException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(SoldOutException e) {
        log.warn("재고 없음 : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ErrorCode.SOLD_OUT.getCode(), ErrorCode.SOLD_OUT.getMessage()));
    }

}
