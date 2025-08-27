package com.example.couponfcfs.service;


import com.example.couponfcfs.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface CouponService {

    ResponseDto selectCoupon(String userId);

    default String randomCoupon(){
        double randNum = Math.random();
        int num = (int) (randNum * 100) + 1;

        if (num == 1) {
            return "A";
        }

        if (num <= 11) {
            return "B";
        }
        return "C";
    }

}
