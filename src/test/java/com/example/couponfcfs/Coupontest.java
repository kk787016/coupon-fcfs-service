package com.example.couponfcfs;

import com.example.couponfcfs.dto.ResponseDto;
import com.example.couponfcfs.service.CouponService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class Coupontest {

    private final CouponService couponService;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;

    @Autowired
    public Coupontest(CouponService couponService, RedisTemplate<String, Object> redisTemplateForCoupon) {
        this.redisTemplateForCoupon = redisTemplateForCoupon;
        this.couponService  = couponService;
    }

    @BeforeEach
    public void setUp() {

        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();

        redisTemplateForCoupon.delete("A");
        redisTemplateForCoupon.delete("B");
        redisTemplateForCoupon.delete("C");
        redisTemplateForCoupon.delete("Stock");
        redisTemplateForCoupon.delete("StockCheck");
        redisTemplateForCoupon.delete("UsedUsers");
        hash.put("A","1","0");

        for(int i = 1; i <= 30; i++){
            hash.put("B", String.valueOf(i),"0");
        }

        for(int i = 1; i <= 69; i++){
            hash.put("C", String.valueOf(i),"0");
        }
        hash.put("Stock","A","1");
        hash.put("Stock","B","30");
        hash.put("Stock","C","69");

        hash.put("StockCheck","A","0");
        hash.put("StockCheck","B","0");
        hash.put("StockCheck","C","0");

    }

    @Test
    void couponTes1t() throws InterruptedException {

        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();

        int threadCount = 200;

        ExecutorService executorService = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try{
                    String randomId = UUID.randomUUID().toString();
                    couponService.selectCoupon(randomId);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Map<String, String> entries = hash.entries("StockCheck");

        int num = 0;
        for(Map.Entry<String, String> entry : entries.entrySet()) {
            int value = Integer.parseInt(entry.getValue());
            num = num + value;
        }

        Assertions.assertThat(num).isEqualTo(threadCount);
    }
}
