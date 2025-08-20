package com.example.couponfcfs.repository;

import com.example.couponfcfs.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponInitRepository extends JpaRepository<Coupon, String> {


}
