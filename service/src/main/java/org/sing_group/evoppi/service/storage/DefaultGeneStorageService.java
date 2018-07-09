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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.service.spi.storage.FastaWriter;
import org.sing_group.evoppi.service.spi.storage.GeneStorageService;

@Stateless
@PermitAll
public class DefaultGeneStorageService implements GeneStorageService {
  @Resource(name = "java:global/evoppi/storage/fasta/path")
  private String genomeStoragePath;
  
  @Inject
  private FastaWriter fastaWriter;

  @PostConstruct
  public void initStorage() {
    final Path storagePath = Paths.get(this.genomeStoragePath);
    
    if (!Files.isDirectory(storagePath)) {
      try {
        Files.createDirectories(storagePath);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }      
    }
  }
  
  @Override
  public Path createFasta(Collection<Gene> genes) throws IOException {
    final UUID uuid = UUID.randomUUID();
    
    final Path path = Paths.get(this.genomeStoragePath, uuid.toString() + ".fasta");
    
    this.fastaWriter.createFasta(genes, path);
    
    return path;
  }

}
