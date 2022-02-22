package com.geekbang.coupon.customer;

import com.geekbang.coupon.customer.loadbalance.CanaryRuleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.geekbang"})
//@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.geekbang"})
// 用于扫描JPA实体类 @Entity，默认扫本包当下路径
@EntityScan(basePackages = {"com.geekbang"})
@EnableJpaAuditing
// 发到coupon-template-serv的调用，使用CanaryRuleConfiguration中定义的负载均衡Rule
@LoadBalancerClient(value = "coupon-template-serv", configuration = {CanaryRuleConfiguration.class})
public class CouponCustomApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponCustomApplication.class, args);
    }
}
