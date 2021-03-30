-- MySQL dump 10.13  Distrib 5.7.33, for Linux (x86_64)
--
-- Host: localhost    Database: evoppi
-- ------------------------------------------------------
-- Server version	5.7.33-0ubuntu0.18.04.1

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
-- Current Database: `evoppi`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `evoppi` /*!40100 DEFAULT CHARACTER SET utf8 */;

ALTER DATABASE evoppi CHARACTER SET = utf8;

USE `evoppi`;

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
  `interactionsResultId` char(36) DEFAULT NULL,
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
  `id` char(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `resultReference` varchar(1023) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL,
  `failureCause` varchar(255) DEFAULT NULL,
  `finishingDateTime` datetime DEFAULT NULL,
  `schedulingDateTime` datetime DEFAULT NULL,
  `startDateTime` datetime DEFAULT NULL,
  `status` varchar(9) NOT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `queryMaxDegree` int(11) NOT NULL,
  `queryGene` int(11) NOT NULL,
  `evalue` double DEFAULT NULL,
  `maxTargetSeqs` int(11) DEFAULT NULL,
  `minimumAlignmentLength` int(11) DEFAULT NULL,
  `minimumIdentity` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_c80h74nea2044bfsgv075i85k` (`queryGene`),
  KEY `FK_fdpxtpw6shpokegxaghqhggk3` (`owner`),
  CONSTRAINT `FK_c80h74nea2044bfsgv075i85k` FOREIGN KEY (`queryGene`) REFERENCES `gene` (`id`),
  CONSTRAINT `FK_fdpxtpw6shpokegxaghqhggk3` FOREIGN KEY (`owner`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `different_species_interactions_result_reference_interactomes`
--

DROP TABLE IF EXISTS `different_species_interactions_result_reference_interactomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `different_species_interactions_result_reference_interactomes` (
  `resultId` char(36) NOT NULL,
  `referenceInteractomeId` int(11) NOT NULL,
  PRIMARY KEY (`resultId`,`referenceInteractomeId`),
  KEY `FKln6lvc23kfx8vxthanwlr60tc` (`referenceInteractomeId`),
  CONSTRAINT `FK_different_species_interactions_result_reference_interactomes` FOREIGN KEY (`resultId`) REFERENCES `different_species_interactions_result` (`id`),
  CONSTRAINT `FKln6lvc23kfx8vxthanwlr60tc` FOREIGN KEY (`referenceInteractomeId`) REFERENCES `interactome` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `different_species_interactions_result_target_interactomes`
--

DROP TABLE IF EXISTS `different_species_interactions_result_target_interactomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `different_species_interactions_result_target_interactomes` (
  `resultId` char(36) NOT NULL,
  `targetInteractomeId` int(11) NOT NULL,
  PRIMARY KEY (`resultId`,`targetInteractomeId`),
  KEY `FKmjooibj4syiguo4vd6we3qxaw` (`targetInteractomeId`),
  CONSTRAINT `FK_different_species_interactions_result_target_interactomes` FOREIGN KEY (`resultId`) REFERENCES `different_species_interactions_result` (`id`),
  CONSTRAINT `FKmjooibj4syiguo4vd6we3qxaw` FOREIGN KEY (`targetInteractomeId`) REFERENCES `interactome` (`id`)
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
  `defaultName` varchar(255) NOT NULL,
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
  `geneA` int(11) NOT NULL,
  `geneB` int(11) NOT NULL,
  `interactionsResultId` varchar(255) NOT NULL,
  PRIMARY KEY (`geneA`,`geneB`,`interactionsResultId`),
  KEY `interactionsResultIdIndex` (`interactionsResultId`),
  KEY `FKirn6ve0wwqeo4tlsj9gffwjr3` (`geneB`),
  CONSTRAINT `FKgs914csec0vv06ex5u68rtida` FOREIGN KEY (`geneA`) REFERENCES `gene` (`id`),
  CONSTRAINT `FKirn6ve0wwqeo4tlsj9gffwjr3` FOREIGN KEY (`geneB`) REFERENCES `gene` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interaction_group_result_interactome_degree`
--

DROP TABLE IF EXISTS `interaction_group_result_interactome_degree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interaction_group_result_interactome_degree` (
  `geneA` int(11) NOT NULL,
  `geneB` int(11) NOT NULL,
  `interactionsResultId` varchar(255) NOT NULL,
  `interactome` int(11) NOT NULL,
  `degree` int(11) NOT NULL,
  PRIMARY KEY (`geneA`,`geneB`,`interactionsResultId`,`interactome`),
  KEY `FKdhcbijsnjefu7crhnebytksqe` (`geneB`),
  KEY `FKbbxa2nb2babhbdm6f7nkov2xe` (`interactome`),
  CONSTRAINT `FKbbxa2nb2babhbdm6f7nkov2xe` FOREIGN KEY (`interactome`) REFERENCES `interactome` (`id`),
  CONSTRAINT `FKd7df4ktolw1egapn36sxxor15` FOREIGN KEY (`geneA`) REFERENCES `gene` (`id`),
  CONSTRAINT `FKdhcbijsnjefu7crhnebytksqe` FOREIGN KEY (`geneB`) REFERENCES `gene` (`id`),
  CONSTRAINT `FKnjpfuh3o3kbn3v71d2924w5yq` FOREIGN KEY (`geneA`, `geneB`, `interactionsResultId`) REFERENCES `interaction_group_result` (`geneA`, `geneB`, `interactionsResultId`)
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
  `dbSourceIdType` varchar(100) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `numFinalGenes` int(11) DEFAULT NULL,
  `numFinalInteractions` int(11) DEFAULT NULL,
  `numGenesNotToGeneId` int(11) DEFAULT NULL,
  `numGenesNotToUniProtKB` int(11) DEFAULT NULL,
  `numInteractionsNotToGeneId` int(11) DEFAULT NULL,
  `numInteractionsNotToUniProtKB` int(11) DEFAULT NULL,
  `numMultimappedToGeneId` int(11) DEFAULT NULL,
  `numOriginalInteractions` int(11) DEFAULT NULL,
  `numRemovedInterSpeciesInteractions` int(11) DEFAULT NULL,
  `numUniqueOriginalGenes` int(11) DEFAULT NULL,
  `numUniqueOriginalInteractions` int(11) DEFAULT NULL,
  `species` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKp5x9ydinmkxbuy09ha9unypf7` (`name`,`species`),
  KEY `IDXauoiyeswmacye8t54i4uf98x0` (`id`,`species`),
  KEY `FK9tl8gsonmm6ksaxy4wt4xs8mr` (`species`),
  CONSTRAINT `FK9tl8gsonmm6ksaxy4wt4xs8mr` FOREIGN KEY (`species`) REFERENCES `species` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `password_recovery`
--

DROP TABLE IF EXISTS `password_recovery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password_recovery` (
  `login` varchar(100) NOT NULL,
  `code` char(36) NOT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_ioxwo72pfnn918c1xs3302yl2` (`code`),
  CONSTRAINT `FK2j66785rlmdk332oh150nflew` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registration`
--

DROP TABLE IF EXISTS `registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registration` (
  `login` varchar(100) NOT NULL,
  `code` char(36) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` char(32) NOT NULL,
  `registrationDateTime` datetime NOT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_mwjjl8530nsph5lshrjv6dthm` (`code`),
  UNIQUE KEY `UK_pqp6404l2ndskpsr1xx8eaa68` (`email`)
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
  `id` char(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `resultReference` varchar(1023) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL,
  `failureCause` varchar(255) DEFAULT NULL,
  `finishingDateTime` datetime DEFAULT NULL,
  `schedulingDateTime` datetime DEFAULT NULL,
  `startDateTime` datetime DEFAULT NULL,
  `status` varchar(9) NOT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `queryMaxDegree` int(11) NOT NULL,
  `queryGene` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6tnwpwpj40p4rqif4fij807fc` (`queryGene`),
  KEY `FK_ppmdhee2so3jk9u2ggkj3db5u` (`owner`),
  CONSTRAINT `FK_6tnwpwpj40p4rqif4fij807fc` FOREIGN KEY (`queryGene`) REFERENCES `gene` (`id`),
  CONSTRAINT `FK_ppmdhee2so3jk9u2ggkj3db5u` FOREIGN KEY (`owner`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `same_species_interactions_result_query_interactome`
--

DROP TABLE IF EXISTS `same_species_interactions_result_query_interactome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `same_species_interactions_result_query_interactome` (
  `resultId` char(36) NOT NULL,
  `queryInteractomesId` int(11) NOT NULL,
  PRIMARY KEY (`resultId`,`queryInteractomesId`),
  KEY `FKlrtx0d2g1he5hme5plmlu38lq` (`queryInteractomesId`),
  CONSTRAINT `FK_same_species_interactions_result_query_interactome` FOREIGN KEY (`resultId`) REFERENCES `same_species_interactions_result` (`id`),
  CONSTRAINT `FKlrtx0d2g1he5hme5plmlu38lq` FOREIGN KEY (`queryInteractomesId`) REFERENCES `interactome` (`id`)
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
  `taxonomyId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_29ixq8ot8e88rk6v7jpkisgr3` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `login` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` char(32) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_interactome_creation`
--

DROP TABLE IF EXISTS `work_interactome_creation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_interactome_creation` (
  `id` char(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `resultReference` varchar(1023) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL,
  `failureCause` varchar(255) DEFAULT NULL,
  `finishingDateTime` datetime DEFAULT NULL,
  `schedulingDateTime` datetime DEFAULT NULL,
  `startDateTime` datetime DEFAULT NULL,
  `status` varchar(9) NOT NULL,
  `owner` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_d0tbrbpqboshjjqudta738thi` (`owner`),
  CONSTRAINT `FK_d0tbrbpqboshjjqudta738thi` FOREIGN KEY (`owner`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_species_creation`
--

DROP TABLE IF EXISTS `work_species_creation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_species_creation` (
  `id` char(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `resultReference` varchar(1023) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL,
  `failureCause` varchar(255) DEFAULT NULL,
  `finishingDateTime` datetime DEFAULT NULL,
  `schedulingDateTime` datetime DEFAULT NULL,
  `startDateTime` datetime DEFAULT NULL,
  `status` varchar(9) NOT NULL,
  `owner` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n461r772b1m0t6f46pime3r9n` (`owner`),
  CONSTRAINT `FK_n461r772b1m0t6f46pime3r9n` FOREIGN KEY (`owner`) REFERENCES `user` (`login`)
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
  PRIMARY KEY (`stepOrder`,`workId`)
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

-- Dump completed on 2021-03-30  9:19:23
