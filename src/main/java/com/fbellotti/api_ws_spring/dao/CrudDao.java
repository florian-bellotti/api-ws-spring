package com.fbellotti.api_ws_spring.dao;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public interface CrudDao<T> {

  /**
   * Create an item
   * @param item The item
   * @return The created item
   */
  T create(T item);

  /**
   * Find an item by id
   * @param id The item's id
   * @return The item
   */
  T findById(String id);

  /**
   * Update an item by id
   * @param id The item's id
   * @param item The item
   */
  void update(String id, T item);

  /**
   * Delete an item by id
   * @param id The item's id
   */
  void delete(String id);

}
