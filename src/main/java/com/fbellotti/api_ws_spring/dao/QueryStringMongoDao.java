package com.fbellotti.api_ws_spring.dao;

import com.fbellotti.api_ws_spring.model.DaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Component
public class QueryStringMongoDao<T> implements QueryStringDao<T> {

  private final MongoTemplate mongoTemplate;
  private final Class<T> genericType;
  private final int acceptRange;

  @Autowired
  public QueryStringMongoDao(MongoTemplate mongoTemplate, Class<T> genericType, int acceptRange) {
    this.mongoTemplate = mongoTemplate;
    this.genericType = genericType;
    this.acceptRange = acceptRange;
  }

  public DaoResponse<T> find(MultivaluedMap<String, String> filters) {
    Query query = createQuery(filters);
    List<T> objects = mongoTemplate.find(query, genericType);
    long total = mongoTemplate.count(new Query(), genericType);
    return new DaoResponse<>(objects, query.getSkip(), query.getSkip() + query.getLimit(), total, acceptRange);
  }

  public long count(MultivaluedMap<String, String> filters) {
    Query query = createQuery(filters);
    return mongoTemplate.count(query, genericType);
  }

  public DaoResponse<T> first(MultivaluedMap<String, String> filters) {
    Query query = createQuery(filters);
    query.skip(0);
    query.limit(1);
    List<T> objects = mongoTemplate.find(query, genericType);
    long total = mongoTemplate.count(new Query(), genericType);
    return new DaoResponse<>(objects, query.getSkip(), query.getSkip() + query.getLimit(), total, acceptRange);
  }

  private Query createQuery(MultivaluedMap<String, String> filters) {
    Query query = new Query();

    if (filters == null) {
      return query;
    }

    for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
      switch (entry.getKey()) {
        case "range":
          String[] range = entry.getValue().get(0).split("-");
          query.skip(Integer.parseInt(range[0]));
          query.limit(Integer.parseInt(range[1]));
          break;
        case "fields":
          for (String field : entry.getValue()){
            query.fields().include(field);
          }
          break;
        case "asc":
          query.with(new Sort(Sort.Direction.ASC, entry.getValue().get(0)));
          break;
        case "desc":
          query.with(new Sort(Sort.Direction.DESC, entry.getValue().get(0)));
          break;
        default:
          query.addCriteria(Criteria.where(entry.getKey()).in(entry.getValue()));
          break;
      }
    }

    if (query.getLimit() == 0 || query.getLimit() > acceptRange) {
      query.limit(acceptRange);
    }

    return query;
  }
}
