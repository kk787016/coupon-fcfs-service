package com.example.couponfcfs.service;


import com.example.couponfcfs.dto.ResponseDto;
import com.example.couponfcfs.model.IssuedCoupon;
import com.example.couponfcfs.model.Status;
import com.example.couponfcfs.repository.IssuedCouponRepository;
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
public class CouponServiceWithLua implements CouponService {


    private final IssuedCouponRepository issuedCouponRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;
    private final RedisScript<String> issueCouponScript;

    @Override
    @Transactional
    public ResponseDto selectCoupon(String userId) {
        String couponName = randomCoupon();

        List<String> keys = List.of(couponName, "Stock", "UsedUsers");

        String issuedCouponField = redisTemplateForCoupon.execute(issueCouponScript, keys, couponName,userId);

        log.info("사용자 ID [{}], 쿠폰 [{}] 발급 성공! 할당된 필드: {}", userId, couponName, issuedCouponField);

        issuedCouponRepository.save(new IssuedCoupon(userId, "a", Status.ISSUED));
        return new ResponseDto(userId, couponName);

    }



}
