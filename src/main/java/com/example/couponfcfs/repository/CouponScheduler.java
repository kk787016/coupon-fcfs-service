package com.example.couponfcfs.repository;


import com.example.couponfcfs.model.Coupon;
import com.example.couponfcfs.model.CouponInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private final RedisTemplate<String, Object> redisTemplateForCoupon;
    private final CouponIssueScheduler couponIssueScheduler;
    private final CouponStockScheduler couponStockScheduler;

    @Scheduled(fixedRate = 20000)
    @Transactional
    public void flushCoupons() {
        log.info("### 쿠폰 동기화 스케쥴러 시작 ###");

        SetOperations<String, Object> set = redisTemplateForCoupon.opsForSet();
        Long couponCount = set.size("CouponSync");

        if (couponCount == null || couponCount == 0) {
            log.info("동기화할 새로운 쿠폰이 없습니다.");
            log.info("### 쿠폰 동기화 스케쥴러 끝 ###");
            return;
        }

        couponIssueScheduler.insertCoupon();
        couponStockScheduler.updateCouponStock();

        log.info("### 쿠폰 동기화 스케쥴러 끝 ###");

    }
}