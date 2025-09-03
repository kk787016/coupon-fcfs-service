package com.example.couponfcfs.scheduler.withoutLua;

import com.example.couponfcfs.model.IssuedCoupon;
import com.example.couponfcfs.model.Status;
import com.example.couponfcfs.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Profile("scheduler-WithoutLua")
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssuanceDbWriter {

    private final RedisTemplate<String, Object> redisTemplateForCoupon;
    private final IssuedCouponRepository issuedCouponRepository;

    private static final String COUPON_ISSUE_QUEUE_KEY = "coupon_issue_queue";
    private static final int BATCH_SIZE = 100; // 한 번에 DB에 저장할 데이터 개수

    @Scheduled(fixedRate = 2000)
    @Transactional
    public void CouponIssuedSync() {
        log.info("### 발급된 쿠폰 동기화 스케쥴러 시작 ###");

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
        log.info("### 발급된 쿠폰 동기화 스케쥴러 끝 ###");

    }
}
