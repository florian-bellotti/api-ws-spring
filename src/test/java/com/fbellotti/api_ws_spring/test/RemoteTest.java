package com.fbellotti.api_ws_spring.test;

import com.fbellotti.api_ws_spring.response.ApiResponse;
import com.fbellotti.api_ws_spring.response.ResponseStatus;
import com.fbellotti.api_ws_spring.test.model.Address;
import com.fbellotti.api_ws_spring.test.remote.CustomerRemoteImpl;
import com.fbellotti.api_ws_spring.test.config.TestConfig;
import com.fbellotti.api_ws_spring.test.model.Customer;
import org.junit.After;
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
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class})
public class RemoteTest {

  @Autowired private CustomerRemoteImpl customerRemoteImpl;
  @Autowired private MongoTemplate mongoTemplate;

  private MultivaluedMap<String, String> queryParams;
  private UriInfo uriInfo;

  @Before
  public void prepareData() {
    Address address = new Address("59000", "Lille");

    Customer customer = new Customer("Florian", "BELLOTTI", address);
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
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    ApiResponse<Long> response = customerRemoteImpl.count(uriInfo);
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals(new Long(1503), response.getObject());

    queryParams = new MultivaluedHashMap<>();
    queryParams.add("firstName","Florian");
    queryParams.add("firstName","Test");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.count(uriInfo);
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals("200", response.getCode());
    assertEquals(new Long(2), response.getObject());
  }

  @Test
  public void findTest() {
    queryParams = new MultivaluedHashMap<>();

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    ApiResponse<List<Customer>> response = customerRemoteImpl.find(uriInfo);
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals(1000, response.getObject().size());
    for (Customer customer : response.getObject()) {
      assertNotNull(customer.getFirstName());
      assertNotNull(customer.getLastName());
    }
  }

  @Test
  public void headerResponseRangeTes() {
    queryParams = new MultivaluedHashMap<>();

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    ApiResponse<List<Customer>> response = customerRemoteImpl.find(uriInfo);
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals("0-1000/1503", response.getContentRange());
    assertEquals("1000", response.getAcceptRange());

    queryParams = new MultivaluedHashMap<>();
    queryParams.add("range","1-2000");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.find(uriInfo);
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals("1-1001/1503", response.getContentRange());
    assertEquals("1000", response.getAcceptRange());
  }

  @Test
  public void firstTest() {
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("asc","firstName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    ApiResponse<List<Customer>> response = customerRemoteImpl.first(uriInfo);
    assertEquals("Success", response.getMessage());
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals(1, response.getObject().size());
  }

  @Test
  public void fieldTest() {
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("fields","firstName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    ApiResponse<List<Customer>> response = customerRemoteImpl.find(uriInfo);
    assertEquals("Success", response.getMessage());
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    for (Customer customer : response.getObject()) {
      assertNotNull(customer.getFirstName());
      assertNull(customer.getLastName());
    }

    queryParams = new MultivaluedHashMap<>();
    queryParams.add("fields","firstName");
    queryParams.add("fields","lastName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.find(uriInfo);
    assertEquals("Success", response.getMessage());
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    for (Customer customer : response.getObject()) {
      assertNotNull(customer.getFirstName());
      assertNotNull(customer.getLastName());
    }

    queryParams = new MultivaluedHashMap<>();
    queryParams.add("fields","firstName");
    queryParams.add("fields","address.postal");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    response = customerRemoteImpl.find(uriInfo);
    assertEquals("Success", response.getMessage());
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    for (Customer customer : response.getObject()) {
      assertNotNull(customer.getFirstName());
      assertNull(customer.getAddress().getCity());
      assertNotNull(customer.getAddress().getPostal());
    }
  }

  @Test
  public void rangeTest() {
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("asc","firstName");
    queryParams.add("range","1-3");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    ApiResponse<List<Customer>> response = customerRemoteImpl.find(uriInfo);
    assertEquals("Success", response.getMessage());
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals(3, response.getObject().size());
    for (Customer customer : response.getObject()) {
      assertNotEquals("Data", customer.getFirstName());
      assertNotNull(customer.getLastName());
    }
  }

  @Test
  public void sortedByTest() {
    // Asc Test
    queryParams = new MultivaluedHashMap<>();
    queryParams.add("asc","firstName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

    ApiResponse<List<Customer>> response = customerRemoteImpl.first(uriInfo);
    assertEquals("Success", response.getMessage());
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals(1, response.getObject().size());
    for (Customer customer : response.getObject()) {
      assertEquals("Data", customer.getFirstName());
      assertNotNull(customer.getLastName());
    }

    // Desc Test
    queryParams=new MultivaluedHashMap<>();
    queryParams.add("desc","firstName");

    uriInfo = Mockito.mock(UriInfo.class);
    Mockito.when(uriInfo.getPathParameters()).thenReturn(queryParams);

     response = customerRemoteImpl.first(uriInfo);
    assertEquals("Success", response.getMessage());
    assertEquals(ResponseStatus.SUCCESS, response.getStatus());
    assertEquals(1, response.getObject().size());
    for (Customer customer : response.getObject()) {
      assertEquals("Test", customer.getFirstName());
      assertNotNull(customer.getLastName());
    }
  }

  @After
  public void clean() {
    mongoTemplate.dropCollection(Customer.class);
  }

}
