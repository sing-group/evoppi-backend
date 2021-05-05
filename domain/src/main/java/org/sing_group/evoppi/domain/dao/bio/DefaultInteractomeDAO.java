/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

import static java.util.Collections.singleton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneInInteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractionDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.DifferentSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.SameSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.interactome.GeneInteractions;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultInteractomeDAO implements InteractomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Interactome> dh;

  @Inject
  private GeneDAO geneDao;

  @Inject
  private GeneInInteractomeDAO geneInInteractomeDao;
  
  @Inject
  private InteractionDAO interactionDao;
  
  @Inject
  private SameSpeciesInteractionsResultDAO sameSpeciesInteractionsResultDao;
  
  @Inject
  private DifferentSpeciesInteractionsResultDAO differentSpeciesInteractionsResultDao;

  public DefaultInteractomeDAO() {
    super();
  }

  public DefaultInteractomeDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Interactome.class, this.em);
  }

  @Override
  public Stream<Interactome> listInteractomes(ListingOptions<Interactome> listingOptions) {
    return this.dh.list(listingOptions).stream();
  }

  @Override
  public Interactome getInteractome(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown interactome: " + id));
  }

  @Override
  @Transactional(dontRollbackOn = IllegalArgumentException.class)
  public Interactome getInteractomeByName(String name) {
    try {
      return this.dh.getBy("name", name);
    } catch (NoResultException e) {
      throw new IllegalArgumentException("Unknown interactome: " + name, e);
    }
  }

  @Override
  public long count(ListingOptions<Interactome> interactomeListingOptions) {
    return this.dh.count(interactomeListingOptions);
  }

  @Override
  public Interactome createInteractome(
    String name, String dbSourceIdType, Integer numOriginalInteractions, Integer numUniqueOriginalInteractions,
    Integer numUniqueOriginalGenes, Integer numInteractionsNotToUniProtKB, Integer numGenesNotToUniProtKB,
    Integer numInteractionsNotToGeneId, Integer numGenesNotToGeneId, Integer numFinalInteractions,
    Integer numFinalGenes, Integer numRemovedInterSpeciesInteractions, Integer numMultimappedToGeneId,
    Species species, GeneInteractions interactions
  ) {
    Interactome interactome =
      this.dh.persist(
        new Interactome(
          name, dbSourceIdType, numOriginalInteractions, numUniqueOriginalInteractions, numUniqueOriginalGenes,
          numInteractionsNotToUniProtKB, numGenesNotToUniProtKB, numInteractionsNotToGeneId, numGenesNotToGeneId,
          numFinalInteractions, numFinalGenes, numRemovedInterSpeciesInteractions, numMultimappedToGeneId,
          species
        )
      );

    Map<Integer, Gene> geneMap = new HashMap<>();

    interactions.getGenes().forEach(gene -> {
      try {
        Gene geneEntity = geneDao.getGene(gene);
        geneMap.put(gene, geneEntity);
        this.geneInInteractomeDao.create(species, interactome, geneEntity);
      } catch (IllegalArgumentException e) {}
    });

    interactions.forEach(interaction -> {
      Gene geneA = geneMap.get(interaction.getA());
      Gene geneB = geneMap.get(interaction.getB());

      if (geneA != null && geneB != null) {
        interactome.addInteraction(species, geneA, geneB);
      }
    });

    geneMap.clear();

    return interactome;
  }
  
  @Override
  public void deleteInteractome(int interactomeId) {
    this.deleteInteractomes(singleton(interactomeId));
  }

  @Override
  public void deleteInteractomes(Collection<Integer> interactomeIds) {
    this.sameSpeciesInteractionsResultDao.deleteResultsByInteractomes(interactomeIds);
    this.differentSpeciesInteractionsResultDao.deleteResultsByInteractomes(interactomeIds);
    this.interactionDao.deleteInteractionsByInteractomes(interactomeIds);
    this.geneInInteractomeDao.deleteGeneInInteractomesByInteractomes(interactomeIds);
    this.dh.deleteBy("id", interactomeIds);
  }
}
