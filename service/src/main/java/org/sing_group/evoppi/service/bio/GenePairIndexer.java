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

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.REQUIRED;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionsResult;
import org.sing_group.evoppi.domain.entities.spi.bio.HasGenePairIds;

@Stateless
@PermitAll
@Transactional(REQUIRED)
public class GenePairIndexer {
  private InteractomeDAO interactomeDao;

  GenePairIndexer() {}
  
  public GenePairIndexer(InteractomeDAO interactomeDao) {
    this.setInteractomeDao(interactomeDao);
  }

  @Inject
  public void setInteractomeDao(InteractomeDAO interactomeDao) {
    this.interactomeDao = requireNonNull(interactomeDao);
  }
  
  public GenePairIndex createForInteractome(int interactomeId) {
    final Interactome interactome = this.interactomeDao.get(interactomeId);
    
    return this.createForInteractome(interactome);
  }
  
  public GenePairIndex createForInteractome(Interactome interactome) {
    final GenePairIndex index = new GenePairIndex();
    
    interactome.getInteractions()
      .forEach(interaction -> index.add(interaction));
    
    return index;
  }
  
  public GenePairValueIndex<Integer> createForInteractionsResult(InteractionsResult result) {
    final GenePairValueIndex<Integer> index = new GenePairValueIndex<>();
    
    result.getInteractions()
      .forEach(interaction -> index.add(
        interaction,
        interaction.getInteractomeIds().boxed().collect(toSet()))
      );
    
    return index;
  }
  
  public static class GenePairIndex {
    private final Map<Integer, Set<Integer>> index;
    
    public GenePairIndex() {
      this.index = new HashMap<>();
    }
    
    void add(HasGenePairIds genePairIds) {
      this.index.compute(genePairIds.getGeneAId(), createRemappingFunction(genePairIds.getGeneBId()));
    }
    
    public boolean has(HasGenePairIds genePairIds) {
      return this.index.getOrDefault(genePairIds.getGeneAId(), emptySet()).contains(genePairIds.getGeneBId());
    }
    
    private final static BiFunction<Integer, Set<Integer>, Set<Integer>> createRemappingFunction(int geneB) {
      return (geneA, genesB) -> {
        if (genesB == null) genesB = new HashSet<>();
        
        genesB.add(geneB);
        
        return genesB;
      };
    }
  }
  
  public static class GenePairValueIndex<T> {
    private final Map<Integer, Map<Integer, Set<T>>> index;
    
    public GenePairValueIndex() {
      this.index = new HashMap<>();
    }
    
    void add(HasGenePairIds genePairIds, Collection<T> values) {
      this.index.compute(genePairIds.getGeneAId(), createRemappingFunction(genePairIds.getGeneBId(), values));
    }
    
    public boolean has(HasGenePairIds genePairIds, T value) {
      return this.index.getOrDefault(genePairIds.getGeneAId(), emptyMap())
        .getOrDefault(genePairIds.getGeneBId(), emptySet())
      .contains(value);
    }
    
    public boolean hasAll(HasGenePairIds genePairIds, Collection<T> value) {
      return this.index.getOrDefault(genePairIds.getGeneAId(), emptyMap())
        .getOrDefault(genePairIds.getGeneBId(), emptySet())
      .containsAll(value);
    }
    
    private final static <T> BiFunction<Integer, Map<Integer, Set<T>>, Map<Integer, Set<T>>> createRemappingFunction(int geneB, Collection<T> value) {
      return (geneA, geneBMap) -> {
        if (geneBMap == null) {
          final Map<Integer, Set<T>> newMap = new HashMap<>();
          final Set<T> newSet = new HashSet<>();
          
          newSet.addAll(value);
          
          newMap.put(geneB, newSet);
          
          return newMap;
        } else {
          geneBMap.compute(geneB, createRemappingFunction(value));
          
          return geneBMap;
        }
      };
    }
    
    private final static <T> BiFunction<Integer, Set<T>, Set<T>> createRemappingFunction(Collection<T> value) {
      return (geneA, genesB) -> {
        if (genesB == null) genesB = new HashSet<>();
        
        genesB.addAll(value);
        
        return genesB;
      };
    }
  }
}
