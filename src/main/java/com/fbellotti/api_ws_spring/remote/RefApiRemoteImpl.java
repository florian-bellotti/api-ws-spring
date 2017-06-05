package com.fbellotti.api_ws_spring.remote;

import com.fbellotti.api_ws_spring.dao.RefApiDao;
import com.fbellotti.api_ws_spring.model.DaoResponse;
import com.fbellotti.api_ws_spring.response.ApiResponse;
import com.fbellotti.api_ws_spring.response.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Component
public abstract class RefApiRemoteImpl<T> implements RefApiRemote<T> {

  private RefApiDao<T> daoRef;
  private final String messageFailedQuery = "Failed during the request execution";
  private final String messageSuccess = "Success";

  @Autowired
  public RefApiRemoteImpl(RefApiDao<T> daoRef) {
    this.daoRef = daoRef;
  }

  /**
   * This method allow to find items in a mongoDB collection
   * The collection depend off T.class
   * @return Return an ApiResponse<List<T>>
   */
  public ApiResponse<List<T>> find(UriInfo info) {
    ApiResponse<List<T>> response = new ApiResponse<>();

    // check if uri info are valid
    String message = checkUriInfo(info);
    if (message != null) {
      response.setApiResponse(null, "400", message, ResponseStatus.FAILURE,
          null, null);
      return response;
    }

    try {
      DaoResponse<List<T>> find = daoRef.find(info.getPathParameters());
      response.setApiResponse(find.getObject(), "200", messageSuccess,
          ResponseStatus.SUCCESS, find.getContentRange(), Integer.toString(find.getAcceptRange()));
    } catch (Exception e) {
      response.setApiResponse(null, "500", messageFailedQuery, ResponseStatus.FAILURE,
          null, null);
    }

    return response;
  }

  /**
   * This method allow to count the number of item in a mongoDB collection
   * The collection depend off T.class
   * @return Return an ApiResponse<Long>
   */
  public ApiResponse<Long> count(UriInfo info) {
    ApiResponse<Long> response = new ApiResponse<>();

    // check if uri info are valid
    String message = checkUriInfo(info);
    if (message != null) {
      response.setApiResponse(null, "400", message, ResponseStatus.FAILURE,
          null, null);
      return response;
    }

    try {
      Long count = daoRef.count(info.getPathParameters());
      response.setApiResponse(count, "200", messageSuccess, ResponseStatus.SUCCESS,
          null, null);
    } catch (Exception e) {
      response.setApiResponse(null, "500", messageFailedQuery, ResponseStatus.FAILURE,
          null, null);
    }

    return response;
  }

  /**
   * This method allow to find the first added item in a mongoDB collection
   * The collection depend off T.class
   * @return Return an ApiResponse<List<T>>
   */
  public ApiResponse<List<T>> first(UriInfo info) {
    ApiResponse<List<T>> response = new ApiResponse<>();

    // check if uri info are valid
    String message = checkUriInfo(info);
    if (message != null) {
      response.setApiResponse(null, "400", message, ResponseStatus.FAILURE,
          null, null);
      return response;
    }

    // sort by is required
    if (info.getPathParameters().get("asc") == null && info.getPathParameters().get("desc") == null) {
      response.setApiResponse(null, "400", "Asc or desc is required",
          ResponseStatus.FAILURE, null, null);
      return response;
    }

    try {
      DaoResponse<List<T>> first = daoRef.first(info.getPathParameters());
      response.setApiResponse(first.getObject(), "200", messageSuccess, ResponseStatus.SUCCESS,
          first.getContentRange(), Integer.toString(first.getAcceptRange()));
    } catch (Exception e) {

      response.setApiResponse(null, "500", messageFailedQuery, ResponseStatus.FAILURE,
          null, null);
    }

    return response;
  }

  private String checkUriInfo(UriInfo info) {
    // asc and desc can't exist in the same query
    if (info.getPathParameters().get("asc") != null && info.getPathParameters().get("desc") != null) {
      return "Asc and desc can't exist in the same query";
    }

    // check if range format is valid
    List<String> range = info.getPathParameters().get("range");
    if (range != null) {
      if (range.size() > 1) {
        return "Invalid range";
      }

      if (!range.get(0).matches("^[0-9]+-[0-9]+$")) {
        return "Invalid range format";
      }

      String[] rg = range.get(0).split("-");
      int from = Integer.parseInt(rg[0]);
      int to = Integer.parseInt(rg[1]);
      if (from > to || from < 0) {
        return "Invalid range";
      }
    }

    return null;
  }

}
