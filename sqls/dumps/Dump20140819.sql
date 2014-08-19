CREATE DATABASE  IF NOT EXISTS `ridesharecn` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ridesharecn`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: ridesharecn
-- ------------------------------------------------------
-- Server version	5.5.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `commuteownerride`
--

DROP TABLE IF EXISTS `commuteownerride`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commuteownerride` (
  `userId` int(10) unsigned DEFAULT NULL,
  `RideInfoId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `MiddlePointCount` int(2) DEFAULT '0',
  `M1Addr` varchar(50) DEFAULT NULL,
  `M1Lat` decimal(10,6) DEFAULT NULL,
  `M1Lon` decimal(10,6) DEFAULT NULL,
  `M2Addr` varchar(50) DEFAULT NULL,
  `M2Lat` decimal(10,6) DEFAULT NULL,
  `M2Lon` decimal(10,6) DEFAULT NULL,
  `M3Addr` varchar(50) DEFAULT NULL,
  `M3Lat` decimal(10,6) DEFAULT NULL,
  `M3Lon` decimal(10,6) DEFAULT NULL,
  `M4Addr` varchar(50) DEFAULT NULL,
  `M4Lat` decimal(10,6) DEFAULT NULL,
  `M4Lon` decimal(10,6) DEFAULT NULL,
  `M5Addr` varchar(50) DEFAULT NULL,
  `M5Lat` decimal(10,6) DEFAULT NULL,
  `M5Lon` decimal(10,6) DEFAULT NULL,
  UNIQUE KEY `RideInfoId` (`RideInfoId`),
  KEY `userId` (`userId`),
  CONSTRAINT `commuteownerride_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commutepartiride`
--

DROP TABLE IF EXISTS `commutepartiride`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commutepartiride` (
  `RideInfoID` int(10) unsigned DEFAULT NULL,
  `TopicId` int(10) DEFAULT NULL,
  `Status` int(2) DEFAULT NULL,
  `GeoMatch` int(3) DEFAULT '0',
  `ScheduleMatch` int(3) DEFAULT '0',
  `BarginMatch` int(3) DEFAULT '0',
  KEY `RideInfoID` (`RideInfoID`),
  CONSTRAINT `commutepartiride_ibfk_1` FOREIGN KEY (`RideInfoID`) REFERENCES `commuteride` (`recordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commuteride`
--

DROP TABLE IF EXISTS `commuteride`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commuteride` (
  `userId` int(10) unsigned DEFAULT NULL,
  `recordId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `origFAddr` varchar(100) DEFAULT NULL,
  `origAddr` varchar(50) DEFAULT NULL,
  `origState` varchar(20) DEFAULT NULL,
  `origCity` varchar(30) DEFAULT NULL,
  `origLat` decimal(10,6) DEFAULT NULL,
  `origLon` decimal(10,6) DEFAULT NULL,
  `destFAddr` varchar(100) DEFAULT NULL,
  `destAddr` varchar(50) DEFAULT NULL,
  `destState` varchar(20) DEFAULT NULL,
  `destCity` varchar(30) DEFAULT NULL,
  `destLat` decimal(10,6) DEFAULT NULL,
  `destLon` decimal(10,6) DEFAULT NULL,
  `distance` int(8) DEFAULT NULL,
  `duration` int(8) DEFAULT NULL,
  `commute` tinyint(1) DEFAULT NULL,
  `roundtrip` tinyint(1) DEFAULT NULL,
  `forwardFlex` time DEFAULT NULL,
  `backFlex` time DEFAULT NULL,
  `tripDate` date DEFAULT NULL,
  `tripTime` time DEFAULT NULL,
  `dayOfWeek` int(7) DEFAULT NULL,
  `f1` time DEFAULT NULL,
  `f2` time DEFAULT NULL,
  `f3` time DEFAULT NULL,
  `f4` time DEFAULT NULL,
  `f5` time DEFAULT NULL,
  `f6` time DEFAULT NULL,
  `f7` time DEFAULT NULL,
  `b1` time DEFAULT NULL,
  `b2` time DEFAULT NULL,
  `b3` time DEFAULT NULL,
  `b4` time DEFAULT NULL,
  `b5` time DEFAULT NULL,
  `b6` time DEFAULT NULL,
  `b7` time DEFAULT NULL,
  `UserType` tinyint(1) DEFAULT NULL,
  `TotalSeats` int(2) DEFAULT NULL,
  `AvailSeats` int(2) DEFAULT NULL,
  `PayperSeat` decimal(6,2) DEFAULT NULL,
  UNIQUE KEY `recordId` (`recordId`),
  KEY `userId` (`userId`),
  CONSTRAINT `commuteride_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commutetopic`
--

DROP TABLE IF EXISTS `commutetopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commutetopic` (
  `TopicId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `OwnerId` int(10) unsigned DEFAULT NULL,
  `ParRideIds` varchar(100) DEFAULT NULL,
  `ReqRideIds` varchar(100) DEFAULT NULL,
  `messageIds` varchar(400) DEFAULT NULL,
  UNIQUE KEY `TopicId` (`TopicId`),
  KEY `OwnerId` (`OwnerId`),
  CONSTRAINT `commutetopic_ibfk_1` FOREIGN KEY (`OwnerId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `MessageId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fromUser` int(10) DEFAULT NULL,
  `toUser` int(10) DEFAULT NULL,
  `topicID` int(10) DEFAULT NULL,
  `messageContent` varchar(500) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isSystemMessage` tinyint(1) DEFAULT NULL,
  UNIQUE KEY `MessageId` (`MessageId`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transientride`
--

DROP TABLE IF EXISTS `transientride`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transientride` (
  `userId` int(10) unsigned DEFAULT NULL,
  `transientRideId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `origFAddr` varchar(100) DEFAULT NULL,
  `origAddr` varchar(50) DEFAULT NULL,
  `origState` varchar(20) DEFAULT NULL,
  `origCity` varchar(30) DEFAULT NULL,
  `origLat` decimal(10,6) DEFAULT NULL,
  `origLon` decimal(10,6) DEFAULT NULL,
  `destFAddr` varchar(100) DEFAULT NULL,
  `destAddr` varchar(50) DEFAULT NULL,
  `destState` varchar(20) DEFAULT NULL,
  `destCity` varchar(30) DEFAULT NULL,
  `destLat` decimal(10,6) DEFAULT NULL,
  `destLon` decimal(10,6) DEFAULT NULL,
  `distance` int(8) DEFAULT NULL,
  `duration` int(8) DEFAULT NULL,
  `rideDate` date DEFAULT NULL,
  `rideTime` time DEFAULT NULL,
  `rideFlex` time DEFAULT NULL,
  `UserType` tinyint(1) DEFAULT NULL,
  `TotalSeats` int(2) DEFAULT NULL,
  `AvailSeats` int(2) DEFAULT NULL,
  `PayperSeat` decimal(6,2) DEFAULT NULL,
  UNIQUE KEY `transientRideId` (`transientRideId`),
  KEY `userId` (`userId`),
  CONSTRAINT `transientride_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transienttopic`
--

DROP TABLE IF EXISTS `transienttopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transienttopic` (
  `transientRideId` int(10) unsigned DEFAULT NULL,
  `nmiddlePoints` int(1) unsigned DEFAULT NULL,
  `middle1Faddr` varchar(100) DEFAULT NULL,
  `middle1Lat` decimal(10,6) DEFAULT NULL,
  `middle1Lng` decimal(10,6) DEFAULT NULL,
  `middle2Faddr` varchar(100) DEFAULT NULL,
  `middle2Lat` decimal(10,6) DEFAULT NULL,
  `middle2Lng` decimal(10,6) DEFAULT NULL,
  `middle3Faddr` varchar(100) DEFAULT NULL,
  `middle3Lat` decimal(10,6) DEFAULT NULL,
  `middle3Lng` decimal(10,6) DEFAULT NULL,
  `middle4Faddr` varchar(100) DEFAULT NULL,
  `middle4Lat` decimal(10,6) DEFAULT NULL,
  `middle4Lng` decimal(10,6) DEFAULT NULL,
  `middle5Faddr` varchar(100) DEFAULT NULL,
  `middle5Lat` decimal(10,6) DEFAULT NULL,
  `middle5Lng` decimal(10,6) DEFAULT NULL,
  `nParticipant` int(1) unsigned DEFAULT NULL,
  `partiuid1` int(10) DEFAULT NULL,
  `partiuid2` int(10) DEFAULT NULL,
  `partiuid3` int(10) DEFAULT NULL,
  `partiuid4` int(10) DEFAULT NULL,
  `partiuid5` int(10) DEFAULT NULL,
  KEY `transientRideId` (`transientRideId`),
  CONSTRAINT `transienttopic_ibfk_1` FOREIGN KEY (`transientRideId`) REFERENCES `transientride` (`transientRideId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `emailAddress` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `password` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  `givenname` char(30) CHARACTER SET utf8 DEFAULT NULL,
  `surname` char(30) CHARACTER SET utf8 DEFAULT NULL,
  `address` varchar(200) CHARACTER SET latin1 DEFAULT NULL,
  `userLevel` int(11) DEFAULT NULL,
  `avatarID` varchar(35) COLLATE utf8_bin DEFAULT NULL,
  `groupId` int(10) unsigned DEFAULT NULL,
  `cellphone` varchar(15) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`userId`),
  KEY `groupId` (`groupId`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`groupId`) REFERENCES `usergroup` (`GroupID`)
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usergroup`
--

DROP TABLE IF EXISTS `usergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usergroup` (
  `GroupID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `GroupName` varchar(50) DEFAULT NULL,
  `GroupAuthLevel` int(11) DEFAULT NULL,
  `authenticationcode` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`GroupID`),
  UNIQUE KEY `GroupID` (`GroupID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-08-19 23:18:32
