package com.geekbang.coupon.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.geekbang"})
//@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.geekbang"})
// 用于扫描JPA实体类 @Entity，默认扫本包当下路径
@EntityScan(basePackages = {"com.geekbang"})
public class CouponCustomApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponCustomApplication.class, args);
    }
}
