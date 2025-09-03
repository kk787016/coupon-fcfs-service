package com.example.couponfcfs.scheduler.withLua;


import com.example.couponfcfs.model.Coupon;
import com.example.couponfcfs.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponStockJob {

    private final CouponRepository couponRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;

    @Transactional
    public void updateCouponStock() {

        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();

        Map<String, String> stock = hash.entries("Stock");

        List<String> couponNames = List.of("A", "B", "C");
        List<Coupon> couponsList = couponRepository.findAllById(couponNames);

        for (Coupon coupon : couponsList) {
            Long couponId = coupon.getId();
            int newQuantity = Integer.parseInt(stock.get(couponId));
            coupon.updateQuantity(newQuantity);
        }

    }


}
