SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `work_step`;
TRUNCATE TABLE `work`;

TRUNCATE TABLE `interaction_group_result_interactome`;
TRUNCATE TABLE `interaction_group_result`;
TRUNCATE TABLE `interactions_result_query_interactome`;
TRUNCATE TABLE `interaction_group_result`;

TRUNCATE TABLE `interaction`;
TRUNCATE TABLE `interactome`;
TRUNCATE TABLE `gene_name_value`;
TRUNCATE TABLE `gene_name`;
TRUNCATE TABLE `gene_sequence`;
TRUNCATE TABLE `gene`;
TRUNCATE TABLE `species`;

TRUNCATE TABLE `researcher`;
TRUNCATE TABLE `administrator`;
TRUNCATE TABLE `user`;
SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE `interactions_result` AUTO_INCREMENT = 1;
ALTER TABLE `interactome` AUTO_INCREMENT = 1;
ALTER TABLE `species` AUTO_INCREMENT = 1;
ALTER TABLE `work` AUTO_INCREMENT = 1;