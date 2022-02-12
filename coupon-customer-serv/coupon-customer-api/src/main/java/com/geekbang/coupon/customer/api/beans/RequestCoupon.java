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
}
