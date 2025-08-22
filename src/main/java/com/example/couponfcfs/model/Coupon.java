package com.example.couponfcfs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Coupon {

    @Id
    @Column(name = "coupon")
    private String id;

    @Column
    private int quantity;


    @OneToMany (mappedBy = "couponName")
    private List<CouponInfo> couponInfoName= new ArrayList<>();


    public Coupon(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

}
