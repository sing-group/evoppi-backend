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
package org.sing_group.evoppi.service.bio;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static javax.transaction.Transactional.TxType.NEVER;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.entities.bio.execution.BlastQueryOptions;
import org.sing_group.evoppi.domain.entities.bio.execution.BlastResult;
import org.sing_group.evoppi.service.spi.bio.BlastService;
import org.sing_group.evoppi.service.spi.execution.DockerExecutor;

@Default
@Transactional(NEVER)
public class DockerBlastService implements BlastService {
  @Resource(name = "java:global/evoppi/docker/path/shared/host")
  private String sharedPathHost;

  @Resource(name = "java:global/evoppi/docker/path/shared/container/input")
  private String sharedPathContainerInput;
  
  @Resource(name = "java:global/evoppi/docker/path/shared/container/output")
  private String sharedPathContainerOutput;
  
  @Resource(name = "java:global/evoppi/docker/blast/makeblastdb/command")
  private String makeblastdbCmd;
  
  @Resource(name = "java:global/evoppi/docker/blast/blast/command")
  private String blastCmd;
  
  @Resource(name = "java:global/evoppi/docker/blast/file/deleteOnFinish")
  private boolean deleteOnFinish;

  @Inject
  private DockerExecutor executor;
  
  @Override
  public Stream<BlastResult> blast(
    Path genome, Path genes,
    BlastQueryOptions blastQueryOptions
  ) throws IOException {
    final String uuid = UUID.randomUUID().toString();
    final String genomeName = "genome.fasta";
    final String genesName = "input.fasta";
    
    final Path hostOutputPath = Paths.get(this.sharedPathHost, uuid);
    final Path containerInputPath = Paths.get(this.sharedPathContainerInput, uuid);
    final Path containerOutputPath = Paths.get(this.sharedPathContainerOutput);
    
    final Path containerGenomePath = containerInputPath.resolve(genomeName);
    final Path containerGenesPath = containerInputPath.resolve(genesName);
    
    final Path containerDatabasePath = containerOutputPath.resolve("database");
    final Path containerBlastOutputPath = containerOutputPath.resolve("output");
    
    final Path hostBlastOutputPath = hostOutputPath.resolve("output");
    
    final Map<String, String> makeblastReplacements = new HashMap<>();
    makeblastReplacements.put("[INPUT]", containerGenomePath.toString());
    makeblastReplacements.put("[OUTPUT]", containerDatabasePath.toString());
    
    final String[] makeblast = generateCommand(this.makeblastdbCmd, makeblastReplacements);
    
    final Map<String, String> blastReplacements = new HashMap<>();
    blastReplacements.put("[QUERY]", containerGenesPath.toString());
    blastReplacements.put("[DATABASE]", containerDatabasePath.toString());
    blastReplacements.put("[EVALUE]", Double.toString(blastQueryOptions.getEvalue()));
    blastReplacements.put("[MAX_TARGET_SEQS]", Integer.toString(blastQueryOptions.getMaxTargetSeqs()));
    blastReplacements.put("[OUTPUT]", containerBlastOutputPath.toString());
    
    final String[] blast = generateCommand(this.blastCmd, blastReplacements);
    
    final Map<Path, Path> mounts = new HashMap<>();
    mounts.put(hostOutputPath, Paths.get(this.sharedPathContainerOutput));
    mounts.put(genome, containerGenomePath);
    mounts.put(genes, containerGenesPath);

    try {
      Files.createDirectories(hostOutputPath);
      
      this.executor.exec(mounts, makeblast);
      this.executor.exec(mounts, blast);
      
      return parseBlast(hostBlastOutputPath)
        .filter(result -> result.getPident() >= blastQueryOptions.getMinimumIdentity() * 100d)
        .filter(result -> result.getLength() >= blastQueryOptions.getMinimumAlignmentLength());
    } finally {
      if (this.deleteOnFinish)
        deleteDirectory(hostOutputPath);
    }
  }
  
  private static void deleteDirectory(Path directory) throws IOException {
    if (Files.isDirectory(directory)) {
      try {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            
            return FileVisitResult.CONTINUE;
          }
          
          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            
            return FileVisitResult.CONTINUE;
          }
        });
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    } else {
      throw new IllegalArgumentException("Path is not a directory: " + directory);
    }
  }
  
  private static Stream<BlastResult> parseBlast(Path blastOutput) throws IOException {
    try (Stream<String> lines = Files.lines(blastOutput)) {
      final List<BlastResult> results = lines
        .map(line -> line.split("\\s+"))
        .map(values -> {
          final String[] qseqid = values[0].split("_");
          final String[] sseqid = values[1].split("_");
          
          return new BlastResult(
            Integer.parseInt(qseqid[0]),
            Integer.parseInt(qseqid[1]),
            Integer.parseInt(sseqid[0]),
            Integer.parseInt(sseqid[1]),
            Double.parseDouble(values[2]),
            Integer.parseInt(values[3]),
            Integer.parseInt(values[4]),
            Integer.parseInt(values[5]),
            Integer.parseInt(values[6]),
            Integer.parseInt(values[7]),
            Integer.parseInt(values[8]),
            Integer.parseInt(values[9]),
            new BigDecimal(values[10]),
            Double.parseDouble(values[11])
          );
        })
      .collect(toList());
      
      return results.stream();
    }
  }

  private static String[] generateCommand(String command, Map<String, String> replacements) {
    return stream(command.split("\\s+"))
      .map(value -> replacements.getOrDefault(value, value))
    .toArray(String[]::new);
  }
}
