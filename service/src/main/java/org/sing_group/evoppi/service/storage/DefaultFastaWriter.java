/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.storage;

import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneSequence;
import org.sing_group.evoppi.service.spi.storage.FastaOutputConfiguration;
import org.sing_group.evoppi.service.spi.storage.FastaWriter;
import org.sing_group.fluent.compare.Compare;

@Default
public class DefaultFastaWriter implements FastaWriter {
  @Resource(name = "java:global/evoppi/storage/file/charset")
  private String fileCharset;

  @Resource(name = "java:global/evoppi/storage/file/lineSeparator")
  private String lineSeparator;

  private String newLine() {
    switch (this.lineSeparator) {
      case "unix":
        return "\n";
      case "windows":
        return "\r\n";
      default:
        throw new IllegalStateException("Invalid line separator type: " + this.lineSeparator);
    }
  }

  @Override
  public void createFasta(Collection<Gene> genes, Path path, FastaOutputConfiguration config) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName(this.fileCharset), CREATE)) {
      this.createFasta(genes, writer, config);
    }
  }

  @Override
  public void createFasta(Collection<Gene> genes, Writer writer, FastaOutputConfiguration config) throws IOException {
    try {
      final Consumer<String> writeLine = line -> {
        try {
          writer.append(line).append(this.newLine());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      };

      Stream<GeneSequence> sequences =
        genes.stream()
          .flatMap(Gene::getGeneSequence);

      if (config.isSorted()) {
        sequences =
          sequences
            .sorted((gs1, gs2) -> Compare.objects(gs1, gs2)
              .by(GeneSequence::getGeneId)
              .thenBy(GeneSequence::getVersion)
              .andGet()
            );
      }

      sequences.forEachOrdered(geneSequence -> appendGeneSequence(geneSequence, config, writeLine));
    } catch (RuntimeException re) {
      if (re.getCause() instanceof IOException) {
        throw (IOException) re.getCause();
      } else {
        throw re;
      }
    }
  }

  private static void appendGeneSequence(
    GeneSequence geneSequence, FastaOutputConfiguration config, Consumer<String> lineConsumer
  ) {
    final StringBuilder idBuilder =
      new StringBuilder()
        .append('>').append(geneSequence.getGene().getId());

    if (config.isIncludeVersionSuffix())
      idBuilder.append('_').append(geneSequence.getVersion());

    lineConsumer.accept(idBuilder.toString());
    wrapText(geneSequence.getSequence(), 80, lineConsumer);
  }

  private static void wrapText(String text, int columns, Consumer<String> lineConsumer) {
    while (text.length() >= columns) {
      lineConsumer.accept(text.substring(0, columns));

      text = text.substring(columns);
    }

    if (!text.isEmpty()) {
      lineConsumer.accept(text);
    }
  }
}
