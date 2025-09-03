package com.example.couponfcfs.scheduler;

import com.example.couponfcfs.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.field.FieldDescription;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Profile("scheduler-WithoutLua")
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponSchedulerWithoutLua implements CouponScheduler {

    private final RedisTemplate<String, Object> redisTemplateForCoupon;

    private final CouponRepository couponRepository;
    @Override
    @Scheduled(fixedRate = 20000)
    @Transactional
    public void flushCoupons() {
        log.info("### 쿠폰 동기화 스케쥴러 시작 ###");
        couponRepository.findAll().forEach(coupon -> {
            Object presentCoupon = redisTemplateForCoupon.opsForValue().get(coupon.getCouponName());

            if (Objects.nonNull(presentCoupon)) {
                int Quantity = Integer.parseInt(String.valueOf(presentCoupon));
                coupon.updateQuantity(Quantity);
            }
            else {
                log.warn("레디스에서 해당 키 값을 찾을 수 없습니다.");
            }
        });
        log.info("### 쿠폰 동기화 스케쥴러 끝 ###");

    }
}
