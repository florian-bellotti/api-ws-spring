package com.fbellotti.api_ws_spring.remote;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public interface QueryStringService {

  @GET
  @Path("/")
  Response find(@Context UriInfo info);

  @GET
  @Path("/count")
  Response count(@Context UriInfo info);

  @GET
  @Path("/first")
  Response first(@Context UriInfo info);

}
