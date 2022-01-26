package com.geekbang.coupon.template.dao.converter;

import com.alibaba.fastjson.JSON;
import com.geekbang.coupon.template.api.enums.CouponType;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class CouponTypeConverter implements AttributeConverter<CouponType, String> {
    @Override
    public String convertToDatabaseColumn(CouponType couponType) {
        return JSON.toJSONString(couponType);
    }

    @Override
    public CouponType convertToEntityAttribute(String code) {
        return CouponType.convert(code);
    }
}
