package com.fbellotti.api_ws_spring.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fbellotti.api_ws_spring.dao.CrudDao;
import com.fbellotti.api_ws_spring.model.ErrorRemoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Component
public abstract class CrudServiceImpl<T> implements CrudService<T> {

  private static final Logger LOG = LoggerFactory.getLogger(CrudServiceImpl.class);

  private CrudDao<T> daoCrud;
  private ErrorRemoteResponse executionError;

  @Autowired
  public CrudServiceImpl(CrudDao<T> daoCrud) {
    this.daoCrud = daoCrud;
    this.executionError = new ErrorRemoteResponse("EXECUTION_ERROR", "Failed during the request execution");
  }

  @Override
  public Response create(T item) {
    ErrorRemoteResponse error;
    GenericEntity<ErrorRemoteResponse> genericError;

    if (item == null) {
      error = new ErrorRemoteResponse("NULL_ITEM", "The item is null");
      genericError = new GenericEntity<ErrorRemoteResponse>(error){};
      return Response.status(400).entity(genericError).build();
    }

    try {
      item = daoCrud.create(item);
      return Response.status(201).entity(item).build();
    } catch (Exception e) {
      LOG.error("Tried to insert item : " + item.toString() + e);
      return Response.status(500).entity(executionError).build();
    }
  }

  @Override
  public Response read(String id) {
    ErrorRemoteResponse error;
    GenericEntity<ErrorRemoteResponse> genericError;

    if (id == null) {
      error = new ErrorRemoteResponse("NULL_ID", "The id is null");
      genericError = new GenericEntity<ErrorRemoteResponse>(error){};
      return Response.status(400).entity(genericError).build();
    }

    try {
      T item = daoCrud.findById(id);
      if (item == null) {
        error = new ErrorRemoteResponse("NO_ITEM", "No item match with the given id");
        genericError = new GenericEntity<ErrorRemoteResponse>(error){};
        return Response.status(400).entity(genericError).build();
      } else {
        return Response.status(200).entity(item).build();
      }
    } catch (Exception e) {
      return Response.status(500).entity(executionError).build();
    }
  }

  @Override
  public Response update(String id, T item) {
    ErrorRemoteResponse error;
    GenericEntity<ErrorRemoteResponse> genericError;

    if (id == null) {
      error = new ErrorRemoteResponse("NULL_ID", "The id is null");
      genericError = new GenericEntity<ErrorRemoteResponse>(error){};
      return Response.status(400).entity(genericError).build();
    }

    if (item == null) {
      error = new ErrorRemoteResponse("NULL_ITEM", "The item is null");
      genericError = new GenericEntity<ErrorRemoteResponse>(error){};
      return Response.status(400).entity(genericError).build();
    }

    try {
      daoCrud.update(id, item);
      return Response.status(204).build();
    } catch (Exception e) {
      return Response.status(500).entity(executionError).build();
    }
  }

  @Override
  public Response delete(String id) {
    ErrorRemoteResponse error;
    GenericEntity<ErrorRemoteResponse> genericError;

    if (id == null) {
      error = new ErrorRemoteResponse("NULL_ID", "The id is null");
      genericError = new GenericEntity<ErrorRemoteResponse>(error){};
      return Response.status(400).entity(genericError).build();
    }

    try {
      daoCrud.delete(id);
      return Response.status(204).build();
    } catch (Exception e) {
      return Response.status(500).entity(executionError).build();
    }
  }
}
