package com.example.couponfcfs.repository;

import com.example.couponfcfs.model.Coupon;
import com.example.couponfcfs.model.CouponEmp;
import com.example.couponfcfs.model.CouponInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueScheduler {

    private final CouponInfoRepository couponInfoRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;

    @Transactional
    public void insertCoupon(){

        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();
        SetOperations<String, Object> set = redisTemplateForCoupon.opsForSet();

        List<Object> couponList = set.pop("CouponSync",100);

        List<CouponInfo> couponInfosToSave = new ArrayList<>();

        for (Object coupon : couponList) {
            String[] parts = ((String) coupon).split(":");
            String couponName = parts[0];
            String couponNumber = parts[1];

            String userName = hash.get(couponName, couponNumber);

            if (userName != null && !"0".equals(userName)) {
                CouponEmp couponEmp = new CouponEmp(couponName, Integer.parseInt(couponNumber));

                CouponInfo newCouponInfo = CouponInfo.builder()
                        .couponEmp(couponEmp)
                        .userName(userName)
                        .build();
                couponInfosToSave.add(newCouponInfo);
            }
        }

        if (!couponInfosToSave.isEmpty()) {
            couponInfoRepository.saveAll(couponInfosToSave);
            log.info("{}개의 쿠폰 동기화했습니다.", couponInfosToSave.size());
        }

    }
}
