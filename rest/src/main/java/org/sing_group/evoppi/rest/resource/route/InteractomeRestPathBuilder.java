package org.sing_group.evoppi.rest.resource.route;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class InteractomeRestPathBuilder implements RestPathBuilder {
  protected UriBuilder builder;

  public InteractomeRestPathBuilder(UriBuilder builder) {
    this.builder = builder.clone().path("interactome");
  }
  
  public InteractomeRestPathBuilder(UriBuilder builder, int id) {
    this(builder);
    
    this.builder = this.builder.path(Integer.toString(id));
  }

  @Override
  public URI build() {
    return this.builder.clone().build();
  }
}
