/*-
 * #%L
 * Domain
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
import org.sing_group.evoppi.domain.dao.spi.bio.GeneDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneInInteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.PredictomeDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneInInteractome;
import org.sing_group.evoppi.domain.entities.bio.InteractomeCollection;
import org.sing_group.evoppi.domain.entities.bio.Predictome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.interactome.GeneInteractions;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPredictomeDAO implements PredictomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Predictome> dh;

  @Inject
  private GeneDAO geneDao;

  @Inject
  private GeneInInteractomeDAO geneInInteractomeDao;

  public DefaultPredictomeDAO() {
    super();
  }

  public DefaultPredictomeDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Predictome.class, this.em);
  }

  @Override
  public Stream<Predictome> list(ListingOptions<Predictome> listingOptions) {
    return this.dh.list(listingOptions).stream();
  }

  @Override
  public Predictome get(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown predictome: " + id));
  }

  @Override
  public long count(ListingOptions<Predictome> interactomeListingOptions) {
    return this.dh.count(interactomeListingOptions);
  }

  @Override
  public Predictome create(
    String name, Species speciesA, Species speciesB, String sourceInteractome, String conversionDatabase,
    GeneInteractions interactions, InteractomeCollection collection
  ) {
    Predictome predictome =
      this.dh.persist(
        new Predictome(name, speciesA, speciesB, sourceInteractome, conversionDatabase, collection)
      );

    Map<Integer, Gene> geneMap = new HashMap<>();
    Map<Gene, GeneInInteractome> geneInInteractomeMap = new HashMap<>();

    interactions.getAGenes().forEach(gene -> {
      processGene(gene, speciesA, predictome, geneMap, geneInInteractomeMap);
    });
    interactions.getBGenes().forEach(gene -> {
      processGene(gene, speciesB, predictome, geneMap, geneInInteractomeMap);
    });

    interactions.forEach(interaction -> {
      Gene geneA = geneMap.get(interaction.getA());
      Gene geneB = geneMap.get(interaction.getB());

      if (geneA != null && geneB != null) {
        predictome.addInteraction(
          speciesA, speciesB, geneA, geneB, geneInInteractomeMap.get(geneA), geneInInteractomeMap.get(geneB)
        );
      }
    });

    geneMap.clear();

    return predictome;
  }

  private void processGene(
    int geneId, Species species, Predictome predictome, Map<Integer, Gene> geneMap,
    Map<Gene, GeneInInteractome> geneInInteractomeMap
  ) {
    try {
      Gene geneEntity = geneDao.getGene(geneId);
      GeneInInteractome gi = this.geneInInteractomeDao.create(species, predictome, geneEntity);

      geneMap.put(geneId, geneEntity);
      geneInInteractomeMap.put(geneEntity, gi);
    } catch (IllegalArgumentException e) {}
  }
}
