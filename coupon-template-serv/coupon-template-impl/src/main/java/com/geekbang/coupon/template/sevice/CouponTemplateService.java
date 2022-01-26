package com.geekbang.coupon.template.sevice;


import com.geekbang.coupon.template.api.beans.CouponTemplateInfo;
import com.geekbang.coupon.template.api.beans.PagedCouponTemplateInfo;
import com.geekbang.coupon.template.api.beans.TemplateSearchParams;

import java.util.Collection;
import java.util.Map;

public interface CouponTemplateService {

    /**
     * 创建优惠券模板
     *
     * @param request
     * @return
     */
    CouponTemplateInfo createTemplate(CouponTemplateInfo request);

    /**
     * 克隆券模板
     *
     * @param templateId 模板id
     * @return
     */
    CouponTemplateInfo cloneTemplate(Long templateId);

    /**
     * 模板查询（分页）
     *
     * @param request
     * @return
     */
    PagedCouponTemplateInfo search(TemplateSearchParams request);

    /**
     * 通过模板ID查询优惠券模板
     *
     * @param id 模板id
     * @return
     */
    CouponTemplateInfo loadTemplateInfo(Long id);

    /**
     * 让优惠券模板无效
     *
     * @param id 模板id
     */
    void deleteTemplate(Long id);

    /**
     * Map是模板ID，key是模板详情 (批量查询)
     *
     * @param ids
     * @return
     */
    Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids);
}
