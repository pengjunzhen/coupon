package com.geekbang.coupon.calculation.controller.service.impl;

import com.alibaba.fastjson.JSON;
import com.geekbang.coupon.calculation.api.beans.ShoppingCart;
import com.geekbang.coupon.calculation.api.beans.SimulationOrder;
import com.geekbang.coupon.calculation.api.beans.SimulationResponse;
import com.geekbang.coupon.calculation.controller.service.CouponCalculationService;
import com.geekbang.coupon.calculation.template.CouponTemplateFactory;
import com.geekbang.coupon.calculation.template.RuleTemplate;
import com.geekbang.coupon.template.api.beans.CouponInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
public class CouponCalculationServiceImpl implements CouponCalculationService {

    private final CouponTemplateFactory couponTemplateFactory;

    public CouponCalculationServiceImpl(CouponTemplateFactory couponTemplateFactory) {
        this.couponTemplateFactory = couponTemplateFactory;
    }

    @Override
    public ShoppingCart calculateOrderPrice(ShoppingCart cart) {
        log.info("calculate order price: {}", JSON.toJSONString(cart));
        RuleTemplate template = couponTemplateFactory.getTemplate(cart);
        return template.calculate(cart);
    }

    @Override
    public SimulationResponse simulateOrder(@RequestBody SimulationOrder order) {
        SimulationResponse simulationResponse = new SimulationResponse();
        Long minOrderPrice = Long.MAX_VALUE;

        // 计算每一个优惠券的订单价格
        for (CouponInfo couponInfo : order.getCouponInfos()) {
            ShoppingCart cart = new ShoppingCart();
            cart.setProducts(order.getProducts());
            cart.setCouponInfos(Lists.newArrayList(couponInfo));
            cart = couponTemplateFactory.getTemplate(cart).calculate(cart);

            Long couponId = couponInfo.getId();
            Long orderPrice = cart.getCost();
            simulationResponse.getCouponToOrderPrice().put(couponId, orderPrice);
            if (minOrderPrice > orderPrice) {
                simulationResponse.setBestCouponId(couponId);
                minOrderPrice = orderPrice;
            }
        }
        return simulationResponse;
    }
}
