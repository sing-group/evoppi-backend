/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.domain.dao.bio;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.DatabaseInteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneInInteractomeDAO;
import org.sing_group.evoppi.domain.entities.bio.DatabaseInteractome;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneInInteractome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.interactome.GeneInteractions;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultDatabaseInteractomeDAO implements DatabaseInteractomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, DatabaseInteractome> dh;

  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private GeneInInteractomeDAO geneInInteractomeDao;
  
  public DefaultDatabaseInteractomeDAO() {
    super();
  }

  public DefaultDatabaseInteractomeDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, DatabaseInteractome.class, this.em);
  }

  @Override
  public Stream<DatabaseInteractome> list(ListingOptions<DatabaseInteractome> listingOptions) {
    return this.dh.list(listingOptions).stream();
  }

  @Override
  public DatabaseInteractome get(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown database interactome: " + id));
  }

  @Override
  public long count(ListingOptions<DatabaseInteractome> listingOptions) {
    return this.dh.count(listingOptions);
  }
  
  @Override
  public DatabaseInteractome create(
    String name, String dbSourceIdType, Integer numOriginalInteractions, Integer numUniqueOriginalInteractions,
    Integer numUniqueOriginalGenes, Integer numInteractionsNotToUniProtKB, Integer numGenesNotToUniProtKB,
    Integer numInteractionsNotToGeneId, Integer numGenesNotToGeneId, Integer numFinalInteractions,
    Integer numFinalGenes, Integer numRemovedInterSpeciesInteractions, Integer numMultimappedToGeneId,
    Species species, GeneInteractions interactions
  ) {
    DatabaseInteractome interactome =
      this.dh.persist(
        new DatabaseInteractome(
          name, dbSourceIdType, numOriginalInteractions, numUniqueOriginalInteractions, numUniqueOriginalGenes,
          numInteractionsNotToUniProtKB, numGenesNotToUniProtKB, numInteractionsNotToGeneId, numGenesNotToGeneId,
          numFinalInteractions, numFinalGenes, numRemovedInterSpeciesInteractions, numMultimappedToGeneId,
          species, species
        )
      );

    Map<Integer, Gene> geneMap = new HashMap<>();
    Map<Gene, GeneInInteractome> geneInInteractomeMap = new HashMap<>();

    interactions.getGenes().forEach(gene -> {
      try {
        Gene geneEntity = geneDao.getGene(gene);
        GeneInInteractome gi = this.geneInInteractomeDao.create(species, interactome, geneEntity);

        geneMap.put(gene, geneEntity);
        geneInInteractomeMap.put(geneEntity, gi);
      } catch (IllegalArgumentException e) {}
    });

    interactions.forEach(interaction -> {
      Gene geneA = geneMap.get(interaction.getA());
      Gene geneB = geneMap.get(interaction.getB());

      if (geneA != null && geneB != null) {
        interactome.addInteraction(species, species, geneA, geneB, geneInInteractomeMap.get(geneA), geneInInteractomeMap.get(geneB));
      }
    });

    geneMap.clear();

    return interactome;
  }
}
