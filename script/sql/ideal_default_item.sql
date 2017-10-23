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

 Date: 23/10/2017 16:10:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ideal_default_item
-- ----------------------------
DROP TABLE IF EXISTS `ideal_default_item`;
CREATE TABLE `ideal_default_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `default_rate` double DEFAULT NULL,
  `ideal_default_id` int(11) DEFAULT NULL,
  `life` int(11) DEFAULT NULL,
  `rank_code` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `rank_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX6unm1yp62guia6wt75dmlnf7y` (`ideal_default_id`)
) ENGINE=InnoDB AUTO_INCREMENT=221 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of ideal_default_item
-- ----------------------------
BEGIN;
INSERT INTO `ideal_default_item` VALUES (1, 0.0001, 1, 1, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (2, 0.00015, 1, 1, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (3, 0.00025, 1, 1, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (4, 0.0005, 1, 1, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (5, 0.0007, 1, 1, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (6, 0.001, 1, 1, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (7, 0.0015, 1, 1, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (8, 0.002, 1, 1, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (9, 0.003, 1, 1, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (10, 0.006, 1, 1, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (11, 0.0081, 1, 1, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (12, 0.01, 1, 1, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (13, 0.013, 1, 1, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (14, 0.02, 1, 1, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (15, 0.04, 1, 1, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (16, 0.06, 1, 1, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (17, 0.18, 1, 1, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (18, 0.26, 1, 1, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (19, 0.43, 1, 1, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (20, 0.61, 1, 1, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (21, 0.853, 1, 1, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (22, 1, 1, 1, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (23, 0.00011, 1, 2, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (24, 0.0002, 1, 2, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (25, 0.00045, 1, 2, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (26, 0.001, 1, 2, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (27, 0.0013, 1, 2, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (28, 0.002, 1, 2, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (29, 0.005, 1, 2, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (30, 0.0065, 1, 2, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (31, 0.01, 1, 2, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (32, 0.0145, 1, 2, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (33, 0.025, 1, 2, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (34, 0.03, 1, 2, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (35, 0.04, 1, 2, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (36, 0.06, 1, 2, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (37, 0.11, 1, 2, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (38, 0.17, 1, 2, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (39, 0.35, 1, 2, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (40, 0.47, 1, 2, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (41, 0.64, 1, 2, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (42, 0.8, 1, 2, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (43, 0.99, 1, 2, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (44, 1, 1, 2, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (45, 0.00027, 1, 3, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (46, 0.0006, 1, 3, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (47, 0.00118, 1, 3, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (48, 0.00222, 1, 3, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (49, 0.0027, 1, 3, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (50, 0.0035, 1, 3, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (51, 0.01, 1, 3, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (52, 0.0115, 1, 3, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (53, 0.0185, 1, 3, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (54, 0.0232, 1, 3, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (55, 0.0462, 1, 3, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (56, 0.054, 1, 3, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (57, 0.08, 1, 3, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (58, 0.12, 1, 3, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (59, 0.19, 1, 3, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (60, 0.28, 1, 3, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (61, 0.48, 1, 3, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (62, 0.62, 1, 3, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (63, 0.78, 1, 3, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (64, 0.9, 1, 3, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (65, 0.99, 1, 3, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (66, 1, 1, 3, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (67, 0.0005, 1, 4, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (68, 0.00105, 1, 4, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (69, 0.0019, 1, 4, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (70, 0.0045, 1, 4, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (71, 0.0053, 1, 4, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (72, 0.0065, 1, 4, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (73, 0.015, 1, 4, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (74, 0.0165, 1, 4, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (75, 0.03, 1, 4, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (76, 0.0347, 1, 4, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (77, 0.0653, 1, 4, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (78, 0.08, 1, 4, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (79, 0.12, 1, 4, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (80, 0.17, 1, 4, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (81, 0.26, 1, 4, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (82, 0.37, 1, 4, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (83, 0.56, 1, 4, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (84, 0.72, 1, 4, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (85, 0.87, 1, 4, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (86, 0.97, 1, 4, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (87, 0.99, 1, 4, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (88, 1, 1, 4, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (89, 0.0011, 1, 5, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (90, 0.002, 1, 5, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (91, 0.0029, 1, 5, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (92, 0.006, 1, 5, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (93, 0.007, 1, 5, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (94, 0.0085, 1, 5, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (95, 0.02, 1, 5, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (96, 0.0216, 1, 5, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (97, 0.039, 1, 5, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (98, 0.047, 1, 5, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (99, 0.0838, 1, 5, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (100, 0.11, 1, 5, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (101, 0.15, 1, 5, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (102, 0.21, 1, 5, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (103, 0.32, 1, 5, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (104, 0.43, 1, 5, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (105, 0.63, 1, 5, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (106, 0.78, 1, 5, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (107, 0.92, 1, 5, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (108, 0.99, 1, 5, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (109, 0.99, 1, 5, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (110, 1, 1, 5, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (111, 0.0025, 1, 6, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (112, 0.0035, 1, 6, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (113, 0.0044, 1, 6, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (114, 0.0075, 1, 6, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (115, 0.0085, 1, 6, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (116, 0.012, 1, 6, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (117, 0.024, 1, 6, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (118, 0.026, 1, 6, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (119, 0.046, 1, 6, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (120, 0.06, 1, 6, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (121, 0.1013, 1, 6, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (122, 0.13, 1, 6, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (123, 0.18, 1, 6, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (124, 0.26, 1, 6, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (125, 0.38, 1, 6, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (126, 0.49, 1, 6, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (127, 0.68, 1, 6, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (128, 0.83, 1, 6, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (129, 0.94, 1, 6, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (130, 0.99, 1, 6, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (131, 0.99, 1, 6, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (132, 1, 1, 6, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (133, 0.0035, 1, 7, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (134, 0.0045, 1, 7, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (135, 0.0054, 1, 7, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (136, 0.009, 1, 7, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (137, 0.01, 1, 7, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (138, 0.015, 1, 7, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (139, 0.027, 1, 7, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (140, 0.03, 1, 7, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (141, 0.052, 1, 7, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (142, 0.07, 1, 7, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (143, 0.1152, 1, 7, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (144, 0.15, 1, 7, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (145, 0.21, 1, 7, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (146, 0.3, 1, 7, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (147, 0.42, 1, 7, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (148, 0.53, 1, 7, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (149, 0.71, 1, 7, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (150, 0.87, 1, 7, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (151, 0.97, 1, 7, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (152, 0.99, 1, 7, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (153, 0.99, 1, 7, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (154, 1, 1, 7, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (155, 0.005, 1, 8, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (156, 0.006, 1, 8, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (157, 0.007, 1, 8, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (158, 0.011, 1, 8, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (159, 0.012, 1, 8, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (160, 0.018, 1, 8, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (161, 0.03, 1, 8, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (162, 0.035, 1, 8, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (163, 0.059, 1, 8, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (164, 0.082, 1, 8, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (165, 0.1279, 1, 8, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (166, 0.17, 1, 8, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (167, 0.23, 1, 8, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (168, 0.33, 1, 8, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (169, 0.46, 1, 8, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (170, 0.57, 1, 8, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (171, 0.735, 1, 8, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (172, 0.9, 1, 8, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (173, 0.99, 1, 8, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (174, 0.99, 1, 8, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (175, 0.99, 1, 8, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (176, 1, 1, 8, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (177, 0.0065, 1, 9, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (178, 0.0075, 1, 9, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (179, 0.0086, 1, 9, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (180, 0.013, 1, 9, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (181, 0.014, 1, 9, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (182, 0.02, 1, 9, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (183, 0.0345, 1, 9, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (184, 0.04, 1, 9, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (185, 0.067, 1, 9, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (186, 0.092, 1, 9, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (187, 0.1382, 1, 9, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (188, 0.18, 1, 9, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (189, 0.25, 1, 9, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (190, 0.36, 1, 9, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (191, 0.5, 1, 9, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (192, 0.61, 1, 9, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (193, 0.76, 1, 9, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (194, 0.91, 1, 9, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (195, 0.99, 1, 9, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (196, 0.99, 1, 9, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (197, 0.99, 1, 9, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (198, 1, 1, 9, 'D', 33);
INSERT INTO `ideal_default_item` VALUES (199, 0.008, 1, 10, 'AAA+', 5);
INSERT INTO `ideal_default_item` VALUES (200, 0.009, 1, 10, 'AAA', 8);
INSERT INTO `ideal_default_item` VALUES (201, 0.0104, 1, 10, 'AAA-', 11);
INSERT INTO `ideal_default_item` VALUES (202, 0.015, 1, 10, 'AA+', 13);
INSERT INTO `ideal_default_item` VALUES (203, 0.0175, 1, 10, 'AA', 15);
INSERT INTO `ideal_default_item` VALUES (204, 0.0235, 1, 10, 'AA-', 17);
INSERT INTO `ideal_default_item` VALUES (205, 0.038, 1, 10, 'A+', 18);
INSERT INTO `ideal_default_item` VALUES (206, 0.0452, 1, 10, 'A', 19);
INSERT INTO `ideal_default_item` VALUES (207, 0.0721, 1, 10, 'A-', 20);
INSERT INTO `ideal_default_item` VALUES (208, 0.1, 1, 10, 'BBB+', 21);
INSERT INTO `ideal_default_item` VALUES (209, 0.1462, 1, 10, 'BBB', 22);
INSERT INTO `ideal_default_item` VALUES (210, 0.2, 1, 10, 'BBB-', 23);
INSERT INTO `ideal_default_item` VALUES (211, 0.27, 1, 10, 'BB+', 24);
INSERT INTO `ideal_default_item` VALUES (212, 0.38, 1, 10, 'BB', 25);
INSERT INTO `ideal_default_item` VALUES (213, 0.53, 1, 10, 'BB-', 26);
INSERT INTO `ideal_default_item` VALUES (214, 0.64, 1, 10, 'B+', 27);
INSERT INTO `ideal_default_item` VALUES (215, 0.78, 1, 10, 'B', 28);
INSERT INTO `ideal_default_item` VALUES (216, 0.92, 1, 10, 'B-', 29);
INSERT INTO `ideal_default_item` VALUES (217, 0.99, 1, 10, 'CCC', 30);
INSERT INTO `ideal_default_item` VALUES (218, 0.99, 1, 10, 'CC', 31);
INSERT INTO `ideal_default_item` VALUES (219, 0.99, 1, 10, 'C', 32);
INSERT INTO `ideal_default_item` VALUES (220, 1, 1, 10, 'D', 33);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
