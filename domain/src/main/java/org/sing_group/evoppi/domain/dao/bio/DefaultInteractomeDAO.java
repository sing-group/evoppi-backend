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
import org.sing_group.evoppi.domain.dao.spi.bio.GeneInInteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractionDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.DifferentSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.SameSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.entities.bio.Interactome;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultInteractomeDAO implements InteractomeDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, Interactome> dh;

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
  public Stream<Interactome> list(ListingOptions<Interactome> listingOptions) {
    return this.dh.list(listingOptions).stream();
  }

  @Override
  public long count(ListingOptions<Interactome> listingOptions) {
    return this.dh.count(listingOptions);
  }

  @Override
  public Interactome get(int id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown interactome: " + id));
  }

  @Override
  @Transactional(dontRollbackOn = IllegalArgumentException.class)
  public Interactome getByName(String name) {
    try {
      return this.dh.getBy("name", name);
    } catch (NoResultException e) {
      throw new IllegalArgumentException("Unknown interactome: " + name, e);
    }
  }

  @Override
  public void delete(int id) {
    this.deleteAll(singleton(id));
  }

  @Override
  public void deleteAll(Collection<Integer> ids) {
    this.sameSpeciesInteractionsResultDao.deleteResultsByInteractomes(ids);
    this.differentSpeciesInteractionsResultDao.deleteResultsByInteractomes(ids);
    this.interactionDao.deleteInteractionsByInteractomes(ids);
    this.geneInInteractomeDao.deleteGeneInInteractomesByInteractomes(ids);
    this.dh.deleteBy("id", ids);
  }
}
