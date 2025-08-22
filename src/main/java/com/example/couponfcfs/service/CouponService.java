package com.example.couponfcfs.service;


import com.example.couponfcfs.dto.ResponseDto;
import com.example.couponfcfs.repository.CouponInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponInfoRepository couponInfoRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;
    private final RedisScript<String> issueCouponScript;

    @Transactional
    public ResponseDto selectCoupon(String id) {

        String couponName = randomCoupon();

        List<String> keys = List.of(couponName, "Stock", "UsedUsers");

        String issuedCouponField = redisTemplateForCoupon.execute(issueCouponScript, keys, couponName,id);

        if (issuedCouponField.equals("DUPLICATE")){
            log.warn("중복 꽝 ");
            return new ResponseDto(id, "D");
        }
        if (issuedCouponField.equals("SOLD_OUT")){
            log.warn("재고 꽝");
            return new ResponseDto(id, "D");
        }
        if (issuedCouponField.equals("INVALID_ARGUMENT")){
            log.warn("@@@@@@@@@@@@@@@@@@@@@@@");
            return new ResponseDto(id, "D");
        }

        log.info("사용자 ID [{}], 쿠폰 [{}] 발급 성공! 할당된 필드: {}", id, couponName, issuedCouponField);

        return new ResponseDto(id, couponName);

    }

    private String randomCoupon(){
        String couponName = " ";
        double randNum = Math.random();
        int num = (int) (randNum * 100) + 1;

        if (num == 1) couponName = "A";
        else if (2 <= num && num <= 11) couponName = "B";
        else if (12 <= num && num <= 100) couponName = "C";

        return couponName;
    }


}
