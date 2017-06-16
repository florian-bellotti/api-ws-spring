package com.fbellotti.api_ws_spring.test.remote;

import com.fbellotti.api_ws_spring.dao.QueryStringDao;
import com.fbellotti.api_ws_spring.test.model.Customer;
import com.fbellotti.api_ws_spring.remote.QueryStringServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Component
public class CustomerQueryStringServiceImpl extends QueryStringServiceImpl<Customer> implements CustomerQueryStringService {

  @Autowired
  public CustomerQueryStringServiceImpl(QueryStringDao<Customer> daoRef) {
    super(daoRef);
  }

}
