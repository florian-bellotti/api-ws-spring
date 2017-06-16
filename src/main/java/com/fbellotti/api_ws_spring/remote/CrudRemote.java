package com.fbellotti.api_ws_spring.remote;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public interface CrudRemote<T> {

  @POST
  @Path("/")
  Response create(T item);

  @GET
  @Path("/{id}")
  Response read(@PathParam("id") String id);

  @PUT
  @Path("/{id}")
  Response update(@PathParam("id") String id, T item);

  @DELETE
  @Path("/{id}")
  Response delete(@PathParam("id") String id);

}
