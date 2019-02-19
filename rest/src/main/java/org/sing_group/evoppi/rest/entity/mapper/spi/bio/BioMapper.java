/*-
 * #%L
 * REST
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

package org.sing_group.evoppi.rest.entity.mapper.spi.bio;

import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.bio.Gene;
import org.sing_group.evoppi.domain.entities.bio.GeneNames;
import org.sing_group.evoppi.domain.entities.bio.Interactome;
import org.sing_group.evoppi.domain.entities.bio.Species;
import org.sing_group.evoppi.domain.entities.bio.execution.DifferentSpeciesInteractionsResult;
import org.sing_group.evoppi.domain.entities.bio.execution.InteractionGroupResult;
import org.sing_group.evoppi.domain.entities.bio.execution.SameSpeciesInteractionsResult;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.DifferentSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.bio.GeneData;
import org.sing_group.evoppi.rest.entity.bio.GeneNameData;
import org.sing_group.evoppi.rest.entity.bio.GeneNamesData;
import org.sing_group.evoppi.rest.entity.bio.InteractionResultData;
import org.sing_group.evoppi.rest.entity.bio.InteractionsResultFilteringOptionsData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeData;
import org.sing_group.evoppi.rest.entity.bio.InteractomeWithInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultData;
import org.sing_group.evoppi.rest.entity.bio.SameSpeciesInteractionsResultSummaryData;
import org.sing_group.evoppi.rest.entity.bio.SpeciesData;

public interface BioMapper {
  public void setUriBuilder(UriBuilder uriBuilder);
  
  public SpeciesData toSpeciesData(Species species);
  
  public InteractomeData toInteractomeData(Interactome interactome);
  
  public InteractomeWithInteractionsData toInteractomeWithInteractionsData(Interactome interactome);
  
  public GeneData toGeneData(Gene gene);
  
  public GeneNamesData toGeneNamesData(Gene gene);
  
  public GeneNameData toGeneNameData(GeneNames geneNames);

  public SameSpeciesInteractionsResultData toInteractionQueryResult(
    SameSpeciesInteractionsResult result,
    InteractionsResultFilteringOptionsData filteringOptions
  );
  
  public SameSpeciesInteractionsResultSummaryData toInteractionQueryResultSummary(
    SameSpeciesInteractionsResult result
  );

  public DifferentSpeciesInteractionsResultData toInteractionQueryResult(
    DifferentSpeciesInteractionsResult result,
    InteractionsResultFilteringOptionsData filteringOptions
  );
  
  public DifferentSpeciesInteractionsResultSummaryData toInteractionQueryResultSummary(
    DifferentSpeciesInteractionsResult result
  );
  
  public InteractionResultData toInteractionResultData(InteractionGroupResult interaction);

  public SameSpeciesInteractionsData toInteractionsResultData(
    SameSpeciesInteractionsResult result,
    InteractionsResultFilteringOptionsData filteringOptions
  );
  
  public DifferentSpeciesInteractionsData toInteractionsResultData(
    DifferentSpeciesInteractionsResult result,
    InteractionsResultFilteringOptionsData filteringOptions
  );
}
