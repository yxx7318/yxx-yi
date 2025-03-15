/*
 Navicat Premium Dump SQL

 Source Server         : aliyunMysql1
 Source Server Type    : MySQL
 Source Server Version : 80300 (8.3.0)
 Source Host           : 8.134.237.56:3306
 Source Schema         : TenantA

 Target Server Type    : MySQL
 Target Server Version : 80300 (8.3.0)
 File Encoding         : 65001

 Date: 15/03/2025 20:28:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business
-- ----------------------------
DROP TABLE IF EXISTS `business`;
CREATE TABLE `business`  (
  `business_id` int NOT NULL AUTO_INCREMENT COMMENT '业务id',
  `detail` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '详情',
  PRIMARY KEY (`business_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of business
-- ----------------------------
INSERT INTO `business` VALUES (1, '测试数据');
INSERT INTO `business` VALUES (2, 'test');

SET FOREIGN_KEY_CHECKS = 1;
