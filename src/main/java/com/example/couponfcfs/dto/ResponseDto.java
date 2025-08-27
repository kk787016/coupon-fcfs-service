package com.example.couponfcfs.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseDto {

    private final String userId;
    private final String item;

    @Builder
    public ResponseDto(String userId, String item) {
        this.userId = userId;
        this.item = item;
    }
}
