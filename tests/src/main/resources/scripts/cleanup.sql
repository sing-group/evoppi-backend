
-- #%L
-- Tests
-- %%
-- Copyright (C) 2017 Miguel Reboiro-Jato and Adolfo Piñón Blanco
-- %%
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as
-- published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
-- 
-- You should have received a copy of the GNU General Public
-- License along with this program.  If not, see
-- <http://www.gnu.org/licenses/gpl-3.0.html>.
-- #L%
DELETE `work_step`;

DELETE `blast_result`;

DELETE `interaction_group_result_interactome_degree`;
DELETE `interaction_group_result`;

DELETE `different_species_interactions_result_reference_interactomes`;
DELETE `different_species_interactions_result_target_interactomes`;
DELETE `different_species_interactions_result`;
DELETE `same_species_interactions_result_query_interactome`;
DELETE `same_species_interactions_result`;

DELETE `interaction`;
DELETE `gene_in_interactome`;
DELETE `gene_name_value`;
DELETE `gene_name`;
DELETE `gene_sequence`;
DELETE `gene`;
DELETE `interactome`;
DELETE `species`;

DELETE `researcher`;
DELETE `administrator`;
DELETE `user`;
