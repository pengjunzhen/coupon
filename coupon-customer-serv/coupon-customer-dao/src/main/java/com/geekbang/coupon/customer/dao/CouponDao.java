package com.geekbang.coupon.customer.dao;

import com.geekbang.coupon.customer.dao.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponDao extends JpaRepository<Coupon, Long> {

    /**
     * 统计用户从当前优惠券模板中领了多少张券
     *
     * @param userId 用户id
     * @param templateId 优惠券模板id
     * @return 领取的优惠券数量
     */
    Long countByUserIdAndTemplateId(Long userId, Long templateId);
}
