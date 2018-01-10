DELETE FROM `interaction`;
DELETE FROM `interactome`;
DELETE FROM `species`;
DELETE FROM `gene`;

DELETE FROM `researcher`;
DELETE FROM `administrator`;
DELETE FROM `user`;

ALTER TABLE `species` AUTO_INCREMENT = 1;
ALTER TABLE `interactome` AUTO_INCREMENT = 1;