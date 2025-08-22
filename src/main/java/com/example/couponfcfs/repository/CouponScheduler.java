package com.example.couponfcfs.repository;


import com.example.couponfcfs.model.Coupon;
import com.example.couponfcfs.model.CouponInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final CouponInfoRepository couponInfoRepository;
    private final CouponRepository couponRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;

    @Scheduled(fixedRate = 20000)
    @Transactional
    public void flushCoupons() {
        System.out.println("### 쿠폰 동기화 스케쥴러 시작 ###");

        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();
        SetOperations<String, Object> set = redisTemplateForCoupon.opsForSet();

        List<Object> couponsToSync = set.pop("CouponSync",100);

        if (couponsToSync == null || couponsToSync.isEmpty()) {
            log.info("동기화할 새로운 쿠폰이 없습니다.");
            return;
        }

        List<CouponInfo> couponInfosToSave = new ArrayList<>();

        for (Object coupon : couponsToSync) {
            String[] parts = ((String) coupon).split(":");
            String couponName = parts[0];
            String couponNumber = parts[1];

            String userName = hash.get(couponName, couponNumber);

            if (userName != null && !"0".equals(userName)) {
                CouponInfo newCouponInfo = CouponInfo.builder()
                        .couponName(couponName)
                        .couponNumber(Integer.valueOf(couponNumber))
                        .userName(userName)
                        .build();
                couponInfosToSave.add(newCouponInfo);
            }
        }

        if (!couponInfosToSave.isEmpty()) {

            Map<String, String> stock = hash.entries("Stock");

            List<String> couponNames = List.of("A", "B", "C");
            List<Coupon> couponsList = couponRepository.findAllById(couponNames);

            for (Coupon coupon : couponsList) {
                String couponId = coupon.getId();
                int newQuantity = Integer.parseInt(stock.get(couponId));
                coupon.updateQuantity(newQuantity);
            }


            couponInfoRepository.saveAll(couponInfosToSave);
            log.info("{}개의 쿠폰 동기화했습니다.", couponInfosToSave.size());
        }

    }
}