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

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.sing_group.evoppi.domain.dao.spi.bio.GeneNamesDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneSequenceDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.SpeciesDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneNames;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.seda.datatype.SequencesGroup;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultSpeciesDAO implements SpeciesDAO {

  @Inject
  private GeneDAO geneDao;
  
  @Inject
  private GeneNamesDAO geneNamesDao;
  
  @Inject
  private GeneSequenceDAO geneSequenceDao;

  @Inject
  private InteractomeDAO interactomeDao;
  
  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Species> dh;

  public DefaultSpeciesDAO() {
    super();
  }

  public DefaultSpeciesDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Species.class, this.em);
  }

  @Override
  public Stream<Species> listSpecies(ListingOptions<Species> speciesListingOptions) {
    return this.dh.list(speciesListingOptions).stream();
  }

  @Override
  public Species getSpecies(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown species: " + id));
  }

  @Override
  public void deleteSpecies(int id) {
    final Species species = this.getSpecies(id);
    final List<Integer> geneIds = species.getGenes()
      .map(Gene::getId)
    .collect(toList());
    
    final List<Integer> interactomeIds = species.getInteractomes()
      .map(Interactome::getId)
    .collect(toList());
    
    this.interactomeDao.deleteAll(interactomeIds);
    this.geneNamesDao.deleteGeneNamesByGenes(geneIds);
    this.geneSequenceDao.deleteGeneSequencesByGenes(geneIds);
    this.geneDao.deleteGenes(geneIds);
    
    this.dh.deleteBy("id", id);
  }
  
  @Override
  @Transactional(dontRollbackOn = IllegalArgumentException.class)
  public Species getSpeciesByName(String name) {
    try {
      return this.dh.getBy("name", name);
    } catch (NoResultException e) {
      throw new IllegalArgumentException("Unknown species: " + name, e);
    }
  }

  @Override
  public long count(ListingOptions<Species> speciesListingOptions) {
    return this.dh.count(speciesListingOptions);
  }

  @Override
  public void createSpecies(
    String name, SequencesGroup sequencesGroup, Map<Integer, List<String>> dictionary, Integer taxonomyId
  ) {
    Species species = new Species(name, taxonomyId);

    this.dh.persist(species);

    this.addGenes(dictionary, species);
    this.addGeneSequences(sequencesGroup, dictionary);
  }

  private void addGenes(Map<Integer, List<String>> dictionary, Species species) {
    dictionary.entrySet().stream().forEach(entry -> {
      int geneId = entry.getKey();
      List<String> names = entry.getValue();
      String defaultName = defaultName(names);

      species.addGene(geneId, defaultName, new GeneNames(geneId, "GeneBank", names));
    });

    this.dh.update(species);
  }
  
  private static String defaultName(List<String> names) {
    Optional<String> defaultName = names.stream().filter(s -> s.matches("^[a-zA-Z].*")).findFirst();
    
    return defaultName.orElseGet(() -> names.get(0));
  }

  private void addGeneSequences(SequencesGroup sequencesGroup, Map<Integer, List<String>> dictionary) {
    sequencesGroup.getSequences().forEach(sequence -> {
      int geneId = Integer.valueOf(sequence.getName());
      try {
        if (dictionary.keySet().contains(geneId)) {
          Gene gene = geneDao.getGene(geneId);
          gene.addGeneSequence(sequence.getChain());
        }
      } catch (IllegalArgumentException e) {}
    });
  }
}
