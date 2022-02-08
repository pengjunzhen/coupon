package com.geekbang.coupon.calculation.template;

import com.geekbang.coupon.calculation.api.beans.ShoppingCart;

public interface RuleTemplate {

    /**
     * 计算优惠券
     *
     * @param settlement 购物车
     * @return 带有计算优惠后的价格
     */
    ShoppingCart calculate(ShoppingCart settlement);
}
