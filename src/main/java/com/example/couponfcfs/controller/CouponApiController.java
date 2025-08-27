package com.example.couponfcfs.controller;


import com.example.couponfcfs.dto.ResponseDto;
import com.example.couponfcfs.service.CouponService;
import com.example.couponfcfs.service.CouponServiceWithLua;
import com.example.couponfcfs.service.CouponServiceWithoutLua;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CouponApiController {

    private final CouponServiceWithLua couponServiceWithLua;
    private final CouponServiceWithoutLua couponServiceWithoutLua;

    @PostMapping("/Coupon/{id}")
    public ResponseEntity<ResponseDto> couponApi(@PathVariable String id) {

        ResponseDto responseDto = couponServiceWithLua.selectCoupon(id);
        return ResponseEntity.ok(responseDto);
    }
    @PostMapping("/CouponWithoutLua/{id}")
    public ResponseEntity<ResponseDto> couponApiNoLua(@PathVariable String id) {

        ResponseDto responseDto = couponServiceWithoutLua.selectCoupon(id);
        return ResponseEntity.ok(responseDto);
    }

}
