package com.fbellotti.api_ws_spring.test.config;

import com.fbellotti.api_ws_spring.dao.RefApiMongoDao;
import com.fbellotti.api_ws_spring.test.remote.CustomerRemoteImpl;
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
  public RefApiMongoDao<Customer> refApiMongoDaoCustomer() throws UnknownHostException {
    return new RefApiMongoDao<>(mongoTemplate(), Customer.class, 1000);
  }

  @Bean
  public CustomerRemoteImpl customerRemoteImpl() throws UnknownHostException {
    return new CustomerRemoteImpl(refApiMongoDaoCustomer());
  }

}
