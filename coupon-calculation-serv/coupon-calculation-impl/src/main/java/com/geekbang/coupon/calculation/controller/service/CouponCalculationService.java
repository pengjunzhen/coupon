package com.geekbang.coupon.calculation.controller.service;

import com.geekbang.coupon.calculation.api.beans.ShoppingCart;
import com.geekbang.coupon.calculation.api.beans.SimulationOrder;
import com.geekbang.coupon.calculation.api.beans.SimulationResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface CouponCalculationService {

    /**
     * 结算订单价格
     *
     * @param cart 购物车
     * @return 带有计算优惠后的价格
     */
    ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart cart);

    /**
     * 试算订单（算出最优惠的价格）
     *
     * @param cart 购物车
     * @return
     */
    SimulationResponse simulateOrder(@RequestBody SimulationOrder cart);
}
