package com.geekbang.coupon.customer.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCoupon {

    /**
     * 领券的用户id
     */
    @NotNull
    private Long userId;

    /**
     * 券模板ID
     */
    @NotNull
    private Long couponTemplateId;

    // Loadbalancer - 用作测试流量打标(打标签的意思，标识是否是特定的请求)
    // 测试流量打标的方法有很多种，比如添加一个特殊的 key-value 到 Http header，或者塞一个值到 RPC Context 中
    private String trafficVersion;
}
