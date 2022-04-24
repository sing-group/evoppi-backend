/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
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
package org.sing_group.evoppi.service.bio.species.pipeline;

import static java.lang.Integer.valueOf;
import static java.util.stream.Collectors.toList;
import static javax.transaction.Transactional.TxType.REQUIRED;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContext;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContextBuilder;
import org.sing_group.evoppi.service.spi.bio.species.SpeciesCreationContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SingleSpeciesCreationStep;
import org.sing_group.evoppi.service.spi.bio.species.pipeline.SpeciesCreationPipeline;
import org.sing_group.gbffparser.DefaultGbffParser;
import org.sing_group.gbffparser.GbffParser;
import org.sing_group.gbffparser.GbffParserException;
import org.sing_group.gbffparser.evoppi.EvoPpiGbffParserListener;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;

@Default
public class SpeciesProcessingStep
  implements SingleSpeciesCreationStep {

  @Inject
  private SpeciesCreationContextBuilderFactory contextBuilderFactory;

  @Override
  public String getStepId() {
    return SpeciesCreationPipeline.SINGLE_PROCESS_SPECIES;
  }

  @Override
  public String getName() {
    return "Process species";
  }

  @Override
  public int getOrder() {
    return 2;
  }

  @Override
  public boolean isComplete(SpeciesCreationContext context) {
    return context.getFasta().isPresent() && context.getDictionary().isPresent();
  }

  @Transactional(REQUIRED)
  @Override
  public SpeciesCreationContext execute(SpeciesCreationContext context) {
    File gbff =
      context.getGenomeFile()
        .orElseThrow(() -> new IllegalArgumentException("The genome file must be set before running this step"));

    try {
      String destName = gbff.getName();

      File fastaFile = new File("/tmp/", destName + ".fasta");
      File dictionaryFile = new File("/tmp/", destName + ".tsv");

      GbffParser parser = new DefaultGbffParser();
      EvoPpiGbffParserListener listener = new EvoPpiGbffParserListener(fastaFile, dictionaryFile);
      parser.addParseListener(listener);

      parser.parse(gbff, true);

      SpeciesCreationContextBuilder builder = this.contextBuilderFactory.createBuilderFor(context);

      if (listener.getTaxonomyId().isPresent()) {
        builder = builder.addTaxonomyId(valueOf(listener.getTaxonomyId().get()));
      } else {
        throw new IllegalArgumentException(
          "Taxonomy ID not found when processing " + context.getConfiguration().getData().getGbffGzipFileUrl()
        );
      }

      SequencesGroup fasta = parseFasta(fastaFile);
      Map<Integer, List<String>> dictionary = parseDictionary(dictionaryFile);

      builder = builder.addFasta(fasta).addDictionary(dictionary);

      return builder.build();
    } catch (GbffParserException | IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private static SequencesGroup parseFasta(File fastaFile) {
    return DatatypeFactory.getDefaultDatatypeFactory().newSequencesGroup(fastaFile.toPath());
  }

  private static Map<Integer, List<String>> parseDictionary(File dictionary) {
    Map<Integer, List<String>> toret = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(dictionary))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] split = line.split(" ");
        toret.put(Integer.valueOf(split[0]), Stream.of(split).skip(1).collect(toList()));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return toret;
  }
}
