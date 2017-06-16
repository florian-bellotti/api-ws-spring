package com.fbellotti.api_ws_spring.dao;

import com.fbellotti.api_ws_spring.model.DaoResponse;

import javax.ws.rs.core.MultivaluedMap;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public interface QueryStringDao<T> {

  /**
   * Find all items
   * @param filters It's a MultivaluedMap that contains an list of filters (asc, fields...)
   * @return Return an object DaoResponse<T>
   */
  DaoResponse<T> find(MultivaluedMap<String, String> filters);

  /**
   * Count the number of items
   * @param filters It's a MultivaluedMap that contains an list of filters (asc, fields...)
   * @return Return an object DaoResponse<T>
   */
  long count(MultivaluedMap<String, String> filters);

  /**
   * Find the first item
   * @param filters It's a MultivaluedMap that contains an list of filters (asc, fields...)
   * @return Return an object DaoResponse<T>
   */
  DaoResponse<T> first(MultivaluedMap<String, String> filters);

}
