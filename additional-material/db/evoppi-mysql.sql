CREATE DATABASE IF NOT EXISTS evoppi
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

GRANT ALL ON evoppi.* TO evoppi@localhost IDENTIFIED BY 'evoppipass';
FLUSH PRIVILEGES;

CREATE TABLE evoppi.user (
  `role` varchar(10) NOT NULL,
  `login` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `administrator` (
  `login` varchar(100) NOT NULL,
  PRIMARY KEY (`login`),
  CONSTRAINT `FK_administrator_user` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `researcher` (
  `login` varchar(100) NOT NULL,
  PRIMARY KEY (`login`),
  CONSTRAINT `FK_researcher_user` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `gene` (
  `id` int(11) NOT NULL,
  `sequence` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `species` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_species_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `interactome` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `speciesId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_interactome_name` (`name`),
  KEY `FK_interactome_species` (`speciesId`),
  CONSTRAINT `FK_interactome_species` FOREIGN KEY (`speciesId`) REFERENCES `species` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `interaction` (
  `interactome` int(11) NOT NULL,
  `geneFrom` int(11) NOT NULL,
  `geneTo` int(11) NOT NULL,
  PRIMARY KEY (`interactome`,`geneFrom`,`geneTo`),
  KEY `FK_interaction_geneFrom_idx` (`geneFrom`),
  KEY `FK_interaction_geneTo_idx` (`geneTo`),
  CONSTRAINT `FK_interaction_interactome` FOREIGN KEY (`interactome`) REFERENCES `interactome` (`id`),
  CONSTRAINT `FK_interaction_geneFrom` FOREIGN KEY (`geneFrom`) REFERENCES `gene` (`id`),
  CONSTRAINT `FK_interaction_geneTo` FOREIGN KEY (`geneTo`) REFERENCES `gene` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
