package com.fbellotti.api_ws_spring.remote;

import com.fbellotti.api_ws_spring.response.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public interface RefApiRemote<T> {

  @GET
  @Path("/")
  ApiResponse<List<T>> find(@Context UriInfo info);

  @GET
  @Path("/count")
  ApiResponse<Long> count(@Context UriInfo info);

  @GET
  @Path("/first")
  ApiResponse<List<T>> first(@Context UriInfo info);

}
