/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : geekbang_coupon_db

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 26/01/2022 17:18:10
*/
CREATE DATABASE IF NOT EXISTS geekbang_coupon_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `template_id` int(0) NOT NULL DEFAULT 0 COMMENT '主键',
  `user_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '拥有这张券的用户ID',
  `created_time` datetime(0) NOT NULL DEFAULT '2021-12-13 00:00:00' COMMENT '领券时间',
  `status` int(0) NOT NULL DEFAULT 0 COMMENT '优惠券的状态，比如未用，已用',
  `shop_id` bigint(0) NULL DEFAULT NULL COMMENT '冗余字段，方便查找',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_template_id`(`template_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '领到手的优惠券' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_template
-- ----------------------------
DROP TABLE IF EXISTS `coupon_template`;
CREATE TABLE `coupon_template`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `available` tinyint(1) NOT NULL DEFAULT 0 COMMENT '优惠券可用状态',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券详细信息',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券类型，比如满减、随机立减、晚间双倍等等',
  `shop_id` bigint(0) NULL DEFAULT NULL COMMENT '优惠券适用的门店，如果是空则代表全场适用',
  `created_time` datetime(0) NOT NULL DEFAULT '2021-12-13 00:00:00' COMMENT '创建时间',
  `rule` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '详细的使用规则',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop_id`(`shop_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券模板' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
