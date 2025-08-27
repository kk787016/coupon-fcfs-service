package com.example.couponfcfs.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class IssuedCoupon {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COUPON_NAME",nullable = false)
    private String couponName;

    @Column(name = "USER_NAME" ,unique = true)
    private String userName;

    @CreatedDate
    private LocalDateTime issuedAt;

    @Enumerated(EnumType.STRING)
    private Status status;



    @Builder
    public IssuedCoupon(String couponName,String userName, Status status) {
        this.couponName = couponName;
        this.userName = userName;
        this.status = status;
    }


}
