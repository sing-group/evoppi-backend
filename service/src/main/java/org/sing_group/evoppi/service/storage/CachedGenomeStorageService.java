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

import static java.util.stream.Collectors.toList;
import static javax.ejb.ConcurrencyManagementType.BEAN;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.service.spi.storage.FastaWriter;
import org.sing_group.evoppi.service.spi.storage.GenomeStorageService;

@Singleton
@PermitAll
@ConcurrencyManagement(BEAN)
public class CachedGenomeStorageService implements GenomeStorageService {
  @Resource(name = "java:global/evoppi/storage/genome/path")
  private String genomeStoragePath;
  
  @Inject
  private FastaWriter fastaWriter;
  
  private final ConcurrentHashMap<Integer, ReentrantReadWriteLock> locks;
  
  public CachedGenomeStorageService() {
    this.locks = new ConcurrentHashMap<>();
  }
  
  @Override
  public Path getGenomePath(Species species) throws IOException {
    final Path speciesPath = this.getSpeciesPath(species.getId());
    
    final ReentrantReadWriteLock lock = this.locks.computeIfAbsent(species.getId(), id -> new ReentrantReadWriteLock());
    
    if (!Files.exists(speciesPath)) {
      final WriteLock writeLock = lock.writeLock();
      
      writeLock.lock();
      try {
        if (!Files.exists(speciesPath)) {
          final Path parentPath = speciesPath.getParent();
          
          if (!Files.isDirectory(parentPath)) {
            Files.createDirectories(parentPath);
          }
          
          this.fastaWriter.createFasta(species.getGenes().collect(toList()), speciesPath);
        }
      } finally {
        writeLock.unlock();
      }
    }
    
    final ReadLock readLock = lock.readLock();
    readLock.lock();
    try {
      return speciesPath;
    } finally {
      readLock.unlock();
    }
  }
  
  private Path getSpeciesPath(int speciesId) {
    return Paths.get(this.genomeStoragePath, "species", speciesId + ".fasta");
  }

}
