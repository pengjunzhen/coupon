package com.geekbang.coupon.calculation.template;

import com.geekbang.coupon.calculation.api.beans.ShoppingCart;
import com.geekbang.coupon.calculation.template.impl.DiscountTemplate;
import com.geekbang.coupon.calculation.template.impl.DummyTemplate;
import com.geekbang.coupon.calculation.template.impl.LonelyNightTemplate;
import com.geekbang.coupon.calculation.template.impl.MoneyOffTemplate;
import com.geekbang.coupon.calculation.template.impl.RandomReductionTemplate;
import com.geekbang.coupon.template.api.beans.CouponTemplateInfo;
import com.geekbang.coupon.template.api.enums.CouponType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 工厂方法根据订单中的优惠券信息，返回对应的Template用于计算优惠价
 */
@Component
@Slf4j
public class CouponTemplateFactory {

    private final DummyTemplate dummyTemplate;

    private final MoneyOffTemplate moneyOffTemplate;

    private final RandomReductionTemplate randomReductionTemplate;

    private final DiscountTemplate discountTemplate;

    private final LonelyNightTemplate lonelyNightTemplate;

    public CouponTemplateFactory(DummyTemplate dummyTemplate, MoneyOffTemplate moneyOffTemplate, RandomReductionTemplate randomReductionTemplate, DiscountTemplate discountTemplate, LonelyNightTemplate lonelyNightTemplate) {
        this.dummyTemplate = dummyTemplate;
        this.moneyOffTemplate = moneyOffTemplate;
        this.randomReductionTemplate = randomReductionTemplate;
        this.discountTemplate = discountTemplate;
        this.lonelyNightTemplate = lonelyNightTemplate;
    }

    /**
     * 通过传入的信息获取对应的优惠券模板
     *
     * @param order
     * @return
     */
    public RuleTemplate getTemplate(ShoppingCart order) {
        if (CollectionUtils.isEmpty(order.getCouponInfos())) {
            return dummyTemplate;
        }
        CouponTemplateInfo couponTemplateInfo = order.getCouponInfos().get(0).getTemplate();
        CouponType couponType = CouponType.convert(couponTemplateInfo.getType());
        switch (couponType) {
            case MONEY_OFF:
                return moneyOffTemplate;
            case RANDOM_DISCOUNT:
                return randomReductionTemplate;
            case DISCOUNT:
                return discountTemplate;
            case LONELY_NIGHT_MONEY_OFF:
                return lonelyNightTemplate;
            default:
                return dummyTemplate;
        }
    }
}
