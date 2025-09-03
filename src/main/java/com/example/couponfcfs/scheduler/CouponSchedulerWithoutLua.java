package com.example.couponfcfs.scheduler;

import com.example.couponfcfs.model.IssuedCoupon;
import com.example.couponfcfs.model.Status;
import com.example.couponfcfs.repository.CouponRepository;
import com.example.couponfcfs.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.field.FieldDescription;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Profile("scheduler-WithoutLua")
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponSchedulerWithoutLua implements CouponScheduler {

    private final RedisTemplate<String, Object> redisTemplateForCoupon;
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    private static final String COUPON_ISSUE_QUEUE_KEY = "coupon_issue_queue";
    private static final int BATCH_SIZE = 100; // 한 번에 DB에 저장할 데이터 개수

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

        List<IssuedCoupon> issuedCoupons = new ArrayList<>();

        for (int i = 0; i < BATCH_SIZE; i++) {
            Object issueInfoObject = redisTemplateForCoupon.opsForList().leftPop(COUPON_ISSUE_QUEUE_KEY);

            if (issueInfoObject == null) {
                break;
            }
            String[] issueInfo = String.valueOf(issueInfoObject).split(":");
            if (issueInfo.length == 2) {
                String couponName = issueInfo[0];
                String userId = issueInfo[1];
                issuedCoupons.add(new IssuedCoupon(couponName, userId, Status.ISSUED));
            }
        }
        if (!issuedCoupons.isEmpty()) {
            issuedCouponRepository.saveAll(issuedCoupons);
            log.info("{}개의 쿠폰 발급 내역을 DB에 저장했습니다.", issuedCoupons.size());
        }
        log.info("### 쿠폰 동기화 스케쥴러 끝 ###");

    }
}
