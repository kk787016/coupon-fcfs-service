package com.example.couponfcfs.service;

import com.example.couponfcfs.dto.ResponseDto;
import com.example.couponfcfs.model.IssuedCoupon;
import com.example.couponfcfs.model.Status;
import com.example.couponfcfs.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceWithoutLua implements CouponService {

    private final RedissonClient redissonClient;
    private final IssuedCouponRepository issuedCouponRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;

    @Override
    @Transactional
    public ResponseDto selectCoupon(String userId) {
        String couponName = randomCoupon();

        String lockKey = "coupon_issue_lock";
        RLock lock = redissonClient.getLock(lockKey);


        try {
            boolean isLocked = lock.tryLock(5,3, TimeUnit.SECONDS); // 5초대기 3초 점유
            if (!isLocked) {
                throw new RuntimeException("많은 양으로 발급 실패");
            }

            Long addedCount = redisTemplateForCoupon.opsForSet().add("duplicate", userId);

            log.info("<UNK>" + couponName+ "<UNK>" + addedCount);
            if (addedCount == null ||addedCount == 0) {
                log.info("중복이에용");
                return new ResponseDto(userId, "D");
            }
            // 재고 확인.
            Object currentStock =  redisTemplateForCoupon.opsForValue().get(couponName);
            if (currentStock == null ||currentStock.equals("0")) {
                log.info("재고 없어용");
                return new ResponseDto(userId, "D");
            }
            redisTemplateForCoupon.opsForValue().decrement(couponName);
            issuedCouponRepository.save(new IssuedCoupon(couponName, userId, Status.ISSUED));


        }catch (InterruptedException e){
           throw new RuntimeException("<UNK>");
        }
        finally {
            lock.unlock();
        }

        return new ResponseDto(userId, couponName);
    }


}
