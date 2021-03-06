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

 Date: 23/10/2017 16:10:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `valid` bit(1) NOT NULL DEFAULT b'1',
  `access_token` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `display_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `init_password` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=254 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, '2017-10-13 14:52:37', '2017-10-23 14:56:57', '041C57C7A07208702C6AC99BCDA0DFEA', 'admin', b'1', 'EBD243899D908AA38C7B2669ABDCCB8E', 'admin', 'wsed#21');
INSERT INTO `user` VALUES (2, '2017-10-23 14:51:25', '2017-10-23 14:58:13', '06E9B90C1BB9BB3D47855719658E4E58', 'wujinshan', b'1', '0348F473602FAB66B376149941321CA5', '吴金善', '118#ct0-');
INSERT INTO `user` VALUES (3, '2017-10-23 14:51:25', NULL, '66D0DC0E7B0C7F82FA4F6A1BBBF5A933', 'wanhuawei', b'1', NULL, '万华伟', 'at%cic5r');
INSERT INTO `user` VALUES (4, '2017-10-23 14:51:25', NULL, '4293EFBDABE3893E7C3560B48F51A791', 'zhouxiaohui', b'1', NULL, '周晓辉', 'v82aix-g');
INSERT INTO `user` VALUES (5, '2017-10-23 14:51:25', NULL, 'DD1D756E205C4A3F8670535B4E79EAE6', 'changlijuan', b'1', NULL, '常丽娟', 'jj&49i&a');
INSERT INTO `user` VALUES (6, '2017-10-23 14:51:25', NULL, '7709DA10DA41B405BD79066107D8AEDB', 'airenzhi', b'1', NULL, '艾仁智', '7w303g5k');
INSERT INTO `user` VALUES (7, '2017-10-23 14:51:25', NULL, '4C2BB25965933F93A0AB8D40BFC3DCCA', 'liuhongtao', b'1', NULL, '刘洪涛', 'nw18zlr8');
INSERT INTO `user` VALUES (8, '2017-10-23 14:51:25', NULL, 'D4F55A12F9240028FF22967E02944EC5', 'liuxiaoliang', b'1', NULL, '刘晓亮', 'fg$1nqf7');
INSERT INTO `user` VALUES (9, '2017-10-23 14:51:25', NULL, '58BC09DF4FE152B05F4A2F29969186A9', 'zhoukui', b'1', NULL, '周馗', 'e7k687gx');
INSERT INTO `user` VALUES (10, '2017-10-23 14:51:25', NULL, 'D6CD60A422AEFD8AF0CF78D23B360285', 'helina', b'1', NULL, '何丽娜', 'aud83k2r');
INSERT INTO `user` VALUES (11, '2017-10-23 14:51:25', NULL, 'B06A21AEFD7F4AAE7010CFD4A8DD53E0', 'lijing', b'1', NULL, '李晶', 'in8&odl0');
INSERT INTO `user` VALUES (12, '2017-10-23 14:51:25', NULL, 'A759C30F4D22DCB6CC1A4A3A91CB6ECE', 'zhangxue', b'1', NULL, '张雪', '22uy$k6w');
INSERT INTO `user` VALUES (13, '2017-10-23 14:51:25', NULL, '31E23F17F539AFEA985BF997A0E8DA79', 'zhangjingxi', b'1', NULL, '张婧茜', 'djvz7142');
INSERT INTO `user` VALUES (14, '2017-10-23 14:51:25', NULL, '52813DEC0D5440716B7F6404D998D370', 'liuyali', b'1', NULL, '刘亚利', '6e4txp9z');
INSERT INTO `user` VALUES (15, '2017-10-23 14:51:25', NULL, '5A97BC1D95498C8ED30A7802DBCF8FD7', 'wangjinlei', b'1', NULL, '王金磊', 'r9#88tt7');
INSERT INTO `user` VALUES (16, '2017-10-23 14:51:25', NULL, 'F1ECC447693B16EA18716C9B92E1AF3D', 'yangting', b'1', NULL, '杨婷', '9-mdu035');
INSERT INTO `user` VALUES (17, '2017-10-23 14:51:25', NULL, '24A2F88CD43854083A16F76B3B23E032', 'zhangshuang', b'1', NULL, '张爽', 'zwmadk8w');
INSERT INTO `user` VALUES (18, '2017-10-23 14:51:25', NULL, 'D91A6D908EF38B73A855C10EFCC8E16E', 'gaochaoqun', b'1', NULL, '高朝群', '4lli0zxw');
INSERT INTO `user` VALUES (19, '2017-10-23 14:51:25', NULL, 'B6B6402635DDA29E173C3537E6D7B151', 'yindan', b'1', NULL, '尹丹', 'lp-zi%mv');
INSERT INTO `user` VALUES (20, '2017-10-23 14:51:25', NULL, '34D63612BF2D4C60F80BD86646DFEC2F', 'huangxuming', b'1', NULL, '黄旭明', '$rgpwg%u');
INSERT INTO `user` VALUES (21, '2017-10-23 14:51:25', NULL, 'B759EEC6405CDFFD0F71DAA2D572B71B', 'zhangjingjing', b'1', NULL, '张晶晶', 's8p&955i');
INSERT INTO `user` VALUES (22, '2017-10-23 14:51:25', NULL, 'B4F104B840DB1504705F3033F72940F7', 'jiangrong', b'1', NULL, '姜荣', '&wbohmik');
INSERT INTO `user` VALUES (23, '2017-10-23 14:51:25', NULL, 'C35381B575C87C7B9AB46D40550F81DF', 'niuhengxing', b'1', NULL, '牛恒星', 'nfvalxyn');
INSERT INTO `user` VALUES (24, '2017-10-23 14:51:25', NULL, '8D15DA4AC990DE473188236075E1B7FE', 'nixin', b'1', NULL, '倪昕', 'uogcwe2e');
INSERT INTO `user` VALUES (25, '2017-10-23 14:51:25', NULL, '84414935F11CAE92DB050B87EADEBEEF', 'cuiying', b'1', NULL, '崔莹', 'g0ai5x6p');
INSERT INTO `user` VALUES (26, '2017-10-23 14:51:25', NULL, '9ACAA38182673A06310FA13C8861171A', 'linaipeng', b'1', NULL, '李乃鹏', 'x0ku3kc2');
INSERT INTO `user` VALUES (27, '2017-10-23 14:51:25', NULL, 'DFB09D85426791FABA55F44380FB2F05', 'xuhuifeng', b'1', NULL, '徐汇丰', 'oycdt1i1');
INSERT INTO `user` VALUES (28, '2017-10-23 14:51:25', NULL, '55A876387FA6573C19702E67AE3B2644', 'yuanlin', b'1', NULL, '袁琳', '4c#0l6u!');
INSERT INTO `user` VALUES (29, '2017-10-23 14:51:25', NULL, 'CC80946FD4F27F18DAB252AA30A3D38E', 'haoyagang', b'1', NULL, '郝亚钢', '5on$!pq2');
INSERT INTO `user` VALUES (30, '2017-10-23 14:51:25', NULL, '9812D259832A68CF5153DC7104329303', 'zhangwei', b'1', NULL, '张蔚', '3#qu66!j');
INSERT INTO `user` VALUES (31, '2017-10-23 14:51:25', NULL, 'D4588750433629BB001D4B212E30BC9C', 'caojie', b'1', NULL, '曹洁', 'txi2vn1!');
INSERT INTO `user` VALUES (32, '2017-10-23 14:51:25', NULL, '7EAA315633CC625F8490C67634AF20BE', 'fenglei', b'1', NULL, '冯磊', 'tz33i0o-');
INSERT INTO `user` VALUES (33, '2017-10-23 14:51:25', NULL, '4EA4F6B9C86E19C6217248AB0937E586', 'wanganna', b'1', NULL, '王安娜', '5$wuiikn');
INSERT INTO `user` VALUES (34, '2017-10-23 14:51:25', NULL, '0954352211FF7E6C7C73054FE57D54D9', 'liuwei', b'1', NULL, '刘薇', 'vhl3#rsr');
INSERT INTO `user` VALUES (35, '2017-10-23 14:51:25', NULL, 'A8060C07DDE6F0AA5C094C863E4BB003', 'lilei', b'1', NULL, '李镭', 'sqteocu2');
INSERT INTO `user` VALUES (36, '2017-10-23 14:51:25', NULL, '6ED796464BFDB38BE67484E756C66679', 'houzhenzhen', b'1', NULL, '候珍珍', 'spbg427e');
INSERT INTO `user` VALUES (37, '2017-10-23 14:51:25', NULL, '14F2C38DF670AAC1337E62D8C7C952B6', 'zhiyamei', b'1', NULL, '支亚梅', 'zk2848js');
INSERT INTO `user` VALUES (38, '2017-10-23 14:51:25', NULL, '8E1CF25B1BD426DABD5E5F97532B0BEB', 'yanxin', b'1', NULL, '闫欣', 'eae9ylz5');
INSERT INTO `user` VALUES (39, '2017-10-23 14:51:25', NULL, 'A1995D14B32EA2458EEA956D64DC7BF3', 'wangjinqu', b'1', NULL, '王进取', '$$i$4-!8');
INSERT INTO `user` VALUES (40, '2017-10-23 14:51:25', NULL, '0974FD3461E093FD11F3187819DA4E3D', 'zhangzhaoxin', b'1', NULL, '张兆新', '0xsyg6wh');
INSERT INTO `user` VALUES (41, '2017-10-23 14:51:25', NULL, '9525D1FD03E85113D792057D7E23FBA0', 'luoxingchi', b'1', NULL, '罗星驰', 'fekijhdi');
INSERT INTO `user` VALUES (42, '2017-10-23 14:51:25', NULL, 'B740D85A37B23E52F008C020D2FA256F', 'chenye', b'1', NULL, '陈叶', '1hx07v2e');
INSERT INTO `user` VALUES (43, '2017-10-23 14:51:25', NULL, '8A1AF77ABF840C48B9741CC7E9B631E9', 'licong', b'1', NULL, '李聪', '7uznj&y#');
INSERT INTO `user` VALUES (44, '2017-10-23 14:51:25', NULL, 'B2DF679F3F135BEA165B2D6730E7919E', 'liyafeng', b'1', NULL, '李亚烽', '!t%x32n$');
INSERT INTO `user` VALUES (45, '2017-10-23 14:51:25', NULL, '35FC6F59FEE1A42F305778A2A952129A', 'liupan', b'1', NULL, '刘潘', '2oxkqrv&');
INSERT INTO `user` VALUES (46, '2017-10-23 14:51:25', NULL, 'F2069266A353EB245413DCDF275FE04F', 'zhoukai', b'1', NULL, '周凯', 'cx1g6%ju');
INSERT INTO `user` VALUES (47, '2017-10-23 14:51:25', NULL, 'D5C21625E5D90BE3AA5D5EC8D114AE09', 'zhangjinyao', b'1', NULL, '张瑾瑶', 'nl4u6!-j');
INSERT INTO `user` VALUES (48, '2017-10-23 14:51:25', NULL, 'BDC053533E75CC51DECFCA861DE0A904', 'caomengru', b'1', NULL, '曹梦茹', '7a668wl1');
INSERT INTO `user` VALUES (49, '2017-10-23 14:51:25', NULL, '34CC3DE1FC2AF8A024A9677B81E8FF4B', 'litongbin', b'1', NULL, '李同斌', 'undhya3&');
INSERT INTO `user` VALUES (50, '2017-10-23 14:51:25', NULL, 'CB4839425374EED5CA0644BD525146E0', 'fuhao', b'1', NULL, '付昊', '6ctpca$%');
INSERT INTO `user` VALUES (51, '2017-10-23 14:51:25', NULL, '65B6519EEE65E790C0484762EC20C2A1', 'lurui', b'1', NULL, '卢瑞', 'yuv10zxl');
INSERT INTO `user` VALUES (52, '2017-10-23 14:51:25', NULL, 'E4DE3E1EB7840A673E2FC058C6AC27D2', 'yangye', b'1', NULL, '杨野', '8d5y$m3%');
INSERT INTO `user` VALUES (53, '2017-10-23 14:51:25', NULL, '7F67596855AA3355E15D90EAFEEBF22A', 'wangyutong', b'1', NULL, '王煜彤', '-9-6pcwz');
INSERT INTO `user` VALUES (54, '2017-10-23 14:51:25', NULL, '8AB80BD2FDBC7304E2C82C5897DB8DB3', 'yangshilong', b'1', NULL, '杨世龙', 'af5cd6m3');
INSERT INTO `user` VALUES (55, '2017-10-23 14:51:25', NULL, '453CC8AB307633EDECE93176F79AAE78', 'wangyue', b'1', NULL, '王越', '$c&ari9!');
INSERT INTO `user` VALUES (56, '2017-10-23 14:51:25', NULL, 'DF6C16D5A846B52EE8B8EB54475B2362', 'zhouting', b'1', NULL, '周婷', '5o1pn186');
INSERT INTO `user` VALUES (57, '2017-10-23 14:51:25', NULL, 'DA25BFD2041B6324E44E636CE2109D7B', 'renguiyong', b'1', NULL, '任贵永', 'mhcsxqoq');
INSERT INTO `user` VALUES (58, '2017-10-23 14:51:25', NULL, '8DEFAB2B380B5D163E008BDB197618CA', 'lvliang', b'1', NULL, '吕梁', '4hoti62x');
INSERT INTO `user` VALUES (59, '2017-10-23 14:51:25', NULL, '22D0C63CD68323151E013E94BE315D94', 'yuejun', b'1', NULL, '岳俊', '$mog3o&p');
INSERT INTO `user` VALUES (60, '2017-10-23 14:51:25', NULL, 'CC27BD24EDC332FD88342A0C77D186D3', 'zhangjun', b'1', NULL, '张军', 'lrc$%1xy');
INSERT INTO `user` VALUES (61, '2017-10-23 14:51:25', NULL, 'D538661A5EFBAB2D7023A8A6E7C17532', 'renjisong', b'1', NULL, '任霁松', 's#426ry3');
INSERT INTO `user` VALUES (62, '2017-10-23 14:51:25', NULL, '55DD161991F037C5182B01FAFED3BBF8', 'xuyiyan', b'1', NULL, '徐益言', 'jzv&#dux');
INSERT INTO `user` VALUES (63, '2017-10-23 14:51:25', NULL, 'EE328524943C468146C24ABF5282B7FD', 'yuruijuan', b'1', NULL, '余瑞娟', '6%0b3-#m');
INSERT INTO `user` VALUES (64, '2017-10-23 14:51:25', NULL, '557E078AE9E9A1CD1BAE22990D3901C0', 'linfang', b'1', NULL, '林芳', 'dgyxe505');
INSERT INTO `user` VALUES (65, '2017-10-23 14:51:25', NULL, '843304444F5254498BA42397A7C2DF69', 'liying', b'1', NULL, '李莹', 't0kdi91o');
INSERT INTO `user` VALUES (66, '2017-10-23 14:51:25', NULL, 'AD258D85DE2F96485AF12B4FC5759C7F', 'lulu', b'1', NULL, '陆璐', 'vcdbhbpb');
INSERT INTO `user` VALUES (67, '2017-10-23 14:51:25', NULL, 'C59DD16C86C02C052E344FE1A6FF9594', 'zhanglei', b'1', NULL, '张磊', 'w4kl9fq%');
INSERT INTO `user` VALUES (68, '2017-10-23 14:51:25', NULL, '221C00E2537EA2016C300BD6B7F40771', 'fansi', b'1', NULL, '樊思', '7yykcqf4');
INSERT INTO `user` VALUES (69, '2017-10-23 14:51:25', NULL, '792BD5D7307351064298B3CFF0FE3693', 'zhoukexin', b'1', NULL, '周珂鑫', 'b!!ky0fg');
INSERT INTO `user` VALUES (70, '2017-10-23 14:51:25', NULL, 'C3409F9EB808C5AAB9DA62D69448D5E2', 'wangxiao', b'1', NULL, '王潇', '1&#cvj-w');
INSERT INTO `user` VALUES (71, '2017-10-23 14:51:25', NULL, 'C905B391981705248EADC03E44D82692', 'gongyi', b'1', NULL, '龚艺', 'f$hz48at');
INSERT INTO `user` VALUES (72, '2017-10-23 14:51:25', NULL, '66EBA438F7B2FAC9087941CACF7984F7', 'wangxiaopeng', b'1', NULL, '王晓鹏', 'vq0%-ez3');
INSERT INTO `user` VALUES (73, '2017-10-23 14:51:25', NULL, 'B3A5241B9DEC05AA1DC0865BB4EBAE3D', 'wangwenyan', b'1', NULL, '王文燕', 'xu3l%bjs');
INSERT INTO `user` VALUES (74, '2017-10-23 14:51:25', NULL, '731EF73E4AF9BD103EA7469180F188D6', 'yutongkun', b'1', NULL, '于彤昆', 'j0521h0g');
INSERT INTO `user` VALUES (75, '2017-10-23 14:51:25', NULL, 'DA27FA19601A6BB754EEFA8B7AC3605D', 'suhaiyun', b'1', NULL, '苏海云', 'h0jevjxw');
INSERT INTO `user` VALUES (76, '2017-10-23 14:51:25', NULL, '83B9E522FB76AD9817650BA5F05ED611', 'gaopeng', b'1', NULL, '高鹏', '57$fwelu');
INSERT INTO `user` VALUES (77, '2017-10-23 14:51:25', NULL, '65151007FFE301C06C50D4DA30082949', 'yeweiwu', b'1', NULL, '叶维武', 't!39ciyi');
INSERT INTO `user` VALUES (78, '2017-10-23 14:51:25', NULL, '61DB0E90E124C2F29F9F585D0577DEF1', 'weiyin', b'1', NULL, '魏音', 'vkl5m54$');
INSERT INTO `user` VALUES (79, '2017-10-23 14:51:25', NULL, '213FF01259C62049B75E0616810A5911', 'tangyuli', b'1', NULL, '唐玉丽', '2zbgne0c');
INSERT INTO `user` VALUES (80, '2017-10-23 14:51:25', NULL, 'A1C328232F3A720E2DBAB3207EC22ACA', 'sunlinlin', b'1', NULL, '孙林林', '-7no17io');
INSERT INTO `user` VALUES (81, '2017-10-23 14:51:25', NULL, 'BA68752D6982A9F3BDA1BB9AEA5506D6', 'wangsuping', b'1', NULL, '王素平', 'jqzhjq68');
INSERT INTO `user` VALUES (82, '2017-10-23 14:51:25', NULL, '514585D1A16223E7C45884ED963853A6', 'ninglijie', b'1', NULL, '宁立杰', 'k16jvnw5');
INSERT INTO `user` VALUES (83, '2017-10-23 14:51:25', NULL, '3F3EDBE63D3516780A136BABE18C72D5', 'puyaxiu', b'1', NULL, '蒲雅修', '8m1a7plp');
INSERT INTO `user` VALUES (84, '2017-10-23 14:51:25', NULL, '9FBA3CF947D14DD2ED28D6AF879A717D', 'luoqiao', b'1', NULL, '罗峤', 'w1%bj8m9');
INSERT INTO `user` VALUES (85, '2017-10-23 14:51:25', NULL, 'B8B09571A7F8BF373D909545D9E57F87', 'fanrui', b'1', NULL, '范瑞', 'fw%pv#go');
INSERT INTO `user` VALUES (86, '2017-10-23 14:51:25', NULL, 'C08428D39BBF830859DC33A0C4C8213D', 'daifeiyi', b'1', NULL, '戴非易', 'wwg227dh');
INSERT INTO `user` VALUES (87, '2017-10-23 14:51:25', NULL, '38C273E6A479293F28B6AF74270B4285', 'dujian', b'1', NULL, '杜渐', '&p2z4#wx');
INSERT INTO `user` VALUES (88, '2017-10-23 14:51:25', NULL, 'FBBACAA6F987C83AB8F840FE0D905349', 'kangganhua', b'1', NULL, '康赣华', 'b!ed$gpk');
INSERT INTO `user` VALUES (89, '2017-10-23 14:51:25', NULL, 'CA46949FC44AD16F8E25798367395261', 'hewenjue', b'1', NULL, '何雯珏', 'e%&hbr4a');
INSERT INTO `user` VALUES (90, '2017-10-23 14:51:25', NULL, '3EB54EB70E5141D4F7893AB8D6FD183B', 'wangjingyu', b'1', NULL, '王婧瑜', 'za-gwb5i');
INSERT INTO `user` VALUES (91, '2017-10-23 14:51:25', NULL, '1B0771648E8CF0CF6DFA03C6E0F3F133', 'fanqin', b'1', NULL, '范琴', 'wsmq-5!x');
INSERT INTO `user` VALUES (92, '2017-10-23 14:51:25', NULL, '95A2342F0B105836EC39F0883F5B4CEC', 'likun', b'1', NULL, '李昆', '8&$cikgk');
INSERT INTO `user` VALUES (93, '2017-10-23 14:51:25', NULL, 'BFE12B697D669063051F37E484DF71E5', 'bichong', b'1', NULL, '毕冲', '!zczbo3u');
INSERT INTO `user` VALUES (94, '2017-10-23 14:51:25', NULL, '2ADB0A9A77DA399A0A9DE0F575451F3C', 'litong', b'1', NULL, '李彤', '%dnjhnc3');
INSERT INTO `user` VALUES (95, '2017-10-23 14:51:25', NULL, 'A8D72F2492ECF96EF4F093C790BFDE9F', 'gaojiayue', b'1', NULL, '高佳悦', 'w-#e8g3k');
INSERT INTO `user` VALUES (96, '2017-10-23 14:51:25', NULL, '290DA28B79FEA6A63F759E1F880E3D66', 'lijingyun', b'1', NULL, '李敬云', '-j1l53sm');
INSERT INTO `user` VALUES (97, '2017-10-23 14:51:25', NULL, 'B7BDF040EEE7A8F312901BEE76C6191A', 'liukedong', b'1', NULL, '刘克东', '9mv3elu1');
INSERT INTO `user` VALUES (98, '2017-10-23 14:51:25', NULL, 'F07249F60BDDF514A0E74245B502E727', 'zhangyi', b'1', NULL, '张祎', '2m90wx37');
INSERT INTO `user` VALUES (99, '2017-10-23 14:51:25', NULL, '0E59C95093CCD86F8892E0EC45251166', 'chenning', b'1', NULL, '陈凝', 'p894hace');
INSERT INTO `user` VALUES (100, '2017-10-23 14:51:25', NULL, '13E8B87EE4C2C363EA2F34355DFF08BE', 'qinchao', b'1', NULL, '秦超', '9irgyiyv');
INSERT INTO `user` VALUES (101, '2017-10-23 14:51:25', NULL, '32641AB2571CCC925C99AD6290C09946', 'dongrixin', b'1', NULL, '董日新', '10cs27q!');
INSERT INTO `user` VALUES (102, '2017-10-23 14:51:25', NULL, '52B29C58519D4DF77683652EDF7442B3', 'jiayihan', b'1', NULL, '贾一晗', 'my13xe9z');
INSERT INTO `user` VALUES (103, '2017-10-23 14:51:25', NULL, '9E595821974241ABD2AD6A0FF6908224', 'liujia315', b'1', NULL, '刘嘉', 'oz!#2ps3');
INSERT INTO `user` VALUES (104, '2017-10-23 14:51:25', NULL, '586026D0B89E5EA5A884EFF30186767F', 'luruixin', b'1', NULL, '卢芮欣', 'gpawzvoa');
INSERT INTO `user` VALUES (105, '2017-10-23 14:51:25', NULL, '2DA4E67CEE671C83A63D56BDDF0D42E9', 'jingdongsheng', b'1', NULL, '井东升', 'ccqb&#$8');
INSERT INTO `user` VALUES (106, '2017-10-23 14:51:25', NULL, '043DD85BBD8F2DCD83B654C3AA5C9BF0', 'luyao', b'1', NULL, '路瑶', 'd#ct%gtk');
INSERT INTO `user` VALUES (107, '2017-10-23 14:51:25', NULL, '8C3F86F958F577531E316C71C7E76897', 'zhangchenlu', b'1', NULL, '张晨露', 'b4fvz-&3');
INSERT INTO `user` VALUES (108, '2017-10-23 14:51:25', NULL, '24A7537D0A82A3FA57562B5B5104A23B', 'guxuzhou', b'1', NULL, '顾旭舟', 'dl5ua5$x');
INSERT INTO `user` VALUES (109, '2017-10-23 14:51:25', NULL, '62E915B7A2DBC4E2D7B14A9DA7F46F95', 'huangkailun', b'1', NULL, '黄凯伦', 's36wjt#9');
INSERT INTO `user` VALUES (110, '2017-10-23 14:51:25', NULL, 'BEFB8425930E3C2CBB0F8F64AEE4D3E0', 'zhangfan', b'1', NULL, '张帆', '460eqt7y');
INSERT INTO `user` VALUES (111, '2017-10-23 14:51:25', NULL, '44A09E54E0B987D430B6318A383BFE27', 'jiahui', b'1', NULL, '贾卉', 'k%tqdvaj');
INSERT INTO `user` VALUES (112, '2017-10-23 14:51:25', NULL, 'E05E2567C2153FB05A2F8B33F648DAE9', 'xiaohaixia', b'1', NULL, '肖海霞', 'iiwaaa9h');
INSERT INTO `user` VALUES (113, '2017-10-23 14:51:25', NULL, '097966AD541F88BAEE1CDDBF25DAD84C', 'zhanglianna', b'1', NULL, '张连娜', 'zl3gk%al');
INSERT INTO `user` VALUES (114, '2017-10-23 14:51:25', NULL, '983D0FA6252CDA215163323B5B5A869A', 'luoweicheng', b'1', NULL, '罗伟成', 'aarn3mwu');
INSERT INTO `user` VALUES (115, '2017-10-23 14:51:25', NULL, '73D52B731E1F754BD0A86EDF98288C5B', 'shaotian', b'1', NULL, '邵天', 'cvu8$clq');
INSERT INTO `user` VALUES (116, '2017-10-23 14:51:25', NULL, '591865CEB4E32A5468C7BE078DD11703', 'zhangjunyao', b'1', NULL, '张君瑶', 't6q3tok!');
INSERT INTO `user` VALUES (117, '2017-10-23 14:51:25', NULL, '190145453CBF786FC95F62B3DEFA5EE3', 'guanxue', b'1', NULL, '官雪', 'sr6l86cf');
INSERT INTO `user` VALUES (118, '2017-10-23 14:51:25', NULL, '7BEE85B389777C514C5BF1A11FF13019', 'wangjuan', b'1', NULL, '王娟', 'im68$23z');
INSERT INTO `user` VALUES (119, '2017-10-23 14:51:25', NULL, 'A0FBC46C9C24D355BEA3BC865BAA5151', 'sunwenxiu', b'1', NULL, '孙文秀', '!x4jdb4g');
INSERT INTO `user` VALUES (120, '2017-10-23 14:51:25', NULL, 'B35F45E91330EF98A62C8B7151ADFD6F', 'cuiyanda', b'1', NULL, '崔彦达', 'ponwsyy9');
INSERT INTO `user` VALUES (121, '2017-10-23 14:51:25', NULL, 'AB8A4BC2E6A60B00E6B19D5864EE7BEB', 'wangyu', b'1', NULL, '王玉', '$pki3e88');
INSERT INTO `user` VALUES (122, '2017-10-23 14:51:25', NULL, '4CA263BA6405EF39F4FF0BF848F1D5C7', 'shenzhongyi', b'1', NULL, '申中一', '#8-$dx3y');
INSERT INTO `user` VALUES (123, '2017-10-23 14:51:25', NULL, '0AC523CC3AE147FB7DE6E704450DFC8F', 'houyi', b'1', NULL, '侯伊', 'xgyzxue0');
INSERT INTO `user` VALUES (124, '2017-10-23 14:51:25', NULL, '058C20E498826DD0230143147FD47503', 'zhouxuan', b'1', NULL, '周璇', '!q$mtfs0');
INSERT INTO `user` VALUES (125, '2017-10-23 14:51:25', NULL, 'D426D0205EA85CAEB3F9886181AE4679', 'sunshuang', b'1', NULL, '孙爽', 'n&8#t8ju');
INSERT INTO `user` VALUES (126, '2017-10-23 14:51:25', NULL, '90E6A6B4712082A36E6D4CFD8E300DF3', 'ningyufei', b'1', NULL, '宁玉飞', '!qw3-nsw');
INSERT INTO `user` VALUES (127, '2017-10-23 14:51:25', NULL, '4794177A1F0AE5CD66D911F64EC605FB', 'zhangyue', b'1', NULL, '张悦', '4jvr8bqq');
INSERT INTO `user` VALUES (128, '2017-10-23 14:51:25', NULL, '140B2A9B60BFFEFD70919B6C2FFD8DF6', 'guoyan', b'1', NULL, '郭燕', '3k48!4kz');
INSERT INTO `user` VALUES (129, '2017-10-23 14:51:25', NULL, 'F02156AFDC677039CA2A65CD432B8AEC', 'gaobing', b'1', NULL, '高兵', '$z97rs&%');
INSERT INTO `user` VALUES (130, '2017-10-23 14:51:25', NULL, '3DC730EDDB04A779FED36D4B6428E2A4', 'guofeimeng', b'1', NULL, '郭飞蒙', 'oh6cza#f');
INSERT INTO `user` VALUES (131, '2017-10-23 14:51:25', NULL, 'EEEF3FEE32752534C65F96B6D49353A5', 'taozan', b'1', NULL, '陶赞', 'wckwp%%8');
INSERT INTO `user` VALUES (132, '2017-10-23 14:51:25', NULL, 'E8657A42DCACE4F276E174AE54AB3C1F', 'zhangkun', b'1', NULL, '张堃', '3qlyy2h5');
INSERT INTO `user` VALUES (133, '2017-10-23 14:51:25', NULL, '494F8D8A22925E86E3A1F2C587E2132C', 'sungeshan', b'1', NULL, '孙歌珊', '1k$9atpj');
INSERT INTO `user` VALUES (134, '2017-10-23 14:51:25', NULL, '1B50DD1EEEDEAEDC80998952962F3DF9', 'zhangwenyu', b'1', NULL, '张文玉', 'hp$yw&$l');
INSERT INTO `user` VALUES (135, '2017-10-23 14:51:25', NULL, '8D5788F3513D1D45FA4379436431121F', 'zhouyayun', b'1', NULL, '周亚运', 'qa4jy5yc');
INSERT INTO `user` VALUES (136, '2017-10-23 14:51:25', NULL, 'B4C4EF931E3B43927E4D1929923CA965', 'liangdong', b'1', NULL, '梁冬', 'hoo57e2s');
INSERT INTO `user` VALUES (137, '2017-10-23 14:51:25', NULL, '374EE5C8AA58CA0AE8866454F20D4352', 'liutiti', b'1', NULL, '刘缇缇', '5!hele-h');
INSERT INTO `user` VALUES (138, '2017-10-23 14:51:25', NULL, '7CBB606971C79B7C841163A17B4ACFE0', 'zhanghongjun', b'1', NULL, '张红军', 't7578j$%');
INSERT INTO `user` VALUES (139, '2017-10-23 14:51:25', NULL, '64F0F7ADC41E22A390DA10F82C803693', 'hushi', b'1', NULL, '胡石', 'uq&w4c1-');
INSERT INTO `user` VALUES (140, '2017-10-23 14:51:25', NULL, '2BCFCAC140295E0DA71D2F528BBC1879', 'heliangfu', b'1', NULL, '何亮甫', 'ow%pnwdt');
INSERT INTO `user` VALUES (141, '2017-10-23 14:51:25', NULL, 'B8C96A9BA29F065C679DFA10709E18D0', 'changkeyi', b'1', NULL, '常可意', 's9d-8ood');
INSERT INTO `user` VALUES (142, '2017-10-23 14:51:25', NULL, '0BC673181609C6AD311938C1CA2F06E5', 'lizhen', b'1', NULL, '李珍', '6f8u!q9%');
INSERT INTO `user` VALUES (143, '2017-10-23 14:51:25', NULL, '420FF179046216DD7578D4B7A72112AF', 'gaoxinlei', b'1', NULL, '高鑫磊', '06qohd4%');
INSERT INTO `user` VALUES (144, '2017-10-23 14:51:25', NULL, '3ADCD0E4BCA5A0420D7F656DBFFC7B18', 'liyun', b'1', NULL, '李云', 'kyjwjsuk');
INSERT INTO `user` VALUES (145, '2017-10-23 14:51:25', NULL, '6BFCB706EB2EA3FDEEE0B90A8337DA14', 'songxu', b'1', NULL, '宋旭', '%ehh8kfr');
INSERT INTO `user` VALUES (146, '2017-10-23 14:51:25', NULL, '0C7818E567CAC207B324ED8C55CA197B', 'zhangfangzhou', b'1', NULL, '张方舟', '&-9&yxcd');
INSERT INTO `user` VALUES (147, '2017-10-23 14:51:25', NULL, 'FE74BE482A22A65C1AC63CD84AAB00A7', 'zhaozhe', b'1', NULL, '赵哲', 'e!3auafn');
INSERT INTO `user` VALUES (148, '2017-10-23 14:51:25', NULL, '6E5C7B7ABF83367C64833572664A6CEE', 'tangyidan', b'1', NULL, '唐艺丹', '7n3r5%0!');
INSERT INTO `user` VALUES (149, '2017-10-23 14:51:25', NULL, 'E25D0BA025D2677E3F6949C8D6DDD5BB', 'zhulin', b'1', NULL, '朱琳', 'nauyxdo-');
INSERT INTO `user` VALUES (150, '2017-10-23 14:51:25', NULL, '71C0B382668F885E1692539C49391DAD', 'luyongdong', b'1', NULL, '鹿永东', '2%6bmy$0');
INSERT INTO `user` VALUES (151, '2017-10-23 14:51:25', NULL, '9B35AE4FCDFB637EEE5607C3E4C40CD3', 'yinge', b'1', NULL, '尹歌', 'oh04!!!3');
INSERT INTO `user` VALUES (152, '2017-10-23 14:51:25', NULL, '282374D22E3ED2E97FD12146E2CD1362', 'wangtengfei', b'1', NULL, '王腾飞', '4eznkst8');
INSERT INTO `user` VALUES (153, '2017-10-23 14:51:25', NULL, 'F5B458A0F8FBE3B5FA1FEE15CCC538DF', 'wangxiuyu', b'1', NULL, '王琇宇', 'p11ia3pg');
INSERT INTO `user` VALUES (154, '2017-10-23 14:51:25', NULL, '3A903D6AB786889E2AA5832335384661', 'caimeng', b'1', NULL, '蔡萌', '70us!ojf');
INSERT INTO `user` VALUES (155, '2017-10-23 14:51:25', NULL, '532C8E50DD69A4F7BF5D0F1BBCC02880', 'zhaozhan', b'1', NULL, '赵展', 'a#3ajp9f');
INSERT INTO `user` VALUES (156, '2017-10-23 14:51:25', NULL, 'B101F7878FC5F34FFDAF910120AC3716', 'lusiyi', b'1', NULL, '鲁思逸', '$moa77yk');
INSERT INTO `user` VALUES (157, '2017-10-23 14:51:25', NULL, '581646034B52440FE5EF4C176719FA3F', 'liangyu', b'1', NULL, '梁钰', 'vwms7h4j');
INSERT INTO `user` VALUES (158, '2017-10-23 14:51:25', NULL, '0E19BB405753F11C09993C63BDA37B9D', 'liujia1216', b'1', NULL, '刘佳', 'kb9-y38b');
INSERT INTO `user` VALUES (159, '2017-10-23 14:51:25', NULL, 'DDF26EBE5AAE1EB97268D97B09A285F1', 'wangyihan', b'1', NULL, '王艺涵', 'cc#sfgtc');
INSERT INTO `user` VALUES (160, '2017-10-23 14:51:25', NULL, 'AAF8226C13B26992FCA7B5186D45B64A', 'wangzu', b'1', NULL, '王足', '$80w04s4');
INSERT INTO `user` VALUES (161, '2017-10-23 14:51:25', NULL, 'DF4FF83F688D66B983FA0484DD166563', 'lixin', b'1', NULL, '李昕', '7f0sv6bi');
INSERT INTO `user` VALUES (162, '2017-10-23 14:51:25', NULL, 'F6C89A5F0A47B79F20D93B7F8F24EA50', 'liyang1027', b'1', NULL, '李洋', 'hl#zg38s');
INSERT INTO `user` VALUES (163, '2017-10-23 14:51:25', NULL, '5D3B9897A8DEFCF6AFD7AD36DB2188E8', 'xiaochuan', b'1', NULL, '肖川', 'gl39e8ke');
INSERT INTO `user` VALUES (164, '2017-10-23 14:51:25', NULL, 'DC226C4C2FF40CE933B424E6194CFB36', 'wangtongtong', b'1', NULL, '王通通', 'on8o3z-x');
INSERT INTO `user` VALUES (165, '2017-10-23 14:51:25', NULL, 'D5AC7E4749831BB32FFC3A10D350C3A2', 'luyao119', b'1', NULL, '陆遥', 'llv#!rsg');
INSERT INTO `user` VALUES (166, '2017-10-23 14:51:25', NULL, '4D6915C8AA5F438AD98DE446D610E264', 'huomeng', b'1', NULL, '霍梦', '$berhlfm');
INSERT INTO `user` VALUES (167, '2017-10-23 14:51:25', NULL, '9E6B5378460F8381757130DE49B9AD7C', 'liuyuchen', b'1', NULL, '刘宇辰', '!uvct31s');
INSERT INTO `user` VALUES (168, '2017-10-23 14:51:25', NULL, '96DF80BA0E51E8D09381B9E96756FE56', 'lixiao', b'1', NULL, '李筱', 'xs5q7d16');
INSERT INTO `user` VALUES (169, '2017-10-23 14:51:25', NULL, '754C1282CE089EA1DADEF09A4EB9D18B', 'dingxiang', b'1', NULL, '丁翔', 'fyuy%1wb');
INSERT INTO `user` VALUES (170, '2017-10-23 14:51:25', NULL, '11A705D7CD52B616E4102B7758333C47', 'chenjialin', b'1', NULL, '陈家林', '80vr04ic');
INSERT INTO `user` VALUES (171, '2017-10-23 14:51:25', NULL, '911C2DACA5133A827CF61BC8B9BBC2DE', 'huqianfang', b'1', NULL, '胡前方', '&pfa5e4a');
INSERT INTO `user` VALUES (172, '2017-10-23 14:51:25', NULL, '8CE8A9E28EDE1AEC87E2C75497BADAFD', 'wangqian', b'1', NULL, '王倩', 'yl70olnd');
INSERT INTO `user` VALUES (173, '2017-10-23 14:51:25', NULL, 'FD7B97AA8674B5DD7ABEC006AAC11023', 'caiwenyu', b'1', NULL, '蔡文宇', 'or0s4or1');
INSERT INTO `user` VALUES (174, '2017-10-23 14:51:25', NULL, 'EBA032C728B388E3B34F95028D11B7A8', 'zhenxing', b'1', NULL, '甄幸', 'pwu9y1q1');
INSERT INTO `user` VALUES (175, '2017-10-23 14:51:25', NULL, 'B0495B03A11252B48FCE1C33B75E88D5', 'jiangjia', b'1', NULL, '蒋佳', 'zjfaf1qr');
INSERT INTO `user` VALUES (176, '2017-10-23 14:51:25', NULL, 'EC24EF1B31F0162A5894D1B3DB2B0C6A', 'zhangfeng', b'1', NULL, '张丰', '74e97ww!');
INSERT INTO `user` VALUES (177, '2017-10-23 14:51:25', NULL, '8655C5C9AA9FAC7F591524882CC131C3', 'shenchen', b'1', NULL, '申晨', '&qmgksty');
INSERT INTO `user` VALUES (178, '2017-10-23 14:51:25', NULL, '117354D23DA47E95E9ABFE635E8494D4', 'xuyujie', b'1', NULL, '许余洁', 'byl0$v#b');
INSERT INTO `user` VALUES (179, '2017-10-23 14:51:25', NULL, '71B1E2098993261E4BF625C8DC0D738A', 'hemiaomiao', b'1', NULL, '何苗苗', 'b#gj3xuq');
INSERT INTO `user` VALUES (180, '2017-10-23 14:51:25', NULL, '698428756B6EC05BAAEC9F00E7B281E7', 'litianjiao', b'1', NULL, '李天娇', '8fthoyjb');
INSERT INTO `user` VALUES (181, '2017-10-23 14:51:25', NULL, '1FB25E249901CE2087F0F8E472A9AD88', 'wuyou', b'1', NULL, '吴优', 'g!g8x%xw');
INSERT INTO `user` VALUES (182, '2017-10-23 14:51:25', NULL, '7809B02B3DB6DF9A2E5F6208A2086C77', 'liuxiaojia', b'1', NULL, '刘晓佳', '22m$0620');
INSERT INTO `user` VALUES (183, '2017-10-23 14:51:25', NULL, '741229420B7051DA8E99955A62FE9D81', 'wangjing', b'1', NULL, '王菁', 'vned6r4z');
INSERT INTO `user` VALUES (184, '2017-10-23 14:51:25', NULL, '5CF3475B094A760160E97FB1B18B2415', 'lishuang', b'1', NULL, '李爽', '6&l&3#ln');
INSERT INTO `user` VALUES (185, '2017-10-23 14:51:25', NULL, '553A0FE5207ADFF1617109BFBA5D54C1', 'zhangxinxin', b'1', NULL, '张欣欣', 'h%10km26');
INSERT INTO `user` VALUES (186, '2017-10-23 14:51:25', NULL, '2352299188FDDDDFA4ADBB2124AE20AF', 'zhaixiaojing', b'1', NULL, '翟晓靖', '73&hdmmj');
INSERT INTO `user` VALUES (187, '2017-10-23 14:51:25', NULL, '71F8ECDDAA4151ED27C25966E101AB38', 'xulingzhi', b'1', NULL, '许凌志', 's%9%sfez');
INSERT INTO `user` VALUES (188, '2017-10-23 14:51:25', NULL, 'D761402C18CB5BB5DB44A2025FB2CE95', 'wangyanan', b'1', NULL, '王亚楠', '#3k&01y0');
INSERT INTO `user` VALUES (189, '2017-10-23 14:51:25', NULL, '23A190A35F0526D79F9B17C147C76382', 'liuying', b'1', NULL, '刘颖', '#2da#7nq');
INSERT INTO `user` VALUES (190, '2017-10-23 14:51:25', NULL, '179DF420F2081F7DDCABEEBFF8CD80DC', 'liufang', b'1', NULL, '刘芳', 'k9y&-wer');
INSERT INTO `user` VALUES (191, '2017-10-23 14:51:25', NULL, '68CD257BBFAF3F52E97C3DDEC949F735', 'gaoyan', b'1', NULL, '高妍', 'rvrao4te');
INSERT INTO `user` VALUES (192, '2017-10-23 14:51:25', NULL, '3CA4684627F47DD848506853A248EABB', 'liuchang', b'1', NULL, '刘畅', '--wwb9mf');
INSERT INTO `user` VALUES (193, '2017-10-23 14:51:25', NULL, '37B8B5550479331326D425F682F9E30A', 'shenyan', b'1', NULL, '沈燕', '6a5t-633');
INSERT INTO `user` VALUES (194, '2017-10-23 14:51:25', NULL, '6D2176EDC27CFA22667E69FBA952A832', 'hanwenjing', b'1', NULL, '韩文婧', '-183gvb0');
INSERT INTO `user` VALUES (195, '2017-10-23 14:51:25', NULL, '22C82A0F2E42AC7DD78D3880DC7518C7', 'majing', b'1', NULL, '马静', 'v-0v7u1s');
INSERT INTO `user` VALUES (196, '2017-10-23 14:51:25', NULL, '6701C984615C9B47954092063E464563', 'wangbaohui', b'1', NULL, '王宝卉', 'as%3mxyy');
INSERT INTO `user` VALUES (197, '2017-10-23 14:51:25', NULL, 'FEBFABBE4BBCD9E1821BA9B785D03921', 'zhuyuying', b'1', NULL, '朱玉英', 'xiqbxo%l');
INSERT INTO `user` VALUES (198, '2017-10-23 14:51:25', NULL, '061C984620A01C2320A238C3F369E8E9', 'zhangqiuxia', b'1', NULL, '张秋霞', '&e1d!!h5');
INSERT INTO `user` VALUES (199, '2017-10-23 14:51:25', NULL, 'DBE315CB2AF7651E7FBE31B55211DBFA', 'dongxuejie', b'1', NULL, '董学杰', '51c4zw2h');
INSERT INTO `user` VALUES (200, '2017-10-23 14:51:25', NULL, '200EA2DF750E4122B139CB08F3E11D13', 'caolei', b'1', NULL, '曹磊', 'h5z03!y3');
INSERT INTO `user` VALUES (201, '2017-10-23 14:51:25', NULL, '4407FCECC3D202722583D0F7BD226621', 'yuanfang', b'1', NULL, '袁芳', '0mio!&m7');
INSERT INTO `user` VALUES (202, '2017-10-23 14:51:25', NULL, '02E0E19B87F3CCEE596FADFD9E12D173', 'gaochang', b'1', NULL, '高畅', 'r7o-3zeb');
INSERT INTO `user` VALUES (203, '2017-10-23 14:51:25', NULL, '38A0FDD9F423AE99B1A45CEC779F05B3', 'chenyihui', b'1', NULL, '陈诣辉', 'pnv&33zv');
INSERT INTO `user` VALUES (204, '2017-10-23 14:51:25', NULL, '005E38DDE588295312FF341BBB6CC7A0', 'shangxu', b'1', NULL, '商旭', 'qphlffk#');
INSERT INTO `user` VALUES (205, '2017-10-23 14:51:25', NULL, 'E703005A671FBE8623092D2A1E5F2789', 'xuhelong', b'1', NULL, '徐鹤龙', 'yqfrdp4t');
INSERT INTO `user` VALUES (206, '2017-10-23 14:51:25', NULL, '5E3BD5A50C08960A326B2B07EF3A9571', 'dengbowen', b'1', NULL, '邓博文', '950h14ae');
INSERT INTO `user` VALUES (207, '2017-10-23 14:51:25', NULL, 'D961D5E8F1FCEF87C76B4E013CB946C7', 'wanghaichao', b'1', NULL, '王海潮', 's!k94lzc');
INSERT INTO `user` VALUES (208, '2017-10-23 14:51:25', NULL, 'D423EF1CAD110334B60D543B957CF4EC', 'huqianhui', b'1', NULL, '胡乾慧', 'wviikz-h');
INSERT INTO `user` VALUES (209, '2017-10-23 14:51:25', NULL, 'D93B7543E3D50BF1A29A1AE0F70E8464', 'jiajing', b'1', NULL, '贾婧', '#pppn761');
INSERT INTO `user` VALUES (210, '2017-10-23 14:51:25', NULL, '220047752CCF56D5E117EF1086F8302B', 'zhuangyuan', b'1', NULL, '庄园', 'x6hjmbch');
INSERT INTO `user` VALUES (211, '2017-10-23 14:51:25', NULL, 'EEB00FEC21A68C9BCAC7B84F575D9AEF', 'lizekun', b'1', NULL, '李泽堃', 'h2xdho6d');
INSERT INTO `user` VALUES (212, '2017-10-23 14:51:25', NULL, '824150772CC0940FAC0284255679AF35', 'chenbo', b'1', NULL, '陈波', 'g6oypvk0');
INSERT INTO `user` VALUES (213, '2017-10-23 14:51:25', NULL, '6859B7B66B6827D9F3424FC1F3604C8B', 'shifengyang', b'1', NULL, '施风洋', 'h2-c7x1m');
INSERT INTO `user` VALUES (214, '2017-10-23 14:51:25', NULL, '0508A53DECED22AF3880597DDE8B42F8', 'fuyunxia', b'1', NULL, '付云霞', 'iksvcp8y');
INSERT INTO `user` VALUES (215, '2017-10-23 14:51:25', NULL, 'EA516A76C501BDF9E464A7EAF904F5BC', 'wangtao', b'1', NULL, '王涛', 'e301urhv');
INSERT INTO `user` VALUES (216, '2017-10-23 14:51:25', NULL, '2AB657A809CA2DD1B1218030C6A4A342', 'liudeng', b'1', NULL, '刘登', 'b&d3j9oj');
INSERT INTO `user` VALUES (217, '2017-10-23 14:51:25', NULL, 'E29FFEA6443C5D4AEB30CFBF41449FD9', 'renhui', b'1', NULL, '任慧', '5jkg7v4i');
INSERT INTO `user` VALUES (218, '2017-10-23 14:51:25', NULL, '08D0DDBAD7591FA4BB3F30A9FC09F3F8', 'wangyuan', b'1', NULL, '王原', 'bj4rscit');
INSERT INTO `user` VALUES (219, '2017-10-23 14:51:25', NULL, 'D810A52CF9A574EFBCE6CD337EA1DEF4', 'zhourongyi', b'1', NULL, '周荣义', 'lve3vdxb');
INSERT INTO `user` VALUES (220, '2017-10-23 14:51:25', NULL, 'D11630E94980CA69EE9C436E4FCCD800', 'liuqi', b'1', NULL, '刘琪', '-b&hbvp-');
INSERT INTO `user` VALUES (221, '2017-10-23 14:51:25', NULL, '68A2613D31435168243F7803E0D2F078', 'liusiyuan', b'1', NULL, '刘思远', '6-q%&$g!');
INSERT INTO `user` VALUES (222, '2017-10-23 14:51:25', NULL, 'D3FE81A319958DF751BA423F2C3E7975', 'zhengjie', b'1', NULL, '郑洁', 'q#jnahit');
INSERT INTO `user` VALUES (223, '2017-10-23 14:51:25', NULL, '3F3758AAAF4C6F582C8B8EEF4A411E31', 'huqifei', b'1', NULL, '胡齐菲', '#b6w3ycx');
INSERT INTO `user` VALUES (224, '2017-10-23 14:51:25', NULL, 'CCA632EC7FAC3F5F1FA166F1C0DB89DD', 'luoxiaotong', b'1', NULL, '罗晓桐', '8s1-8x5e');
INSERT INTO `user` VALUES (225, '2017-10-23 14:51:25', NULL, '1D4067E03E0E6E5F155F112F9C812D28', 'chenfanghui', b'1', NULL, '陈芳慧', '$dvl2snq');
INSERT INTO `user` VALUES (226, '2017-10-23 14:51:25', NULL, 'E94E4874A2521202CDDF4B04FE6CF0EA', 'sunyu', b'1', NULL, '孙与', 'gxlw-ksi');
INSERT INTO `user` VALUES (227, '2017-10-23 14:51:25', NULL, '88F05AD0902C1E7E40CBFD038419E0FA', 'huangzi', b'1', NULL, '黄孜', '080pmwt$');
INSERT INTO `user` VALUES (228, '2017-10-23 14:51:25', NULL, 'C7CFF373549B0A683599C7CA2530DC62', 'luoyi', b'1', NULL, '罗一', '#5pumpmw');
INSERT INTO `user` VALUES (229, '2017-10-23 14:51:25', NULL, '31ECBC0828D9C5C275558F731BF59C6D', 'gongyunqi', b'1', NULL, '巩云琪', 'w3xxmndu');
INSERT INTO `user` VALUES (230, '2017-10-23 14:51:25', NULL, 'C3BEA995B7849B4386DC5D953D0E6AD1', 'dingyuetong', b'1', NULL, '宁悦彤', 'l76#d7#b');
INSERT INTO `user` VALUES (231, '2017-10-23 14:51:25', NULL, 'C88230B9B631942C60D6A9E90616D016', 'tangzhenqi', b'1', NULL, '唐震奇', 'b%vfvg!u');
INSERT INTO `user` VALUES (232, '2017-10-23 14:51:25', NULL, '994FF4F9B84229D5095E743E2E71639E', 'kangjia', b'1', NULL, '康佳', 'mft#5lct');
INSERT INTO `user` VALUES (233, '2017-10-23 14:51:25', NULL, '0B68D3E92C1894119E71B7EC23372454', 'zhouyuxiao', b'1', NULL, '周宇骁', 'cyrl4y6$');
INSERT INTO `user` VALUES (234, '2017-10-23 14:51:25', NULL, '88926D5F69F957CD7701EC866F7B1391', 'jinlei', b'1', NULL, '金磊', 'thp0gxt$');
INSERT INTO `user` VALUES (235, '2017-10-23 14:51:25', NULL, '1ECE561A803AFE5B28C02EA652010AA0', 'cuijinzhu', b'1', NULL, '崔津珠', '53dgkkne');
INSERT INTO `user` VALUES (236, '2017-10-23 14:51:25', NULL, 'EA8027BF055ED995269DB6B02D7556BE', 'liyuan', b'1', NULL, '李源', '9cdg!9ae');
INSERT INTO `user` VALUES (237, '2017-10-23 14:51:25', NULL, '42D248B6C85695399C4B41CF634FE939', 'wanshengmou', b'1', NULL, '万盛谋', 't&qs1hj5');
INSERT INTO `user` VALUES (238, '2017-10-23 14:51:25', NULL, 'D56C1F1329BEBD4A3ED5D65DBB670646', 'leiyuqing', b'1', NULL, '雷雨晴', '319lvy#j');
INSERT INTO `user` VALUES (239, '2017-10-23 14:51:25', NULL, 'F2377BD91E0DFEB998FF6FE9F488E388', 'fenglei316', b'1', NULL, '冯磊', '4wp-9jw7');
INSERT INTO `user` VALUES (240, '2017-10-23 14:51:25', NULL, 'BB6B3749307B30FC69E1620005C36F4D', 'jiaerliu', b'1', NULL, '贾二柳', '64x$6v9d');
INSERT INTO `user` VALUES (241, '2017-10-23 14:51:25', NULL, 'FA1AF89B54F8315FE0E806D308D9DE21', 'duantong', b'1', NULL, '段彤', '9&tfr42c');
INSERT INTO `user` VALUES (242, '2017-10-23 14:51:25', NULL, '14A99DDB47E8797A8335C5F1ADC25C8E', 'xuyiran', b'1', NULL, '徐亦然', 'c93wcw39');
INSERT INTO `user` VALUES (243, '2017-10-23 14:51:25', NULL, 'D4B19AD60193D35339E092B404ADB212', 'liujing', b'1', NULL, '刘静', '-8cv5xtq');
INSERT INTO `user` VALUES (244, '2017-10-23 14:51:25', NULL, '808A9F38D685528B1C1A9C2557B41A33', 'fengrui', b'1', NULL, '冯锐', '0i1fntgc');
INSERT INTO `user` VALUES (245, '2017-10-23 14:51:25', NULL, 'FC16917FB86400E0E972613A33AD9C8A', 'zouqian', b'1', NULL, '邹倩', 'f8p&f&k8');
INSERT INTO `user` VALUES (246, '2017-10-23 14:51:25', NULL, 'D0848E3710C8BBAAF6B3C4D419B7F214', 'liyiyang', b'1', NULL, '李易阳', '5h5qpis&');
INSERT INTO `user` VALUES (247, '2017-10-23 14:51:25', NULL, '74FDA7BA2FF6C9AC88A1361C094F3980', 'chenfeng', b'1', NULL, '陈峰', '%wuk00!8');
INSERT INTO `user` VALUES (248, '2017-10-23 14:51:25', NULL, 'B6C90532153D03CE0435AB16E578FB8C', 'xingguangzhao', b'1', NULL, '邢广超', 'bddgle$k');
INSERT INTO `user` VALUES (249, '2017-10-23 14:51:25', NULL, '2CB20296D2CCB4044CBD3D8F47A963C8', 'gaobo', b'1', NULL, '高波', '%z64j#s8');
INSERT INTO `user` VALUES (250, '2017-10-23 14:51:25', NULL, '0C8E2F9E541D3D619300DFA28D132B87', 'tianye', b'1', NULL, '田野', 'nk6o!vx8');
INSERT INTO `user` VALUES (251, '2017-10-23 14:51:25', NULL, 'BB21B83964EF1300AB40794668098982', 'dangzonghan', b'1', NULL, '党宗汉', 'kls7qs-1');
INSERT INTO `user` VALUES (252, '2017-10-23 14:51:25', NULL, '52A12B8471B5560309F85F68D266800D', 'xiaohaixia_sp', b'1', NULL, '肖海霞', 'eaj!7ted');
INSERT INTO `user` VALUES (253, '2017-10-23 14:51:25', NULL, 'F9235D68F65375884BC768B3384E79F4', 'cuiyan', b'1', NULL, '崔炎', '8a!wbsp#');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
