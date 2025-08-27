package com.example.couponfcfs.service;

import com.example.couponfcfs.model.Coupon;
import com.example.couponfcfs.repository.CouponRepository;
import com.example.couponfcfs.repository.IssuedCouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponServiceWithoutLuaTest {

    private final IssuedCouponRepository issuedCouponRepository;
    private final CouponServiceWithoutLua couponServiceWithoutLua;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;
    private final CouponRepository couponRepository;

    @Autowired
    public CouponServiceWithoutLuaTest(CouponServiceWithoutLua couponServiceWithoutLua, RedisTemplate<String, Object> redisTemplateForCoupon, IssuedCouponRepository issuedCouponRepository, CouponRepository couponRepository) {
        this.redisTemplateForCoupon = redisTemplateForCoupon;
        this.couponServiceWithoutLua = couponServiceWithoutLua;
        this.issuedCouponRepository = issuedCouponRepository;
        this.couponRepository = couponRepository;
    }

    @BeforeEach
    public void setUp() {

        Coupon couponA = new Coupon("A",1);
        Coupon couponB = new Coupon("B",10);
        Coupon couponC = new Coupon("C",89);

        couponRepository.save(couponA);
        couponRepository.save(couponB);
        couponRepository.save(couponC);

        redisTemplateForCoupon.opsForValue().set("A","1");
        redisTemplateForCoupon.opsForValue().set("B","10");
        redisTemplateForCoupon.opsForValue().set("C","89");
    }

    @AfterEach
    public void tearDown() {
        // 각 테스트가 끝난 후, RDB의 Coupon 데이터를 모두 삭제합니다.
        couponRepository.deleteAllInBatch();
    }
    @Test
    void getCoupon() throws InterruptedException{

        int threadCount = 500;

        ExecutorService executorService = Executors.newFixedThreadPool(200);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try{
                    String randomId = UUID.randomUUID().toString();
                    couponServiceWithoutLua.selectCoupon(randomId);
                }catch (RuntimeException e) {
                    // 재고 소진, 중복 발급 등으로 예외가 발생하는 것은 정상적인 시나리오이므로,
                    // 테스트가 중단되지 않도록 catch 해줍니다.
                     System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then: 모든 작업이 끝난 후 최종 결과를 검증
        // 1. RDB에 저장된 발급된 쿠폰의 총 개수는 100개여야 합니다.
        long issuedCount = issuedCouponRepository.count();
        assertThat(issuedCount).isEqualTo(100);

        // 2. Redis에 남은 재고량의 합은 0이어야 합니다.
        long stockA = Long.parseLong((String) Objects.requireNonNull(redisTemplateForCoupon.opsForValue().get("A")));
        long stockB = Long.parseLong((String) Objects.requireNonNull(redisTemplateForCoupon.opsForValue().get("B")));
        long stockC = Long.parseLong((String) Objects.requireNonNull(redisTemplateForCoupon.opsForValue().get("C")));
        assertThat(stockA + stockB + stockC).isZero();
//
//        // 3. 중복 발급을 체크하는 Set의 크기도 100이어야 합니다.
//        Long duplicateSetSize = redisTemplateForCoupon.opsForSet().size("duplicate");
//        assertThat(duplicateSetSize).isEqualTo(100);

        System.out.println("총 발급된 쿠폰 수: " + issuedCount);
        System.out.println("남은 재고 (A, B, C): " + stockA + ", " + stockB + ", " + stockC);

    }

}