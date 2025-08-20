package com.example.couponfcfs.service;


import com.example.couponfcfs.dto.ResponseDto;
import com.example.couponfcfs.model.CouponInfo;
import com.example.couponfcfs.repository.CouponRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final RedisTemplate<String, Object> redisTemplateForCoupon;

    @Transactional
    public ResponseDto selectCoupon(String id) {

        //중복 검사
//        boolean checkDuplicate = couponRepository.existsByUserName(id);
//        if (checkDuplicate) return null;

        // 확률 체크
        String couponName = " ";
        double randNum = Math.random();
        int num = (int) (randNum * 100) + 1;

        if (num == 1) couponName = "A";
        else if (2 <= num && num <= 11) couponName = "B";
        else if (12 <= num && num <= 100) couponName = "C";

        log.info("1. 랜덤함수 값 " + num);
        log.info("2. 쿠폰 이름 " + couponName);

        //couponName = "A";

        // 재고 체크
        HashOperations<String, String, String> hash = redisTemplateForCoupon.opsForHash();

        int stock = Integer.parseInt(Objects.requireNonNull(hash.get("Stock", couponName)));

        log.info("3. 재고 수 " + stock);

        if (stock == 0) {
            log.warn("재고 0 개,  D 반환");
            int a = Integer.parseInt(Objects.requireNonNull(hash.get("StockCheck", "A")));
            ++a;
            hash.put("StockCheck", "A", String.valueOf(a));
            return new ResponseDto(id, "D");
        }


//        Cursor<Map.Entry<String, String>> cursor =
//                hash.scan(couponName, ScanOptions.NONE);
//
//        while (!cursor.hasNext()) {
//            Map.Entry<String, String> entry = cursor.next();
//            String value = entry.getValue();
//            if (value.equals("0")) {
//                hash.put(couponName, entry.getKey(), id);
//            }
//        }

        Map<String, String> entries = hash.entries(couponName);

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equals("0")) {

                log.info("4. 쿠폰 이름  " + couponName + " 쿠폰 번호 " + key + " 쿠폰 값 " + value);

                stock--;
                hash.put(couponName, entry.getKey(), id);
                hash.put("Stock", couponName, String.valueOf(stock));

                break;
            }
        }


        Map<String, String> currentStock = hash.entries("Stock");
        log.info("5. 남은 쿠폰 갯수 " + currentStock);
        //


        // 리턴 만들기
        //
        return new ResponseDto(id, couponName);
    }


}
