# 极客时间《Spring Cloud微服务项目实战》-个人实践

[原仓库地址](https://gitee.com/banxian-yao/geekbang-coupon-center)

项目结构：
- coupon-template-serv： 创建、查找、克隆、删除优惠券模板；
- coupon-calculation-serv：计算优惠后的订单价格、试算每个优惠券的优惠幅度；
- coupon-customer-serv：通过调用 template 和 calculation 服务，实现用户领取优惠券、模拟计算最优惠的券、删除优惠券、下订单等操作；
- middleware：存放一些与业务无关的平台类组件。