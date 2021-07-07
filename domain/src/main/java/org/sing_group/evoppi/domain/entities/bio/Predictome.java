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
package org.sing_group.evoppi.domain.entities.bio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "predictome")
public class Predictome extends Interactome {
  private static final long serialVersionUID = 1L;

  @Column(name = "sourceInteractome", length = 120, nullable = false)
  private String sourceInteractome;

  @Enumerated(EnumType.STRING)
  private PredictomeConversionDatabase conversionDatabase;

  Predictome() {}

  public Predictome(
    String name, Species speciesA, Species speciesB, String sourceInteractome,
    PredictomeConversionDatabase conversionDatabase
  ) {
    super(name, speciesA, speciesB);

    this.sourceInteractome = sourceInteractome;
    this.conversionDatabase = conversionDatabase;
  }

  public String getSourceInteractome() {
    return sourceInteractome;
  }

  public PredictomeConversionDatabase getConversionDatabase() {
    return conversionDatabase;
  }
}
