package com.fbellotti.api_ws_spring.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Component
public class CrudMongoDao<T> implements CrudDao<T> {

  private final MongoTemplate mongoTemplate;
  private final Class<T> genericType;
  private final String idField;

 @Autowired
  public CrudMongoDao(MongoTemplate mongoTemplate, Class<T> genericType, String idField) {
    this.mongoTemplate = mongoTemplate;
    this.genericType = genericType;
    this.idField = idField;
  }

  @Override
  public T findById(String id) {
    Query query = new Query(Criteria.where(idField).is(id));
    return mongoTemplate.findOne(query, genericType);
  }

  @Override
  public T create(T item) {
    mongoTemplate.insert(item);
    return item;
  }

  @Override
  public void update(String id, T item) {
    Query query = new Query(Criteria.where(idField).is(id));

    //build update
    DBObject dbDoc = new BasicDBObject();
    mongoTemplate.getConverter().write(item, dbDoc);
    Update update = fromDBObjectExcludeNullFields(dbDoc);

    mongoTemplate.updateFirst(query, update, genericType);
  }

  @Override
  public void delete(String id) {
    Query query = new Query(Criteria.where(idField).is(id));
    mongoTemplate.remove(query, genericType);
  }

  /**
   * Return Update object
   * @param object The object to convert
   * @return The converted object
   */
  private Update fromDBObjectExcludeNullFields(DBObject object) {
    Update update = new Update();
    for (String key : object.keySet()) {
      Object value = object.get(key);
      if(value!=null){
        update.set(key, value);
      }
    }
    return update;
  }
}
