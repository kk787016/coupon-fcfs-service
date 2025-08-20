package com.example.couponfcfs.repository;

import com.example.couponfcfs.model.CouponInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CouponRepository extends JpaRepository<CouponInfo, Long> {

    boolean existsByUserName(String userName);
}
