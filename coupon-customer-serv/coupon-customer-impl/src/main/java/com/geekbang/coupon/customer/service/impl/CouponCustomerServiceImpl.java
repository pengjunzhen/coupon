package com.geekbang.coupon.customer.service.impl;

import com.geekbang.coupon.calculation.api.beans.ShoppingCart;
import com.geekbang.coupon.calculation.api.beans.SimulationOrder;
import com.geekbang.coupon.calculation.api.beans.SimulationResponse;
import com.geekbang.coupon.customer.api.beans.RequestCoupon;
import com.geekbang.coupon.customer.api.beans.SearchCoupon;
import com.geekbang.coupon.customer.api.enums.CouponStatus;
import com.geekbang.coupon.customer.dao.CouponDao;
import com.geekbang.coupon.customer.dao.entity.Coupon;
import com.geekbang.coupon.customer.service.CouponConverter;
import com.geekbang.coupon.customer.service.CouponCustomerService;
import com.geekbang.coupon.template.api.beans.CouponInfo;
import com.geekbang.coupon.template.api.beans.CouponTemplateInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CouponCustomerServiceImpl implements CouponCustomerService {

    private final CouponDao couponDao;

    private final WebClient.Builder webClientBuilder;

    public CouponCustomerServiceImpl(CouponDao couponDao, WebClient.Builder webClientBuilder) {
        this.couponDao = couponDao;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Coupon requestCoupon(RequestCoupon request) {
        CouponTemplateInfo templateInfo =
                webClientBuilder.build()
                .get()
                .uri("http://coupon-template-serv/template/getTemplate?id=" + request.getCouponTemplateId())
                .retrieve()
                .bodyToMono(CouponTemplateInfo.class)
                .block();

        // 1、 从请求信息中获取对应模板id，查询对应的优惠券模板
        // 2、 如果模板不为空，则进行下一步，否则抛错返回
        // 3、 存在模板，则需要校验模板是否还在有效期内
        // 4、有些优惠券会限制领取数量，比如说有些只能用一张，有些可以领3张。所以需要查询当前用户领取了多少张该优惠券
        // 5、以上条件都没有问题的话，就可以开始通过优惠券模板生成对应的优惠券

        // 模板不存在则报错
        if (templateInfo == null) {
            log.error("invalid template id={}", request.getCouponTemplateId());
            throw new IllegalArgumentException("Invalid template id");
        }

        // 模板不能过期
        long now = Calendar.getInstance().getTimeInMillis();
        Long expTime = templateInfo.getRule().getDeadline();
        if (expTime != null && now >= expTime || BooleanUtils.isFalse(templateInfo.getAvailable())) {
            log.error("template is not available id={}", request.getCouponTemplateId());
            throw new IllegalArgumentException("template is unavailable");
        }

        // 用户领券数量超过上限
        Long count = couponDao.countByUserIdAndTemplateId(request.getUserId(), request.getCouponTemplateId());
        if (count >= templateInfo.getRule().getLimitation()) {
            log.error("exceeds maximum number");
            throw new IllegalArgumentException("exceeds maximum number");
        }

        Coupon coupon = Coupon.builder()
                .templateId(request.getCouponTemplateId())
                .userId(request.getUserId())
                .shopId(templateInfo.getShopId())
                .status(CouponStatus.AVAILABLE)
                .build();
        couponDao.save(coupon);
        return coupon;
    }

    @Transactional
    @Override
    public ShoppingCart placeOrder(ShoppingCart shoppingCart) {
        if (CollectionUtils.isEmpty(shoppingCart.getProducts())) {
            log.error("invalid check out request, order={}", shoppingCart);
            throw new IllegalArgumentException("cart if empty");
        }

        Coupon coupon = null;
        if (shoppingCart.getCouponId() != null) {
            // 如果有优惠券，验证是否可用，并且是当前客户的
            Coupon example = Coupon.builder()
                    .userId(shoppingCart.getUserId())
                    .id(shoppingCart.getCouponId())
                    .status(CouponStatus.AVAILABLE)
                    .build();
            coupon = couponDao.findAll(Example.of(example))
                    .stream()
                    .findFirst()
                    // 如果找不到券，就抛出异常
                    .orElseThrow(() -> new RuntimeException("Coupon not found"));

            CouponInfo couponInfo = CouponConverter.convertToCouponInfo(coupon);
            couponInfo.setTemplate(loadTemplateInfo(coupon.getTemplateId()));
            shoppingCart.setCouponInfos(Lists.newArrayList(couponInfo));
        }

        // order清算
        ShoppingCart checkoutInfo = webClientBuilder.build().post()
                .uri("http://coupon-calculation-serv/calculator/checkout")
                .bodyValue(shoppingCart)
                .retrieve()
                .bodyToMono(ShoppingCart.class)
                .block();

        if (coupon != null) {
            // 如果优惠券没有被结算掉，而用户传递了优惠券，报错提示该订单满足不了优惠条件
            if (CollectionUtils.isEmpty(checkoutInfo.getCouponInfos())) {
                log.error("cannot apply coupon to order, couponId={}", coupon.getId());
                throw new IllegalArgumentException("coupon is not applicable to this order");
            }

            log.info("update coupon status to used, couponId={}", coupon.getId());
            coupon.setStatus(CouponStatus.USED);
            couponDao.save(coupon);
        }

        return checkoutInfo;
    }

    private CouponTemplateInfo loadTemplateInfo(Long templateId) {
        return webClientBuilder.build().get()
                .uri("http://coupon-template-serv/template/getTemplate?id=" + templateId)
                .retrieve()
                .bodyToMono(CouponTemplateInfo.class)
                .block();
    }

    @Override
    public SimulationResponse simulateOrderPrice(SimulationOrder simulationOrder) {
        List<CouponInfo> couponInfos = Lists.newArrayList();
        // 挨个循环，把优惠券信息加载出来
        // 高并发场景下不能这么一个个循环，更好的做法是批量查询
        // 而且券模板一旦创建不会改内容，所以在创建端做数据异构放到缓存里，使用端从缓存捞template信息
        for (Long couponId : simulationOrder.getCouponIDs()) {
            Coupon example = Coupon.builder()
                    .userId(simulationOrder.getUserId())
                    .id(couponId)
                    .status(CouponStatus.AVAILABLE)
                    .build();

            Optional<Coupon> couponOptional = couponDao.findAll(Example.of(example))
                    .stream().findFirst();
            // 加载优惠券信息
            if (couponOptional.isPresent()) {
                Coupon coupon = couponOptional.get();
                CouponInfo couponInfo = CouponConverter.convertToCouponInfo(coupon);
                couponInfo.setTemplate(loadTemplateInfo(coupon.getTemplateId()));
                couponInfos.add(couponInfo);
            }
        }
        simulationOrder.setCouponInfos(couponInfos);

        // 调用接口试算服务
        return webClientBuilder.build().post()
                .uri("http://coupon-calculation-serv/calculator/simulate")
                .bodyValue(simulationOrder)
                .retrieve()
                .bodyToMono(SimulationResponse.class)
                .block();
    }

    @Override
    public void deleteCoupon(Long userId, Long couponId) {
        Coupon example = Coupon.builder()
                .userId(userId)
                .id(couponId)
                .status(CouponStatus.AVAILABLE)
                .build();
        Coupon coupon = couponDao.findAll(Example.of(example))
                .stream()
                .findFirst()
                // 如果找不到券，就抛出异常
                .orElseThrow(() -> new RuntimeException("Could not find available coupon"));

        coupon.setStatus(CouponStatus.INACTIVE);
        couponDao.save(coupon);
    }

    @Override
    public List<CouponInfo> findCoupon(SearchCoupon request) {
        // 在真正的生产环境，这个接口需要做分页查询，并且查询条件要封装成一个类
        Coupon example = Coupon.builder()
                .userId(request.getUserId())
                .status(CouponStatus.convert(request.getCouponStatus()))
                .shopId(request.getShopId())
                .build();

        // 这里你可以尝试实现分页查询
        List<Coupon> coupons = couponDao.findAll(Example.of(example));
        if (coupons.isEmpty()) {
            return Lists.newArrayList();
        }

        List<Long> templateIds = coupons.stream()
                .map(Coupon::getTemplateId)
                .collect(Collectors.toList());
        Map<Long, CouponTemplateInfo> templateMap = webClientBuilder.build().get()
                .uri("http://coupon-template-serv/template/getBatch?ids=" + templateIds)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<Long, CouponTemplateInfo>>() {})
                .block();
        coupons.forEach(e -> e.setTemplateInfo(templateMap.get(e.getTemplateId())));

        return coupons.stream()
                .map(CouponConverter::convertToCouponInfo)
                .collect(Collectors.toList());
    }
}
