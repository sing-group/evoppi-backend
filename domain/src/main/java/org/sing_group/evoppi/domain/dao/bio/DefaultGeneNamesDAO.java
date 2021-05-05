/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2017 - 2021 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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

import static java.util.stream.Collectors.joining;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.evoppi.domain.dao.DAOHelper;
import org.sing_group.evoppi.domain.dao.spi.bio.GeneNamesDAO;
import org.sing_group.evoppi.domain.entities.bio.GeneNames;
import org.sing_group.evoppi.domain.entities.bio.GeneNames.GeneNamesId;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultGeneNamesDAO implements GeneNamesDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<GeneNamesId, GeneNames> dh;

  public DefaultGeneNamesDAO() {
    super();
  }

  public DefaultGeneNamesDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(GeneNamesId.class, GeneNames.class, this.em);
  }

  @Override
  public void deleteGeneNamesByGenes(Collection<Integer> geneIds) {
    this.em.createNativeQuery(String.format(
      "DELETE FROM gene_name_value WHERE geneId IN (%s)", 
      geneIds.stream().map(i -> i.toString()).collect(joining(", "))
    )).executeUpdate();
    
    this.dh.deleteBy("geneId", geneIds);
  }

}
