package com.example.couponfcfs.controller;


import com.example.couponfcfs.dto.ResponseDto;
import com.example.couponfcfs.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CouponApiController {

    private final CouponService couponService;

    @PostMapping("/Coupon/{id}")
    public ResponseEntity<ResponseDto> couponApi(@PathVariable String id) {

        ResponseDto responseDto = couponService.selectCoupon(id);
        return ResponseEntity.ok(responseDto);
    }

}
