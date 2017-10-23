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

 Date: 23/10/2017 16:10:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `is_del` bit(1) NOT NULL,
  `is_valid` bit(1) NOT NULL,
  `param_code` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `param_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `param_order` int(11) DEFAULT NULL,
  `param_value` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `version` double DEFAULT NULL,
  `version_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXpif2tlsh3dovx3yrq5tuqbv7k` (`param_code`),
  KEY `IDX8stxacr3n0m3pl5vl1sgqc2ga` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of sys_dictionary
-- ----------------------------
BEGIN;
INSERT INTO `sys_dictionary` VALUES (1, '2017-09-19 15:23:58', b'0', b'1', 'GUARANTEE_MODE', '担保方式', NULL, NULL, NULL, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (2, '2017-09-19 15:23:58', b'0', b'1', 'CREDIT_LEVEL', '信用级别', NULL, NULL, NULL, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (3, '2017-09-19 15:23:58', b'0', b'1', 'ORG_TYPE', '机构类型', NULL, NULL, NULL, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (4, '2017-09-19 15:24:17', b'0', b'1', 'credit_loan', '信用贷款', 1, '1', 1, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (5, '2017-09-19 15:24:17', b'0', b'1', 'AAA+', NULL, 1, '1', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (6, '2017-09-19 15:24:17', b'0', b'1', 'zcx', '政策性银行', 1, '1', 3, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (7, '2017-09-19 15:24:17', b'0', b'1', 'guarantee', '保证担保', 2, '2', 1, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (8, '2017-09-19 15:24:17', b'0', b'1', 'AAA', NULL, 2, '2', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (9, '2017-09-19 15:24:17', b'0', b'1', 'gysy', '国有商业银行', 2, '2', 3, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (10, '2017-09-19 15:24:17', b'0', b'1', 'mortgage', '抵押贷款', 3, '3', 1, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (11, '2017-09-19 15:24:17', b'0', b'1', 'AAA-', NULL, 3, '3', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (12, '2017-09-19 15:24:17', b'0', b'1', 'gfzsy', '股份制商业银行', 3, '3', 3, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (13, '2017-09-19 15:24:17', b'0', b'1', 'AA+', NULL, 4, '4', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (14, '2017-09-19 15:24:17', b'0', b'1', 'dfsyjwz', '地方商业银行及外资行', 4, '4', 3, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (15, '2017-09-19 15:24:17', b'0', b'1', 'AA', NULL, 5, '5', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (16, '2017-09-19 15:24:17', b'0', b'1', 'other', '其他', 5, '5', 3, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (17, '2017-09-19 15:24:17', b'0', b'1', 'AA-', NULL, 6, '6', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (18, '2017-09-19 15:24:17', b'0', b'1', 'A+', NULL, 7, '7', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (19, '2017-09-19 15:24:17', b'0', b'1', 'A', NULL, 8, '8', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (20, '2017-09-19 15:24:17', b'0', b'1', 'A-', NULL, 9, '9', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (21, '2017-09-19 15:24:17', b'0', b'1', 'BBB+', NULL, 10, '10', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (22, '2017-09-19 15:24:17', b'0', b'1', 'BBB', NULL, 11, '11', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (23, '2017-09-19 15:24:17', b'0', b'1', 'BBB-', NULL, 12, '12', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (24, '2017-09-19 15:24:17', b'0', b'1', 'BB+', NULL, 13, '13', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (25, '2017-09-19 15:24:17', b'0', b'1', 'BB', NULL, 14, '14', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (26, '2017-09-19 15:24:17', b'0', b'1', 'BB-', NULL, 15, '15', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (27, '2017-09-19 15:24:17', b'0', b'1', 'B+', NULL, 16, '16', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (28, '2017-09-19 15:24:17', b'0', b'1', 'B', NULL, 17, '17', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (29, '2017-09-19 15:24:17', b'0', b'1', 'B-', NULL, 18, '18', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (30, '2017-09-19 15:24:17', b'0', b'1', 'CCC', NULL, 19, '19', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (31, '2017-09-19 15:24:17', b'0', b'1', 'CC', NULL, 20, '20', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (32, '2017-09-19 15:24:17', b'0', b'1', 'C', NULL, 21, '21', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (33, '2017-09-19 15:26:18', b'0', b'1', 'D', NULL, 22, '22', 2, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (34, '2017-09-19 15:46:51', b'0', b'1', 'BASE_COEFFICIENT', '资产基准相关系数', NULL, NULL, NULL, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (35, '2017-09-19 15:46:51', b'0', b'1', 'sameArea_sameIndustry', '相同地区、相同行业', NULL, '0.4', 34, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (36, '2017-09-19 15:46:51', b'0', b'1', 'sameArea_diffIndustry', '相同地区、不同行业', NULL, '0.25', 34, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (37, '2017-09-19 15:46:51', b'0', b'1', 'diffArea_sameIndustry', '不同地区、相同行业', NULL, '0.35', 34, 1, NULL);
INSERT INTO `sys_dictionary` VALUES (38, '2017-09-19 15:46:51', b'0', b'1', 'diffArea_diffIndustry', '不同地区、不同行业', NULL, '0.1', 34, 1, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
