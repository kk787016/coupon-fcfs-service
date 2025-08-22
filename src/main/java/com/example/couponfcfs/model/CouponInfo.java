package com.example.couponfcfs.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CouponInfo {

    @EmbeddedId
    private CouponEmp couponEmp;

    @Column(unique = true)
    private String userName;


    @Builder
    public CouponInfo(CouponEmp couponEmp, String userName) {
        this.couponEmp = couponEmp;
        this.userName = userName;
    }

}
