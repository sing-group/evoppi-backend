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
package org.sing_group.evoppi.domain.dao.bio.execution;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.InteractionGroupResultDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.InteractionGroupResultInteractomeDegreesDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.execution.SameSpeciesInteractionsResultDAO;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.execution.WorkEntity;
import org.sing_group.evoppi.domain.entities.user.User;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultSameSpeciesInteractionsResultDAO implements SameSpeciesInteractionsResultDAO {
  @Inject
  private InteractionGroupResultDAO igrDao;
  
  @Inject
  private InteractionGroupResultInteractomeDegreesDAO igridDao;

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, SameSpeciesInteractionsResult> dh;

  public DefaultSameSpeciesInteractionsResultDAO() {
    super();
  }

  public DefaultSameSpeciesInteractionsResultDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, SameSpeciesInteractionsResult.class, this.em);
  }
  
  @Override
  public boolean exists(String interactionResultId) {
    try {
      this.get(interactionResultId);
      
      return true;
    } catch (IllegalArgumentException iae) {
      return false;
    }
  }

  @Override
  public SameSpeciesInteractionsResult get(String interactionResultId) {
    return this.dh.get(interactionResultId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown interaction result: " + interactionResultId));
  }

  @Override
  public Stream<SameSpeciesInteractionsResult> listById(
    String[] ids, ListingOptions<SameSpeciesInteractionsResult> listingOptions
  ) {
    final Set<String> idsSet = stream(ids).collect(toSet());
    return this.dh.listBy("id", idsSet, listingOptions).stream();
  }
  
  @Override
  public Stream<SameSpeciesInteractionsResult> listUserResults(
    User user, ListingOptions<SameSpeciesInteractionsResult> listingOptions
  ) {
    return this.dh.listBy("owner", user, listingOptions).stream();
  }
  
  @Override
  public long coungById(String[] ids, ListingOptions<SameSpeciesInteractionsResult> listingOptions) {
    final Set<String> idsSet = stream(ids).collect(toSet());
    return this.dh.countBy("id", idsSet, listingOptions);
  }
  
  @Override
  public long countByUser(User user, ListingOptions<SameSpeciesInteractionsResult> listingOptions) {
    return this.dh.countBy("owner", user, listingOptions);
  }
  
  @Override
  public SameSpeciesInteractionsResult create(
    String name, String description, String resultReference,
    Gene queryGene, int queryMaxDegree, Collection<Interactome> queryInteractomes, User owner
  ) {
    return this.dh.persist(new SameSpeciesInteractionsResult(
      name, description, resultReference, queryGene, queryMaxDegree, queryInteractomes, owner
    ));
  }
  
  @Override
  public SameSpeciesInteractionsResult create(
    String name, String description, Function<String, String> resultReferenceBuilder,
    Gene queryGene, int queryMaxDegree, Collection<Interactome> queryInteractomes,
    User owner
  ) {
    return this.dh.persist(new SameSpeciesInteractionsResult(
      name, description, resultReferenceBuilder, queryGene, queryMaxDegree, queryInteractomes, owner
    ));
  }
  
  public void deleteResultsByInteractomes(Collection<Integer> ids) {
    final Set<SameSpeciesInteractionsResult> results = this.findByInteractomeId(ids)
    .collect(toSet());
    
    if (!results.isEmpty()) {
      this.igridDao.deleteInteractionGroupResultInteractomeDegreesByInteractionsResult(results);
      this.dh.deleteBy("id", results.stream().map(WorkEntity::getId).collect(toSet()));
      this.igrDao.deleteInteractionGroupResultsByInteractionsResult(results);
    }
  }
  
  private Stream<SameSpeciesInteractionsResult> findByInteractomeId(Collection<Integer> ids) {
    final CriteriaQuery<SameSpeciesInteractionsResult> query = this.dh.createCBQuery();
    final Root<SameSpeciesInteractionsResult> root = query.from(SameSpeciesInteractionsResult.class);
    final Join<SameSpeciesInteractionsResult, Interactome> join = root.join("queryInteractomes");
    
    query.where(join.get("id").in(ids));
    
    return this.em.createQuery(query).getResultList().stream();
  }
}
