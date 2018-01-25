/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.service.spi.bio.event;

import java.util.stream.Stream;

import org.sing_group.evoppi.service.entity.bio.GeneInteraction;

public interface InteractionsCalculusCallback {
  public void calculusStarted();
  public void degreeCalculusStarted(int degree);
  public void degreeCalculusFinished(int degree, Stream<GeneInteraction> interactions);
  public void calculusFinished();
  
  public static class SimpleInteractionsCalculusCallback implements InteractionsCalculusCallback {
    @Override
    public void calculusStarted() {}

    @Override
    public void degreeCalculusStarted(int degree) {}

    @Override
    public void degreeCalculusFinished(int degree, Stream<GeneInteraction> interactions) {}

    @Override
    public void calculusFinished() {}
  }
}