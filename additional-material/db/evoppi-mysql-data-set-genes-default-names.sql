USE `evoppi`;

UPDATE gene g
SET g.defaultName = (SELECT name FROM gene_name_value WHERE geneId = g.id ORDER BY IF(name RLIKE '^[a-z]', 1, 2), name LIMIT 1);
