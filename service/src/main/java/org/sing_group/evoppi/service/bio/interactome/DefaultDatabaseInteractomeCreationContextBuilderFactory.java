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

package org.sing_group.evoppi.service.bio.interactome;

import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationConfiguration;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContext;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContextBuilder;
import org.sing_group.evoppi.service.spi.bio.interactome.DatabaseInteractomeCreationContextBuilderFactory;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.DatabaseInteractomeCreationPipeline;
import org.sing_group.evoppi.service.spi.bio.interactome.pipeline.event.DatabaseInteractomeCreationEventManager;

public class DefaultDatabaseInteractomeCreationContextBuilderFactory
  implements DatabaseInteractomeCreationContextBuilderFactory {

  @Override
  public DatabaseInteractomeCreationContextBuilder createBuilderFor(
    DatabaseInteractomeCreationContext context
  ) {
    return new DefaultDatabaseInteractomeCreationContextBuilder(context);
  }

  @Override
  public DatabaseInteractomeCreationContextBuilder createBuilderFor(
    DatabaseInteractomeCreationPipeline pipeline, DatabaseInteractomeCreationConfiguration configuration,
    DatabaseInteractomeCreationEventManager eventManager
  ) {
    return new DefaultDatabaseInteractomeCreationContextBuilder(pipeline, configuration, eventManager);
  }
}