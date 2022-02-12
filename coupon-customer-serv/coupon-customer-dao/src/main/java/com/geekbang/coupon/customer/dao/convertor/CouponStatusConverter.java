package com.geekbang.coupon.customer.dao.convertor;

import com.geekbang.coupon.customer.api.enums.CouponStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CouponStatusConverter implements AttributeConverter<CouponStatus, Integer> {


    // 如果需要把DB里的值转换成enum对象，就采用这种方式就好了
    // 利用泛型模板继承AttributeConverter

    /**
     * enum 转 db value
     *
     * @param couponStatus 优惠券枚举类型
     * @return
     */
    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    /**
     * DB value 转 enum 值
     *
     * @param code
     * @return
     */
    @Override
    public CouponStatus convertToEntityAttribute(Integer code) {
        return CouponStatus.convert(code);
    }
}
