package com.fbellotti.api_ws_spring.test.config;

import com.fbellotti.api_ws_spring.dao.CrudMongoDao;
import com.fbellotti.api_ws_spring.dao.QueryStringMongoDao;
import com.fbellotti.api_ws_spring.test.remote.CustomerCrudServiceImpl;
import com.fbellotti.api_ws_spring.test.remote.CustomerQueryStringServiceImpl;
import com.fbellotti.api_ws_spring.test.model.Customer;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Configuration
public class TestConfig {

  @Bean
  public MongoTemplate mongoTemplate() throws UnknownHostException {
    MongoDbFactory dbFactory = new SimpleMongoDbFactory(new MongoClient(), "Test");
    return new MongoTemplate(dbFactory);
  }

  @Bean
  public QueryStringMongoDao<Customer> customerQueryStringMongoDao() throws UnknownHostException {
    return new QueryStringMongoDao<>(mongoTemplate(), Customer.class, 1000);
  }

  @Bean
  public CustomerQueryStringServiceImpl customerQueryStringService() throws UnknownHostException {
    return new CustomerQueryStringServiceImpl(customerQueryStringMongoDao());
  }

  @Bean
  public CrudMongoDao<Customer> customerCrudMongoDao() throws UnknownHostException {
    return new CrudMongoDao<>(mongoTemplate(), Customer.class, "identifier");
  }

  @Bean
  public CustomerCrudServiceImpl customerCrudService() throws UnknownHostException {
    return new CustomerCrudServiceImpl(customerCrudMongoDao());
  }
}
