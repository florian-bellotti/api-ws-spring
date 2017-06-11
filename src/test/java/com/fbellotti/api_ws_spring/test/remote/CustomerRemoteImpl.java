package com.fbellotti.api_ws_spring.test.remote;

import com.fbellotti.api_ws_spring.dao.RefApiDao;
import com.fbellotti.api_ws_spring.test.model.Customer;
import com.fbellotti.api_ws_spring.remote.RefApiRemoteImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Component
public class CustomerRemoteImpl extends RefApiRemoteImpl<Customer> implements CustomerRemote {

  @Autowired
  public CustomerRemoteImpl(RefApiDao<Customer> daoRef) {
    super(daoRef);
  }

}
