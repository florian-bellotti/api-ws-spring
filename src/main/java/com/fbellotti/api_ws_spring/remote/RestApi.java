package com.fbellotti.api_ws_spring.remote;

import com.fbellotti.api_ws_spring.model.ErrorRemoteResponse;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public abstract class RestApi {

  protected Response badRequest(String message) {
    return Response
      .status(400)
      .header("content-type", "application/json")
      .entity(new ErrorRemoteResponse("INVALID_PARAMETER", message))
      .build();
  }

  protected Response notFound(String message) {
    return Response
      .status(404)
      .header("content-type", "application/json")
      .entity(new ErrorRemoteResponse("INVALID_PARAMETER", message))
      .build();
  }

  protected Response internalError(Throwable ex) {
    return Response
      .status(500)
      .header("content-type", "application/json")
      .entity(new ErrorRemoteResponse("EXECUTION_ERROR", ex.getMessage()))
      .build();
  }
}
