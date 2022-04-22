/*
Navicat MySQL Data Transfer

Source Server         : Hwadee
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : user0

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2022-04-22 20:22:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_login_time` datetime(6) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_num` int(11) DEFAULT NULL,
  `random_code` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
