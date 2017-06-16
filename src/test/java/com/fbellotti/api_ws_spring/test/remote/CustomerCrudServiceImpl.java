package com.fbellotti.api_ws_spring.test.remote;

import com.fbellotti.api_ws_spring.dao.CrudDao;
import com.fbellotti.api_ws_spring.remote.CrudRemoteImpl;
import com.fbellotti.api_ws_spring.test.model.Customer;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@Component
public class CustomerCrudServiceImpl extends CrudRemoteImpl<Customer> implements CustomerCrudService {

  public CustomerCrudServiceImpl(CrudDao<Customer> daoCrud) {
    super(daoCrud);
  }
}
