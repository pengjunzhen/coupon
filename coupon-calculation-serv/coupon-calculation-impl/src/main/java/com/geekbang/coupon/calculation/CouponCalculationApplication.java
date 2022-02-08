package com.geekbang.coupon.calculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.geekbang"})
public class CouponCalculationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponCalculationApplication.class, args);
    }
}
