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
//        Coupon couponA = new Coupon("A",1);
//        Coupon couponB = new Coupon("B",10);
//        Coupon couponC = new Coupon("C",89);
//
//        couponRepository.save(couponA);
//        couponRepository.save(couponB);
//        couponRepository.save(couponC);
//

        //List<IssuedCoupon> allIssuedCoupons = new ArrayList<>();


//        CouponEmp couponA_Id = new CouponEmp("A", 1);
//
//        CouponInfo couponInfoA = CouponInfo.builder()
//                .couponEmp(couponA_Id)
//                .userName(null)
//                .build();
//
//        allCouponInfos.add(couponInfoA);
//
//        for (int i = 1; i <= 30; i++) {
//            CouponEmp couponB_Id = new CouponEmp("B", i);
//            CouponInfo couponInfoB = CouponInfo.builder()
//                    .couponEmp(couponB_Id)
//                    .userName(null)
//                    .build();
//            allCouponInfos.add(couponInfoB);
//        }
//
//        for (int i = 1; i <= 69; i++) {
//            CouponEmp couponC_Id = new CouponEmp("C", i);
//            CouponInfo couponInfoC = CouponInfo.builder()
//                    .couponEmp(couponC_Id)
//                    .userName(null)
//                    .build();
//            allCouponInfos.add(couponInfoC);
//        }


        //couponInfoRepository.saveAll(allIssuedCoupons);

    }
    public void initRedis() {

        redisTemplate.opsForValue().set("A","1");
        redisTemplate.opsForValue().set("B","10");
        redisTemplate.opsForValue().set("C","89");
//        redisTemplateForCoupon.delete("UsedUsers");
//        redisTemplateForCoupon.delete("CouponSync");
//
//        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();
//
//        hash.put("A","1","0");
//
//        for(int i = 1; i <= 30; i++){
//            hash.put("B", String.valueOf(i),"0");
//        }
//
//        for(int i = 1; i <= 69; i++){
//            hash.put("C", String.valueOf(i),"0");
//        }
//        hash.put("Stock","A","1");
//        hash.put("Stock","B","30");
//        hash.put("Stock","C","69");
//
//
//        hash.put("StockCheck","A","0");
//        hash.put("StockCheck","B","0");
//        hash.put("StockCheck","C","0");



    }
}
