package com.example.couponfcfs.global.config;

import com.example.couponfcfs.model.Coupon;
import com.example.couponfcfs.model.CouponInfo;
import com.example.couponfcfs.repository.CouponInitRepository;
import com.example.couponfcfs.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseData implements CommandLineRunner {

    private final CouponRepository couponRepository;
    private final CouponInitRepository couponInitRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;


    @Override
    public void run(String... args) throws Exception {
        //initDB();
        initRedis();
    }

    public void initDB(){
        Coupon couponA = new Coupon("A",1);
        Coupon couponB = new Coupon("B",10);
        Coupon couponC = new Coupon("C",89);

        couponInitRepository.save(couponA);
        couponInitRepository.save(couponB);
        couponInitRepository.save(couponC);

        CouponInfo couponInfoA = CouponInfo.builder()
                .couponNumber(1)
                .couponName(couponA)
                .userName("null")
                .build();

        couponRepository.save(couponInfoA);

        for(int i = 1; i <= 30; i++){
            CouponInfo couponInfoB = CouponInfo.builder()
                    .couponNumber(i)
                    .couponName(couponB)
                    .userName("null")
                    .build();

            couponRepository.save(couponInfoB);
        }

        for(int i = 1; i <= 69; i++){
            CouponInfo couponInfoC = CouponInfo.builder()
                    .couponNumber(i)
                    .couponName(couponC)
                    .userName("null")
                    .build();

            couponRepository.save(couponInfoC);
        }
    }
    public void initRedis() {
        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();

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

    }
}
