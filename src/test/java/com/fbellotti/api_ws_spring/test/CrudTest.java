package com.fbellotti.api_ws_spring.test;

import com.fbellotti.api_ws_spring.test.config.TestConfig;
import com.fbellotti.api_ws_spring.test.model.Address;
import com.fbellotti.api_ws_spring.test.model.Customer;
import com.fbellotti.api_ws_spring.test.remote.CustomerRemoteImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class})
public class CrudTest {

  @Autowired private CustomerRemoteImpl customerRemoteImpl;
  @Autowired private MongoTemplate mongoTemplate;

  @Before
  public void init() {
    clean();
  }

  @Test
  public void crud() {
    MultivaluedMap<String, String> queryParams;
    UriInfo uriInfo;
    /*
       create
     */
    Address address = new Address("59000", "Lille");
    Customer customer = new Customer("Florian", "BELLOTTI", address);

    // insert
    Response response = customerRemoteImpl.create(customer);

    // assert
    Assert.assertEquals(201, response.getStatus());
    List<Customer> customers = mongoTemplate.find(new Query(), Customer.class);
    Assert.assertEquals(1, customers.size());
    Assert.assertEquals("Florian", customers.get(0).getFirstName());
    String id = customers.get(0).getIdentifier();

    /*
       read
     */
    // mock
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("id",id);
    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    // read
    response = customerRemoteImpl.find();

    // assert
    Assert.assertEquals(200, response.getStatus());
    Assert.assertEquals(1, ((List<Customer>) response).size());
    Assert.assertEquals("Florian", ((List<Customer>) customers).get(0).getFirstName());

    /*
       update
     */
    customer.setFirstName("Test");

    // update
    response = customerRemoteImpl.update(customer);
    Assert.assertEquals(400, response.getStatus());

    // update
    customer.setIdentifier(id);
    response = customerRemoteImpl.update(customer);

    // assert
    Assert.assertEquals(200, response.getStatus());
    customers = mongoTemplate.find(new Query(), Customer.class);
    Assert.assertEquals(1, customers.size());
    Assert.assertEquals("Test", customers.get(0).getFirstName());

    /*
       delete
     */
    // mock
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("id",id);
    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    // delete
    response = customerRemoteImpl.delete();

    // assert
    Assert.assertEquals(204, response.getStatus());
    customers = mongoTemplate.find(new Query(), Customer.class);
    Assert.assertEquals(0, customers.size());
  }

  @After
  public void clean() {
    mongoTemplate.dropCollection(Customer.class);
  }
}
