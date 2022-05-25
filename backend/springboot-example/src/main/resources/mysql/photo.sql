/*
 Navicat Premium Data Transfer
 
 Source Server         : Hongbin
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : photoplatform
 
 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001
 
 Date: 18/05/2022 10:27:34
 */
SET
  NAMES utf8mb4;

SET
  FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for photo
-- ----------------------------
DROP TABLE IF EXISTS `photo`;

CREATE TABLE `photo` (
  `photoID` int NOT NULL AUTO_INCREMENT,
  `userId` int NULL DEFAULT NULL,
  `photoName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `photoPath` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`photoID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 283 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET
  FOREIGN_KEY_CHECKS = 1;