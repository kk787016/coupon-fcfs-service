package com.example.couponfcfs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COUPON_NAME", nullable = false, unique = true)
    private String couponName;

    @Column(name = "ISSUE_QUANTITY", nullable = false)
    private int issueQuantity;

    @Column(name = "CURRENT_ISSUED_COUNT",nullable = false)
    private int currentIssuedCount;


    public Coupon(String couponName, int quantity) {
        this.couponName = couponName;
        this.issueQuantity = quantity;
        this.currentIssuedCount = quantity;

    }

    public void updateQuantity(int quantity) {
        this.currentIssuedCount = quantity;
    }

}
