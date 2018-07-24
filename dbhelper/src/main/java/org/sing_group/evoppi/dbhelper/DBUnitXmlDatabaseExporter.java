/*-
 * #%L
 * DB Helper
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.evoppi.dbhelper;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;

public class DBUnitXmlDatabaseExporter {
  public static void main(String[] args) throws SQLException, DatabaseUnitException, IOException {
    final String speciesIds = "1, 2";
    final String resultIds = "";//"'eba4a963-54c1-4b5e-8eaf-3f360687548b','9b2a10e7-c0b1-4239-80f9-f72d7e78109f'";
    
    final Optional<String> interactomeIds = Optional.empty(); // Optional.of("11,12,25,26");
    final Optional<String> geneIds = Optional.empty(); // Optional.of("41140,41311,40506,36329,44438,37998,39035,42446,26067077,26067080,3845,130,351,377,569,808,1024,1902,107983993,107987491");
    
    final int maxInteractomes = Integer.MAX_VALUE;
    final int maxGenesInInteractome = Integer.MAX_VALUE;
    final int maxGeneNames = Integer.MAX_VALUE;
    final int maxBlastSeqVersion = Integer.MAX_VALUE;
    
    try (Connection connection = DriverManager.getConnection(args[0], args[1], args[2])) {
      final IDatabaseConnection dbConnection = new DatabaseConnection(connection);
      final DatabaseConfig dbConfig = dbConnection.getConfig();
      
      dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
      
      final String speciesQuery = String.format("SELECT * FROM species WHERE id IN (%s)", speciesIds);
      final String interactomeLimitedQuery = generateIdOrLimitedQuery(interactomeIds, "interactome", "id", "species", speciesIds, "name", maxInteractomes);
      final String interactomeIdQuery = interactomeIds.orElse(interactomeLimitedQuery.replaceFirst("\\*", "id"));
      final String geneInInteractomeQuery = generateIdOrLimitedQuery(geneIds, "gene_in_interactome", "gene", "interactome", interactomeIdQuery, "gene", maxGenesInInteractome);
      final String geneInteractomeIdQuery = geneIds.orElse(geneInInteractomeQuery.replaceFirst("\\*", "gene"));
      final String geneQuery = String.format("SELECT * FROM gene WHERE id IN (%s)", geneInteractomeIdQuery);
      final String geneIdsQuery = geneIds.orElse(geneQuery.replaceFirst("\\*", "id"));
      final String geneNameQuery = String.format("SELECT * FROM gene_name WHERE geneId in (%s)", geneIdsQuery);
      final String geneNameValueLimitedQuery = generateLimitedQuery("gene_name_value", "geneId", geneIdsQuery, "name", maxGeneNames);
      final String geneSequenceQuery = String.format("SELECT * FROM gene_sequence WHERE geneId in (%s) AND version <= 2", geneIdsQuery);
      final String interactionQuery = String.format("SELECT * FROM interaction WHERE geneA IN (%s) AND geneB IN (%s) AND interactome IN (%s)", geneIdsQuery, geneIdsQuery, interactomeIdQuery);
      final String interactionGroupResult = String.format("SELECT * FROM interaction_group_result WHERE geneA IN (%s) AND geneB IN (%s) AND interactionsResultId IN (%s)", geneIdsQuery, geneIdsQuery, resultIds);
      final String interactionGroupResultInteractomeDegree = String.format("SELECT * FROM interaction_group_result_interactome_degree WHERE geneA IN (%s) AND geneB IN (%s) AND interactionsResultId IN (%s)", geneIdsQuery, geneIdsQuery, resultIds);
      final String differentSpeciesInteractionsResult = String.format("SELECT * FROM different_species_interactions_result WHERE id IN (%s)", resultIds);
      final String differentSpeciesInteractionsResultReferenceInteractomes = String.format("SELECT * FROM different_species_interactions_result_reference_interactomes WHERE resultId IN (%s)", resultIds);
      final String differentSpeciesInteractionsResultTargetInteractomes = String.format("SELECT * FROM different_species_interactions_result_target_interactomes WHERE resultId IN (%s)", resultIds);
      final String sameSpeciesInteractionsResult = String.format("SELECT * FROM same_species_interactions_result WHERE id IN (%s)", resultIds);
      final String sameSpeciesInteractionsResultQueryInteractome = String.format("SELECT * FROM same_species_interactions_result_query_interactome WHERE resultId IN (%s)", resultIds);
      final String workStep = String.format("SELECT * FROM work_step WHERE workId IN (%s)", resultIds);
      final String blastResult = String.format("SELECT * FROM blast_result WHERE interactionsResultId IN (%s) AND sseqid IN (%s) AND qseqid IN (%s)", resultIds, geneIdsQuery, geneIdsQuery, maxBlastSeqVersion, maxBlastSeqVersion);
      
      final QueryDataSet query = new QueryDataSet(dbConnection);
      query.addTable("user");
      query.addTable("administrator");
      query.addTable("researcher");
      query.addTable("species", speciesQuery);
      query.addTable("interactome", interactomeLimitedQuery);
      query.addTable("gene", geneQuery);
      query.addTable("gene_sequence", geneSequenceQuery);
      query.addTable("gene_name", geneNameQuery);
      query.addTable("gene_name_value", geneNameValueLimitedQuery);
      query.addTable("gene_in_interactome", geneInInteractomeQuery);
      query.addTable("interaction", interactionQuery);
      if (!resultIds.isEmpty()) {
        query.addTable("interaction_group_result", interactionGroupResult);
        query.addTable("interaction_group_result_interactome_degree", interactionGroupResultInteractomeDegree);
        query.addTable("work_step", workStep);
        query.addTable("same_species_interactions_result", sameSpeciesInteractionsResult);
        query.addTable("same_species_interactions_result_query_interactome", sameSpeciesInteractionsResultQueryInteractome);
        query.addTable("different_species_interactions_result", differentSpeciesInteractionsResult);
        query.addTable("different_species_interactions_result_reference_interactomes", differentSpeciesInteractionsResultReferenceInteractomes);
        query.addTable("different_species_interactions_result_target_interactomes", differentSpeciesInteractionsResultTargetInteractomes);
        query.addTable("blast_result", blastResult);
      }

      final StringWriter writer = new StringWriter();
      FlatXmlDataSet.write(query, writer);
      
      /* Uncomment for full database
      final IDataSet dataset = dbConnection.createDataSet();
      final StringWriter writer = new StringWriter();
      FlatXmlDataSet.write(dataset, writer);
      */
      
      System.out.println(writer.toString());
    }
  }
  
  private static String generateIdOrLimitedQuery(Optional<String> ids, String table, String idColumn, String commonColumn, String commonValues, String filteringColumn, int count) {
    if (ids.isPresent()) {
      return String.format("SELECT * FROM %s WHERE %s IN (%s) AND %s IN (%s)", table, idColumn, ids.get(), commonColumn, commonValues);
    } else {
      return generateLimitedQuery(table, commonColumn, commonValues, filteringColumn, count);
    }
  }
  
  private static String generateLimitedQuery(String table, String commonColumn, String commonValues, String filteringColumn, int count) {
    return String.format(
      "SELECT * FROM %s t1 WHERE t1.%s IN (%s) "
        + "AND (SELECT COUNT(*) FROM %s t2 WHERE t1.%s = t2.%s AND t1.%s < t2.%s) < %s",
      table, commonColumn, commonValues, table, commonColumn, commonColumn, filteringColumn, filteringColumn, count
    );
  }
}
