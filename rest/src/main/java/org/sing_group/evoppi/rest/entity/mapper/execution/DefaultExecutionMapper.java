/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.rest.entity.mapper.execution;

import static java.util.Objects.requireNonNull;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.evoppi.domain.entities.execution.Work;
import org.sing_group.evoppi.domain.entities.execution.WorkStep;
import org.sing_group.evoppi.rest.entity.execution.WorkData;
import org.sing_group.evoppi.rest.entity.execution.WorkStepData;
import org.sing_group.evoppi.rest.entity.mapper.spi.execution.ExecutionMapper;
import org.sing_group.evoppi.rest.entity.user.IdAndUri;
import org.sing_group.evoppi.rest.resource.route.BaseRestPathBuilder;

@Default
public class DefaultExecutionMapper implements ExecutionMapper {
  private UriBuilder uriBuilder;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
  }
  
  @Override
  public WorkData toWorkData(Work work) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(this.uriBuilder);
    
    return new WorkData(
      new IdAndUri(work.getId(), pathBuilder.work(work.getId()).build()),
      work.getName(),
      work.getDescription().orElse(null),
      work.getCreationDateTime(),
      work.getStartDateTime().orElse(null),
      work.getEndDateTime().orElse(null),
      work.getResultReference().orElse(null),
      work.isFinished(),
      work.getSteps()
        .map(this::toWorkStepData)
      .toArray(WorkStepData[]::new)
    );
  }
  
  private WorkStepData toWorkStepData(WorkStep step) {
    return new WorkStepData(
      step.getOrder(),
      step.getDescription().orElse(null),
      step.getProgress().orElse(null)
    );
  }
}
