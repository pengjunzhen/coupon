package com.geekbang.coupon.customer.service;

import com.geekbang.coupon.calculation.api.beans.ShoppingCart;
import com.geekbang.coupon.calculation.api.beans.SimulationOrder;
import com.geekbang.coupon.calculation.api.beans.SimulationResponse;
import com.geekbang.coupon.customer.api.beans.RequestCoupon;
import com.geekbang.coupon.customer.api.beans.SearchCoupon;
import com.geekbang.coupon.customer.dao.entity.Coupon;
import com.geekbang.coupon.template.api.beans.CouponInfo;

import java.util.List;

/**
 * 用户对接优惠券服务
 */
public interface CouponCustomerService {

    /**
     * 领券
     *
     * @param request
     * @return
     */
    Coupon requestCoupon(RequestCoupon request);

    /**
     * 核销优惠券
     *
     * @param shoppingCart
     * @return
     */
    ShoppingCart placeOrder(ShoppingCart shoppingCart);

    /**
     * 优惠券试算
     *
     * @param simulationOrder
     * @return
     */
    SimulationResponse simulateOrderPrice(SimulationOrder simulationOrder);

    /**
     * 删除优惠券 (逻辑删除)
     *
     * @param userId 用户id
     * @param couponId 优惠券id
     */
    void deleteCoupon(Long userId, Long couponId);

    /**
     * 查找用户优惠券
     *
     * @param request
     * @return
     */
    List<CouponInfo> findCoupon(SearchCoupon request);
}
