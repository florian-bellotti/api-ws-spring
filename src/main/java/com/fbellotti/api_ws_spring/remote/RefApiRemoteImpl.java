package com.fbellotti.api_ws_spring.remote;

import com.fbellotti.api_ws_spring.dao.RefApiDao;
import com.fbellotti.api_ws_spring.model.DaoResponse;
import com.fbellotti.api_ws_spring.model.ErrorRemoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Component
public abstract class RefApiRemoteImpl<T> implements RefApiRemote {

  private RefApiDao<T> daoRef;
  private ErrorRemoteResponse executionError;

  @Autowired
  public RefApiRemoteImpl(RefApiDao<T> daoRef) {
    this.daoRef = daoRef;
    this.executionError = new ErrorRemoteResponse("EXECUTION_ERROR", "Failed during the request execution");
  }

  /**
   * This method allow to find items in a mongoDB collection
   * The collection depend off T.class
   * @return Return a Response
   */
  public Response find(UriInfo info) {
    // check if uri info are valid
    List<ErrorRemoteResponse> errors = checkUriInfo(info);

    if (errors != null) {
      GenericEntity<List<ErrorRemoteResponse>> genericList
          = new GenericEntity<List<ErrorRemoteResponse>>(errors){};
      return Response.status(400).entity(genericList).build();
    }

    try {
      DaoResponse<T> find = daoRef.find(info.getQueryParameters());
      GenericEntity<List<T>> genericList = new GenericEntity<List<T>>(find.getItems()){};
      return Response
          .status((find.getPartialResponse()) ? 206 : 200)
          .entity(genericList)
          .header("content-range", find.getContentRange())
          .header("accept-range", find.getAcceptRange())
          .build();
    } catch (Exception e) {
      return Response.status(500).entity(executionError).build();
    }
  }

  /**
   * This method allow to count the number of item in a mongoDB collection
   * The collection depend off T.class
   * @return Return a Response
   */
  public Response count(UriInfo info) {
    // check if uri info are valid
    List<ErrorRemoteResponse> errors = checkUriInfo(info);

    if (errors != null) {
      GenericEntity<List<ErrorRemoteResponse>> genericList
          = new GenericEntity<List<ErrorRemoteResponse>>(errors){};
      return Response.status(400).entity(genericList).build();
    }

    try {
      Long count = daoRef.count(info.getQueryParameters());
      return Response.status(200).entity(count).build();
    } catch (Exception e) {
      return Response.status(500).entity(executionError).build();
    }
  }

  /**
   * This method allow to find the first added item in a mongoDB collection
   * The collection depend off T.class
   * @return Return a Response
   */
  public Response first(UriInfo info) {
// check if uri info are valid
    List<ErrorRemoteResponse> errors = checkUriInfo(info);

    if (errors != null) {
      GenericEntity<List<ErrorRemoteResponse>> genericList = new GenericEntity<List<ErrorRemoteResponse>>(errors){};
      return Response.status(400).entity(genericList).build();
    }

    // sort by is required
    if (info.getQueryParameters().get("asc") == null && info.getQueryParameters().get("desc") == null) {
      ErrorRemoteResponse error = new ErrorRemoteResponse("ASC_OR_DESC_REQUIRED", "Asc or desc is required");
      return Response.status(400).entity(error).build();
    }

    try {
      DaoResponse<T> first = daoRef.first(info.getQueryParameters());
      GenericEntity<List<T>> genericList = new GenericEntity<List<T>>(first.getItems()){};
      return Response
          .status((first.getPartialResponse()) ? 206 : 200)
          .entity(genericList)
          .header("content-range", first.getContentRange())
          .header("accept-range", first.getAcceptRange())
          .build();
    } catch (Exception e) {
      return Response.status(500).entity(executionError).build();
    }
  }

  private List<ErrorRemoteResponse> checkUriInfo(UriInfo info) {
    List<ErrorRemoteResponse> errors = null;

    // asc and desc can't exist in the same query
    if (info.getQueryParameters().get("asc") != null && info.getQueryParameters().get("desc") != null) {
      errors = new ArrayList<>();
      errors.add(new ErrorRemoteResponse("ASC_AND_DESC_EXIST",
          "Asc and desc can't exist in the same query"));
    }

    // check if range format is valid
    List<String> range = info.getQueryParameters().get("range");
    if (range != null) {
      if (range.size() > 1) {
        if (errors == null) {
          errors = new ArrayList<>();
        }
        errors.add(new ErrorRemoteResponse("INVALID_RANGE",
            "Range is invalid"));
      }

      if (!range.get(0).matches("^[0-9]+-[0-9]+$")) {
        if (errors == null) {
          errors = new ArrayList<>();
        }
        errors.add(new ErrorRemoteResponse("INVALID_RANGE_FORMAT",
            "Range format is invalid"));
      }

      String[] rg = range.get(0).split("-");
      int from = Integer.parseInt(rg[0]);
      int to = Integer.parseInt(rg[1]);
      if (from > to || from < 0) {
        if (errors == null) {
          errors = new ArrayList<>();
        }
        errors.add(new ErrorRemoteResponse("INVALID_RANGE",
            "Range is invalid"));
      }
    }

    return errors;
  }
}
