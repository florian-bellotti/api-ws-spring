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
public abstract class CrudServiceImpl<T> extends RestApi implements CrudService<T> {

  private static final Logger LOG = LoggerFactory.getLogger(CrudServiceImpl.class);

  private CrudDao<T> daoCrud;

  @Autowired
  public CrudServiceImpl(CrudDao<T> daoCrud) {
    this.daoCrud = daoCrud;
  }

  @Override
  public Response create(T item) {
    if (item == null) {
      return badRequest("The item is null.");
    }

    try {
      T response = daoCrud.create(item);
      return Response.status(201).entity(response).build();
    } catch (Exception e) {
      LOG.error("Tried to insert item : " + item.toString(), e);
      return internalError(e);
    }
  }

  @Override
  public Response read(String id) {
    if (id == null) {
      return badRequest("The id is null.");
    }

    try {
      T item = daoCrud.findById(id);
      if (item == null) {
        return notFound("Item not found.");
      } else {
        return Response.status(200).entity(item).build();
      }
    } catch (Exception e) {
      return internalError(e);
    }
  }

  @Override
  public Response update(String id, T item) {
    if (id == null) {
      return badRequest("The id is null.");
    }

    if (item == null) {
      return badRequest("The item is null.");
    }

    try {
      daoCrud.update(id, item);
      return Response.status(204).build();
    } catch (Exception e) {
      return internalError(e);
    }
  }

  @Override
  public Response delete(String id) {
    if (id == null) {
      return badRequest("The id is null.");
    }

    try {
      daoCrud.delete(id);
      return Response.status(204).build();
    } catch (Exception e) {
      return internalError(e);
    }
  }
}
