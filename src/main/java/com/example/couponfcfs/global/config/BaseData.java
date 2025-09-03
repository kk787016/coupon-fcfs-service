package com.example.couponfcfs.global.config;

import com.example.couponfcfs.model.Coupon;
import com.example.couponfcfs.repository.CouponRepository;
import com.example.couponfcfs.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseData implements CommandLineRunner {

    private final IssuedCouponRepository issuedCouponRepository;
    private final CouponRepository couponRepository;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public void run(String... args) throws Exception {
        initDB();
        initRedis();
    }

    public void initDB(){
        Coupon couponA = new Coupon("A",1);
        Coupon couponB = new Coupon("B",10);
        Coupon couponC = new Coupon("C",89);

        couponRepository.save(couponA);
        couponRepository.save(couponB);
        couponRepository.save(couponC);

    }
    public void initRedis() {

        redisTemplate.delete("coupon_issued");
        redisTemplate.delete("duplicate");

        redisTemplate.opsForValue().set("A","1");
        redisTemplate.opsForValue().set("B","10");
        redisTemplate.opsForValue().set("C","89");


    }
}
