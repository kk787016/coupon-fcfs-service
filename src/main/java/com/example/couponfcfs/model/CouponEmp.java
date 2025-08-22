package com.example.couponfcfs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CouponEmp implements Serializable {

    private String couponName;

    @Column(nullable = false)
    private Integer couponNumber;

}
