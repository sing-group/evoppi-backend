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

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneInInteractomeDao;
import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneInInteractome;
import org.sing_group.evoppi.domain.entities.bio.GeneInInteractome.GeneInInteractomeId;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultGeneInInteractomeDAO implements GeneInInteractomeDao {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<GeneInInteractomeId, GeneInInteractome> dh;

  public DefaultGeneInInteractomeDAO() {
    super();
  }

  public DefaultGeneInInteractomeDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(GeneInInteractomeId.class, GeneInInteractome.class, this.em);
  }

  @Override
  public GeneInInteractome create(Species species, Interactome interactome, Gene gene) {
    Optional<GeneInInteractome> db =
      this.dh.get(new GeneInInteractomeId(species.getId(), interactome.getId(), gene.getId()));
    if (db.isPresent()) {
      return db.get();
    } else {
      return this.dh.persist(new GeneInInteractome(species, interactome, gene));
    }
  }
}
