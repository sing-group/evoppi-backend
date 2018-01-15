CREATE DATABASE  IF NOT EXISTS `evoppi` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `evoppi`;
-- MySQL dump 10.13  Distrib 5.6.33, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: evoppi
-- ------------------------------------------------------
-- Server version	5.6.33-0ubuntu0.14.04.1-log

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
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `login` varchar(100) NOT NULL,
  PRIMARY KEY (`login`),
  CONSTRAINT `FK7mdlakg18ui7hqe8de30yni45` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gene`
--

DROP TABLE IF EXISTS `gene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gene` (
  `id` int(11) NOT NULL,
  `sequence` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gene_name`
--

DROP TABLE IF EXISTS `gene_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gene_name` (
  `geneId` int(11) NOT NULL,
  `source` varchar(255) NOT NULL,
  PRIMARY KEY (`geneId`,`source`),
  CONSTRAINT `FKltnrhrbud9fvdilc74lybdvio` FOREIGN KEY (`geneId`) REFERENCES `gene` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gene_name_value`
--

DROP TABLE IF EXISTS `gene_name_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gene_name_value` (
  `gene_id` int(11) NOT NULL,
  `gene_source` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  KEY `FK_gene_name_gene_name_values` (`gene_id`,`gene_source`),
  CONSTRAINT `FK_gene_name_gene_name_values` FOREIGN KEY (`gene_id`, `gene_source`) REFERENCES `gene_name` (`geneId`, `source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interaction`
--

DROP TABLE IF EXISTS `interaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interaction` (
  `geneA` int(11) NOT NULL,
  `geneB` int(11) NOT NULL,
  `interactome` int(11) NOT NULL,
  PRIMARY KEY (`geneA`,`geneB`,`interactome`),
  KEY `FKekyhguctj9hvu5qevfo5v3rvj` (`geneB`),
  KEY `FKno3mlamen4lj75iywf6kxh8l8` (`interactome`),
  CONSTRAINT `FKekyhguctj9hvu5qevfo5v3rvj` FOREIGN KEY (`geneB`) REFERENCES `gene` (`id`),
  CONSTRAINT `FKkpnlx65nno3hups2cwudtmg90` FOREIGN KEY (`geneA`) REFERENCES `gene` (`id`),
  CONSTRAINT `FKno3mlamen4lj75iywf6kxh8l8` FOREIGN KEY (`interactome`) REFERENCES `interactome` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interactome`
--

DROP TABLE IF EXISTS `interactome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interactome` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `speciesId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kkhr6oll21trthn8foo6w12xb` (`name`),
  KEY `FKglc54t69v5a3veskvhr86penc` (`speciesId`),
  CONSTRAINT `FKglc54t69v5a3veskvhr86penc` FOREIGN KEY (`speciesId`) REFERENCES `species` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `researcher`
--

DROP TABLE IF EXISTS `researcher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `researcher` (
  `login` varchar(100) NOT NULL,
  PRIMARY KEY (`login`),
  CONSTRAINT `FK5xp3cky3w5eqkshtmtl31xqpp` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `species`
--

DROP TABLE IF EXISTS `species`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `species` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_29ixq8ot8e88rk6v7jpkisgr3` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `role` varchar(10) NOT NULL,
  `login` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-15 18:36:49
