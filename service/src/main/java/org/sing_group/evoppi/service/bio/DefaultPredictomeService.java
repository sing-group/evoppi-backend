/*-
 * #%L
 * Service
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

package org.sing_group.evoppi.service.bio;

import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.evoppi.domain.dao.ListingOptions;
import org.sing_group.evoppi.domain.dao.spi.bio.InteractomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.PredictomeDAO;
import org.sing_group.evoppi.domain.dao.spi.bio.SpeciesDAO;
import org.sing_group.evoppi.domain.entities.bio.Predictome;
import org.sing_group.evoppi.domain.interactome.GeneInteraction;
import org.sing_group.evoppi.domain.interactome.GeneInteractions;
import org.sing_group.evoppi.service.bio.entity.PredictomeCreationData;
import org.sing_group.evoppi.service.spi.bio.PredictomeService;

@Stateless
@PermitAll
public class DefaultPredictomeService implements PredictomeService {

  @Inject
  private PredictomeDAO dao;

  @Inject
  private InteractomeDAO interactomeDao;

  @Inject
  private SpeciesDAO speciesDao;

  @Override
  public Stream<Predictome> list(ListingOptions<Predictome> listingOptions) {
    return this.dao.list(listingOptions);
  }

  @Override
  public Predictome get(int id) {
    return this.dao.get(id);
  }

  @Override
  public long count() {
    return this.count(ListingOptions.noModification());
  }

  @Override
  public long count(ListingOptions<Predictome> listingOptions) {
    return this.dao.count(listingOptions);
  }

  @Override
  @RolesAllowed("ADMIN")
  public Predictome create(PredictomeCreationData data) {
    boolean exists = existsInteractomeWithName(data.getName());

    if (exists) {
      throw new IllegalArgumentException("Predictome name already exists");
    } else {

      GeneInteractions interactions;
      try {
        interactions = loadInteractions(data.getFile());
      } catch (IOException e) {
        throw new IllegalArgumentException("The interactions file can't be read: " + e.toString());
      }
      
      return this.dao.create(
        data.getName(),
        speciesDao.getSpecies(data.getSpeciesADbId()),
        speciesDao.getSpecies(data.getSpeciesBDbId()),
        data.getSourceInteractome(),
        data.getConversionDatabase(),
        interactions
      );
    }
  }

  private boolean existsInteractomeWithName(String name) {
    try {
      this.interactomeDao.getByName(name);
      return true;
    } catch (IllegalArgumentException iae) {
      return false;
    }
  }

  private static GeneInteractions loadInteractions(File file) throws FileNotFoundException, IOException {
    GeneInteractions toret = new GeneInteractions();

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] lineSplit = line.split("\t| ");
        if (lineSplit.length != 2) {
          System.err.println("Predictome creation: ignore interaction line withouth two fields");
          System.err.println(line);
          continue;
        }
        try {
          toret.add(new GeneInteraction(parseInt(lineSplit[0]), parseInt(lineSplit[1])));
        } catch (NumberFormatException nfe) {
          throw new IOException(nfe);
        }
      }
    }

    return toret;
  }
}
