package com.example.couponfcfs.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private final RedisTemplate<String, Object> redisTemplateForCoupon;
    private final CouponIssueJob couponIssueJob;
    private final CouponStockJob couponStockJob;

    @Scheduled(fixedRate = 20000)
    //@Scheduled(fixedDelay = )
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

        couponIssueJob.insertCoupon();
        couponStockJob.updateCouponStock();

        log.info("### 쿠폰 동기화 스케쥴러 끝 ###");

    }
}