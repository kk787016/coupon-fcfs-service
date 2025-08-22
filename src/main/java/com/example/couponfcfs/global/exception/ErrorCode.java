package com.example.couponfcfs.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    DUPLICATE("409", "중복 사용자 입니다."),
    SOLD_OUT("409", "쿠폰 재고가 없습니다."),
    INVALID_ARGUMENT("400", "잘못된 접근입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
