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
package org.sing_group.evoppi.rest.resource.notification;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.sing_group.evoppi.rest.entity.mapper.spi.notification.FeedbackMapper;
import org.sing_group.evoppi.rest.entity.notification.FeedbackData;
import org.sing_group.evoppi.rest.filter.CrossDomain;
import org.sing_group.evoppi.rest.resource.spi.notification.FeedbackResource;
import org.sing_group.evoppi.service.spi.notification.FeedbackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("feedback")
@Api("feedback")
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultFeedbackResource implements FeedbackResource {
  @Inject
  private FeedbackMapper mapper;
  
  @Inject
  private FeedbackService service;

  @POST
  @ApiOperation(
    value = "Sends feedback to page administrators.",
    code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 200, message = "successful operation"),
  })
  @Override
  public Response send(FeedbackData feedback) {
    this.service.notifyFeedback(this.mapper.toFeedback(feedback));
    
    return Response.ok().build();
  }

}
