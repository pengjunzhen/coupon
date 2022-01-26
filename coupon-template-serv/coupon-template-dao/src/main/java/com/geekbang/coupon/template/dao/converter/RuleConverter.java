package com.geekbang.coupon.template.dao.converter;

import com.alibaba.fastjson.JSON;
import com.geekbang.coupon.template.api.beans.rules.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class RuleConverter implements AttributeConverter<TemplateRule, String> {

    @Override
    public String convertToDatabaseColumn(TemplateRule templateRule) {
        return JSON.toJSONString(templateRule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String rule) {
        return JSON.parseObject(rule, TemplateRule.class);
    }
}
