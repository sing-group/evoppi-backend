package org.sing_group.evoppi.rest.resource.route;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class GeneRestPathBuilder implements RestPathBuilder {
  private UriBuilder builder;
  
  public GeneRestPathBuilder(UriBuilder builder) {
    this.builder = builder.clone().path("gene");
  }
  
  public GeneRestPathBuilder(UriBuilder builder, int id) {
    this(builder);
    
    this.builder = this.builder.path("gene").path(Integer.toString(id));
  }
  
  @Override
  public URI build() {
    return this.builder.clone().build();
  }
}
