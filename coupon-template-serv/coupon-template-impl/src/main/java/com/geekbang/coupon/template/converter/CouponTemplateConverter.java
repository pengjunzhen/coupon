package com.geekbang.coupon.template.converter;

import com.geekbang.coupon.template.api.beans.CouponTemplateInfo;
import com.geekbang.coupon.template.dao.entity.CouponTemplate;

public class CouponTemplateConverter {

    public static CouponTemplateInfo convertToTemplateInfo(CouponTemplate couponTemplate) {
        return CouponTemplateInfo.builder()
                .id(couponTemplate.getId())
                .name(couponTemplate.getName())
                .desc(couponTemplate.getDescription())
                .type(couponTemplate.getCategory().getCode())
                .shopId(couponTemplate.getShopId())
                .available(couponTemplate.getAvailable())
                .rule(couponTemplate.getRule())
                .build();
    }
}
