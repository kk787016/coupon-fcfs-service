package com.example.couponfcfs.scheduler;

import com.example.couponfcfs.model.IssuedCoupon;
import com.example.couponfcfs.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueJob {

    private final IssuedCouponRepository issuedCouponRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;

    @Transactional
    public void insertCoupon(){

        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();
        SetOperations<String, Object> set = redisTemplateForCoupon.opsForSet();

        List<Object> couponList = set.pop("CouponSync",100);

        List<IssuedCoupon> issuedCouponInfosToSave = new ArrayList<>();

        for (Object coupon : couponList) {
            String[] parts = ((String) coupon).split(":");
            String couponName = parts[0];
            String couponNumber = parts[1];

            String userName = hash.get(couponName, couponNumber);

            if (userName != null && !"0".equals(userName)) {

                IssuedCoupon newIssuedCoupon = IssuedCoupon.builder()
                        .userName(userName)
                        .build();
                issuedCouponInfosToSave.add(newIssuedCoupon);
            }
        }

        if (!issuedCouponInfosToSave.isEmpty()) {
            issuedCouponRepository.saveAll(issuedCouponInfosToSave);
            log.info("{}개의 쿠폰 동기화했습니다.", issuedCouponInfosToSave.size());
        }

    }
}
