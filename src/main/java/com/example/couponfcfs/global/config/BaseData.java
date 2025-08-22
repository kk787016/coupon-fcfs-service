package com.example.couponfcfs.global.config;

import com.example.couponfcfs.model.Coupon;
import com.example.couponfcfs.model.CouponInfo;
import com.example.couponfcfs.repository.CouponRepository;
import com.example.couponfcfs.repository.CouponInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseData implements CommandLineRunner {

    private final CouponInfoRepository couponInfoRepository;
    private final CouponRepository couponRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;


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

        CouponInfo couponInfoA = CouponInfo.builder()
                .couponNumber(1)
                .couponName("A")
                .userName("null")
                .build();

        couponInfoRepository.save(couponInfoA);

        for(int i = 1; i <= 30; i++){
            CouponInfo couponInfoB = CouponInfo.builder()
                    .couponNumber(i)
                    .couponName("B")
                    .userName("null")
                    .build();

            couponInfoRepository.save(couponInfoB);
        }

        for(int i = 1; i <= 69; i++){
            CouponInfo couponInfoC = CouponInfo.builder()
                    .couponNumber(i)
                    .couponName("C")
                    .userName("null")
                    .build();

            couponInfoRepository.save(couponInfoC);
        }
    }
    public void initRedis() {

        redisTemplateForCoupon.delete("UsedUsers");

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


        hash.put("StockCheck","A","0");
        hash.put("StockCheck","B","0");
        hash.put("StockCheck","C","0");



    }
}
