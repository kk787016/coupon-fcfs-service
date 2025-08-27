package com.example.couponfcfs.repository;

import com.example.couponfcfs.model.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

}
