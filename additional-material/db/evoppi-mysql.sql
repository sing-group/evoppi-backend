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
-- Table structure for table `blast_result`
--

DROP TABLE IF EXISTS `blast_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blast_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bitscore` double DEFAULT NULL,
  `evalue` varchar(255) DEFAULT NULL,
  `gapopen` int(11) NOT NULL,
  `length` int(11) NOT NULL,
  `mismatch` int(11) NOT NULL,
  `pident` double DEFAULT NULL,
  `qend` int(11) NOT NULL,
  `qseqid` int(11) NOT NULL,
  `qseqversion` int(11) NOT NULL,
  `qstart` int(11) NOT NULL,
  `send` int(11) NOT NULL,
  `sseqid` int(11) NOT NULL,
  `sseqversion` int(11) NOT NULL,
  `sstart` int(11) NOT NULL,
  `interactionsResultId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_different_species_interactions_result_blast_result` (`interactionsResultId`),
  CONSTRAINT `FK_different_species_interactions_result_blast_result` FOREIGN KEY (`interactionsResultId`) REFERENCES `different_species_interactions_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `different_species_interactions_result`
--

DROP TABLE IF EXISTS `different_species_interactions_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `different_species_interactions_result` (
  `evalue` double DEFAULT NULL,
  `maxTargetSeqs` int(11) DEFAULT NULL,
  `minimumAlignmentLength` int(11) DEFAULT NULL,
  `minimumIdentity` double DEFAULT NULL,
  `referenceInteractomeId` int(11) NOT NULL,
  `targetInteractomeId` int(11) NOT NULL,
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK2hyw1ycq92xcp54jn78p62uhx` FOREIGN KEY (`id`) REFERENCES `interactions_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `different_species_interactions_result_reference_genes`
--

DROP TABLE IF EXISTS `different_species_interactions_result_reference_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `different_species_interactions_result_reference_genes` (
  `interactionsResultId` varchar(255) NOT NULL,
  `geneId` int(11) NOT NULL,
  PRIMARY KEY (`interactionsResultId`,`geneId`),
  CONSTRAINT `FK_different_species_interactions_result_reference_genes` FOREIGN KEY (`interactionsResultId`) REFERENCES `different_species_interactions_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `different_species_interactions_result_target_genes`
--

DROP TABLE IF EXISTS `different_species_interactions_result_target_genes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `different_species_interactions_result_target_genes` (
  `interactionsResultId` varchar(255) NOT NULL,
  `geneId` int(11) NOT NULL,
  PRIMARY KEY (`interactionsResultId`,`geneId`),
  CONSTRAINT `FK_different_species_interactions_result_target_genes` FOREIGN KEY (`interactionsResultId`) REFERENCES `different_species_interactions_result` (`id`)
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
  `species` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXnkshoslla6kq08gqh38grefke` (`id`,`species`),
  KEY `FKg5uaph3wq3eu765ch9lkq6qi1` (`species`),
  CONSTRAINT `FKg5uaph3wq3eu765ch9lkq6qi1` FOREIGN KEY (`species`) REFERENCES `species` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gene_in_interactome`
--

DROP TABLE IF EXISTS `gene_in_interactome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gene_in_interactome` (
  `gene` int(11) NOT NULL,
  `interactome` int(11) NOT NULL,
  `species` int(11) NOT NULL,
  PRIMARY KEY (`gene`,`interactome`,`species`),
  KEY `FKsswgs3cc7avkugqvq78sv21xg` (`interactome`),
  KEY `FKtpcnom6fs9jal4qfgao444cse` (`species`),
  CONSTRAINT `FKa02a13n65pbhq1m1ehk63f4es` FOREIGN KEY (`gene`) REFERENCES `gene` (`id`),
  CONSTRAINT `FKsswgs3cc7avkugqvq78sv21xg` FOREIGN KEY (`interactome`) REFERENCES `interactome` (`id`),
  CONSTRAINT `FKtpcnom6fs9jal4qfgao444cse` FOREIGN KEY (`species`) REFERENCES `species` (`id`)
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
  `geneId` int(11) NOT NULL,
  `geneSource` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  KEY `FK_gene_name_gene_name_values` (`geneId`,`geneSource`),
  CONSTRAINT `FK_gene_name_gene_name_values` FOREIGN KEY (`geneId`, `geneSource`) REFERENCES `gene_name` (`geneId`, `source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gene_sequence`
--

DROP TABLE IF EXISTS `gene_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gene_sequence` (
  `geneId` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `sequence` longtext NOT NULL,
  PRIMARY KEY (`geneId`,`version`),
  CONSTRAINT `FK83tcxrdc8c7l2mxy2ba3b72r6` FOREIGN KEY (`geneId`) REFERENCES `gene` (`id`)
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
  `species` int(11) NOT NULL,
  PRIMARY KEY (`geneA`,`geneB`,`interactome`,`species`),
  KEY `FKno3mlamen4lj75iywf6kxh8l8` (`interactome`),
  KEY `FK7q2wc7gxivblkb8b3117djryx` (`species`),
  KEY `FKkrjwgfrrkhptj43ex3yiat7ip` (`geneA`,`interactome`,`species`),
  KEY `FKtnwk6qwdvtao7vqp002k8f8uo` (`geneB`,`interactome`,`species`),
  CONSTRAINT `FK7q2wc7gxivblkb8b3117djryx` FOREIGN KEY (`species`) REFERENCES `species` (`id`),
  CONSTRAINT `FKekyhguctj9hvu5qevfo5v3rvj` FOREIGN KEY (`geneB`) REFERENCES `gene` (`id`),
  CONSTRAINT `FKkpnlx65nno3hups2cwudtmg90` FOREIGN KEY (`geneA`) REFERENCES `gene` (`id`),
  CONSTRAINT `FKkrjwgfrrkhptj43ex3yiat7ip` FOREIGN KEY (`geneA`, `interactome`, `species`) REFERENCES `gene_in_interactome` (`gene`, `interactome`, `species`),
  CONSTRAINT `FKno3mlamen4lj75iywf6kxh8l8` FOREIGN KEY (`interactome`) REFERENCES `interactome` (`id`),
  CONSTRAINT `FKtnwk6qwdvtao7vqp002k8f8uo` FOREIGN KEY (`geneB`, `interactome`, `species`) REFERENCES `gene_in_interactome` (`gene`, `interactome`, `species`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interaction_group_result`
--

DROP TABLE IF EXISTS `interaction_group_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interaction_group_result` (
  `geneAId` int(11) NOT NULL,
  `geneBId` int(11) NOT NULL,
  `interactionsResultId` varchar(255) NOT NULL,
  PRIMARY KEY (`geneAId`,`geneBId`,`interactionsResultId`),
  KEY `FK_interactions_result_interaction_group_result` (`interactionsResultId`),
  CONSTRAINT `FK_interactions_result_interaction_group_result` FOREIGN KEY (`interactionsResultId`) REFERENCES `interactions_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interaction_group_result_interactome`
--

DROP TABLE IF EXISTS `interaction_group_result_interactome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interaction_group_result_interactome` (
  `geneAId` int(11) NOT NULL,
  `geneBId` int(11) NOT NULL,
  `interactionsResultId` varchar(255) NOT NULL,
  `degree` int(11) NOT NULL,
  `interactomeDegrees_KEY` int(11) NOT NULL,
  PRIMARY KEY (`geneAId`,`geneBId`,`interactionsResultId`,`interactomeDegrees_KEY`),
  CONSTRAINT `FK_interaction_group_result_interactome` FOREIGN KEY (`geneAId`, `geneBId`, `interactionsResultId`) REFERENCES `interaction_group_result` (`geneAId`, `geneBId`, `interactionsResultId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interactions_result`
--

DROP TABLE IF EXISTS `interactions_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interactions_result` (
  `type` varchar(4) NOT NULL,
  `id` varchar(255) NOT NULL,
  `queryGeneId` int(11) NOT NULL,
  `queryMaxDegree` int(11) NOT NULL,
  `creationDateTime` datetime NOT NULL,
  `endDateTime` datetime DEFAULT NULL,
  `failureCause` varchar(255) DEFAULT NULL,
  `startDateTime` datetime DEFAULT NULL,
  `status` varchar(9) NOT NULL,
  PRIMARY KEY (`id`)
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
  `species` int(11) NOT NULL,
  `dbSourceIdType` varchar(100) DEFAULT NULL,
  `numOriginalInteractions` int(11) DEFAULT NULL,
  `numUniqueOriginalInteractions` int(11) DEFAULT NULL,
  `numUniqueOriginalGenes` int(11) DEFAULT NULL,
  `numInteractionsNotToUniProtKB` int(11) DEFAULT NULL,
  `numGenesNotToUniProtKB` int(11) DEFAULT NULL,
  `numInteractionsNotToGeneId` int(11) DEFAULT NULL,
  `numGenesNotToGeneId` int(11) DEFAULT NULL,
  `numFinalInteractions` int(11) DEFAULT NULL,
  `probFinalInteractions` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKp5x9ydinmkxbuy09ha9unypf7` (`name`,`species`),
  KEY `IDXauoiyeswmacye8t54i4uf98x0` (`id`,`species`),
  KEY `FK9tl8gsonmm6ksaxy4wt4xs8mr` (`species`),
  CONSTRAINT `FK9tl8gsonmm6ksaxy4wt4xs8mr` FOREIGN KEY (`species`) REFERENCES `species` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
-- Table structure for table `same_species_interactions_result`
--

DROP TABLE IF EXISTS `same_species_interactions_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `same_species_interactions_result` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK6yr100lq3edt1b38tu3g4inqc` FOREIGN KEY (`id`) REFERENCES `interactions_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `same_species_interactions_result_query_interactome`
--

DROP TABLE IF EXISTS `same_species_interactions_result_query_interactome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `same_species_interactions_result_query_interactome` (
  `interactionsResultId` varchar(255) NOT NULL,
  `interactomeId` int(11) NOT NULL,
  PRIMARY KEY (`interactionsResultId`,`interactomeId`),
  CONSTRAINT `FK_same_species_interactions_result_query_interactome` FOREIGN KEY (`interactionsResultId`) REFERENCES `same_species_interactions_result` (`id`)
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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

--
-- Table structure for table `work`
--

DROP TABLE IF EXISTS `work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `resultReference` varchar(1023) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL,
  `endDateTime` datetime DEFAULT NULL,
  `failureCause` varchar(255) DEFAULT NULL,
  `startDateTime` datetime DEFAULT NULL,
  `status` varchar(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_step`
--

DROP TABLE IF EXISTS `work_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_step` (
  `stepOrder` int(11) NOT NULL,
  `workId` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `progress` double DEFAULT NULL,
  PRIMARY KEY (`stepOrder`,`workId`),
  KEY `FK_work_work_step` (`workId`),
  CONSTRAINT `FK_work_work_step` FOREIGN KEY (`workId`) REFERENCES `work` (`id`)
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

-- Dump completed on 2018-02-11  0:17:25
