/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50713
 Source Host           : localhost:3306
 Source Schema         : lh

 Target Server Type    : MySQL
 Target Server Version : 50713
 File Encoding         : 65001

 Date: 23/10/2017 16:10:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ideal_default
-- ----------------------------
DROP TABLE IF EXISTS `ideal_default`;
CREATE TABLE `ideal_default` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `version` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of ideal_default
-- ----------------------------
BEGIN;
INSERT INTO `ideal_default` VALUES (1, '2017-09-19 17:55:13', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
