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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_name", nullable = false)
    private Coupon couponName;

    private Integer couponNumber;

    @Column( nullable = false)
    private String userName;


    @Builder
    public CouponInfo(Coupon couponName, Integer couponNumber, String userName) {
        this.couponName = couponName;
        this.couponNumber = couponNumber;
        this.userName = userName;
    }

}
