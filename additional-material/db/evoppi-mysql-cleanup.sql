SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `work_step`;
TRUNCATE TABLE `work`;

TRUNCATE TABLE `blast_result`;

TRUNCATE TABLE `different_species_interactions_result_reference_genes`;
TRUNCATE TABLE `different_species_interactions_result_target_genes`;
TRUNCATE TABLE `different_species_interactions_result`;
TRUNCATE TABLE `same_species_interactions_result_query_interactome`;
TRUNCATE TABLE `same_species_interactions_result`;

TRUNCATE TABLE `interaction_group_result_interactome`;
TRUNCATE TABLE `interaction_group_result`;
TRUNCATE TABLE `interactions_result`;

TRUNCATE TABLE `interaction`;
TRUNCATE TABLE `gene_in_interactome`;
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

ALTER TABLE `blast_result` AUTO_INCREMENT = 1;
ALTER TABLE `interactome` AUTO_INCREMENT = 1;
ALTER TABLE `species` AUTO_INCREMENT = 1;
