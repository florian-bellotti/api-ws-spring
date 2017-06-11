package com.fbellotti.api_ws_spring.dao;

import com.fbellotti.api_ws_spring.model.DaoResponse;

import javax.ws.rs.core.MultivaluedMap;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public interface RefApiDao<T> {

  DaoResponse<T> find(MultivaluedMap<String, String> filters);
  long count(MultivaluedMap<String, String> filters);
  DaoResponse<T> first(MultivaluedMap<String, String> filters);

}
