/*-
 * #%L
 * Service
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
package org.sing_group.evoppi.service.spi.execution.pipeline;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;

@Local
@PermitAll
public interface PipelineExecutor {
  @PermitAll
  public <
    C extends PipelineConfiguration,
    PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
    PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
    P extends Pipeline<C, PC, PS, P, PE, PEM>,
    PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
    PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
  > void execute(P pipeline, PC context);

  @PermitAll
  public default <
    C extends PipelineConfiguration,
    PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
    PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
    P extends Pipeline<C, PC, PS, P, PE, PEM>,
    PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
    PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
  > void execute(P pipeline, C configuration) {
    this.execute(pipeline, pipeline.createContext(configuration));
  }
}
