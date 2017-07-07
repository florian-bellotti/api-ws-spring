package com.fbellotti.api_ws_spring.test;

import com.fbellotti.api_ws_spring.test.model.Address;
import com.fbellotti.api_ws_spring.test.remote.CustomerQueryStringServiceImpl;
import com.fbellotti.api_ws_spring.test.config.TestConfig;
import com.fbellotti.api_ws_spring.test.model.Customer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
public class RemoteTest {

  @Autowired private CustomerQueryStringServiceImpl customerRemoteImpl;
  @Autowired private MongoTemplate mongoTemplate;

  private MultivaluedMap<String, String> queryParams;
  private UriInfo uriInfo;

  @Before
  public void prepareData() {
    clean();

    Address address = new Address("59000", "Lille");

    Customer customer = new Customer("Florian", "BELLOTTI", address);
    //customer.setIdentifier("eee");
    mongoTemplate.insert(customer);

    customer = new Customer("Test", "TEST", address);
    mongoTemplate.insert(customer);

    customer = new Customer("Data", "DATA", address);
    mongoTemplate.insert(customer);

    for (int i = 1; i <= 1500; i++) {
      customer = new Customer("Eeee", "EEEE", address);
      mongoTemplate.insert(customer);
    }
  }

  @Test
  public void countTest() {
    queryParams = new MultivaluedHashMap<>();

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    Response response = customerRemoteImpl.count(uriInfo);
    Assert.assertEquals(200, response.getStatus());
    Assert.assertEquals(new Long(1503), response.getEntity());

    queryParams = new MultivaluedHashMap<>();
    queryParams.add("firstName", "Florian");
    queryParams.add("firstName", "Test");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.count(uriInfo);
    Assert.assertEquals(200, response.getStatus());
    Assert.assertEquals(new Long(2), response.getEntity());
  }

  @Test
  public void findTest() {
    queryParams = new MultivaluedHashMap<>();

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    Response response = customerRemoteImpl.find(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    List<Customer> list = (List<Customer>) response.getEntity();
    Assert.assertEquals(1000, list.size());
    for (Customer customer : list) {
      Assert.assertNotNull(customer.getFirstName());
      Assert.assertNotNull(customer.getLastName());
    }
  }

  @Test
  public void headerResponseRangeTes() {
    queryParams = new MultivaluedHashMap<>();

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    Response response = customerRemoteImpl.find(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    Assert.assertEquals("0-1000/1503", response.getHeaderString("content-range"));
    Assert.assertEquals("1000", response.getHeaderString("accept-range"));

    queryParams = new MultivaluedHashMap<>();
    queryParams.add("range", "1-2000");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.find(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    Assert.assertEquals("1-1001/1503", response.getHeaderString("content-range"));
    Assert.assertEquals("1000", response.getHeaderString("accept-range"));
  }

  @Test
  public void firstTest() {
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("asc", "firstName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    Response response = customerRemoteImpl.first(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    List<Customer> list = (List<Customer>) response.getEntity();
    Assert.assertEquals(1, list.size());
  }

  @Test
  public void fieldTest() {
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("fields", "firstName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    Response response = customerRemoteImpl.find(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    for (Customer customer : (List<Customer>) response.getEntity()) {
      Assert.assertNotNull(customer.getFirstName());
      Assert.assertNull(customer.getLastName());
    }

    queryParams = new MultivaluedHashMap<>();
    queryParams.add("fields", "firstName");
    queryParams.add("fields", "lastName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.find(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    for (Customer customer : (List<Customer>) response.getEntity()) {
      Assert.assertNotNull(customer.getFirstName());
      Assert.assertNotNull(customer.getLastName());
    }

    queryParams = new MultivaluedHashMap<>();
    queryParams.add("fields", "firstName");
    queryParams.add("fields", "address.postal");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.find(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    for (Customer customer : (List<Customer>) response.getEntity()) {
      Assert.assertNotNull(customer.getFirstName());
      Assert.assertNull(customer.getAddress().getCity());
      Assert.assertNotNull(customer.getAddress().getPostal());
    }
  }

  @Test
  public void rangeTest() {
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("asc", "firstName");
    queryParams.add("range", "1-3");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    Response response = customerRemoteImpl.find(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    Assert.assertEquals(3, ((List<Customer>) response.getEntity()).size());
    for (Customer customer : (List<Customer>) response.getEntity()) {
      Assert.assertNotEquals("Data", customer.getFirstName());
      Assert.assertNotNull(customer.getLastName());
    }
  }

  @Test
  public void sortedByTest() {
    // Asc Test
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("asc", "firstName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    Response response = customerRemoteImpl.first(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    Assert.assertEquals(1, ((List<Customer>) response.getEntity()).size());
    for (Customer customer : (List<Customer>) response.getEntity()) {
      Assert.assertEquals("Data", customer.getFirstName());
      Assert.assertNotNull(customer.getLastName());
    }

    // Desc Test
    queryParams=new MultivaluedHashMap<>();
    queryParams.add("desc", "firstName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.first(uriInfo);
    Assert.assertEquals(206, response.getStatus());
    Assert.assertEquals(1, ((List<Customer>) response.getEntity()).size());
    for (Customer customer : (List<Customer>) response.getEntity()) {
      Assert.assertEquals("Test", customer.getFirstName());
      Assert.assertNotNull(customer.getLastName());
    }
  }

  @After
  public void clean() {
    mongoTemplate.dropCollection(Customer.class);
  }

}
