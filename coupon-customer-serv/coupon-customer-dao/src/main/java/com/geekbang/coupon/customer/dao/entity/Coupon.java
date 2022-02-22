package com.geekbang.coupon.customer.dao.entity;

import com.geekbang.coupon.customer.api.enums.CouponStatus;
import com.geekbang.coupon.customer.dao.convertor.CouponStatusConverter;
import com.geekbang.coupon.template.api.beans.CouponTemplateInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 对应的模板ID
    @Column(name = "template_id", nullable = false)
    private Long templateId;

    // 所有者的用户ID
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 冗余一个shop id方便查找
    @Column(name = "shop_id")
    private Long shopId;

    // 优惠券的使用/未使用状态
    @Column(name = "status", nullable = false)
    @Convert(converter = CouponStatusConverter.class)
    private CouponStatus status;

    // 被Transient标记的属性是不会被持久化的
    @Transient
    private CouponTemplateInfo templateInfo;

    @CreatedDate
    @Column(name = "created_time", nullable = false)
    private Date createdTime;
}
