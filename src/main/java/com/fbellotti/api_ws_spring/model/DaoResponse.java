package com.fbellotti.api_ws_spring.model;

import java.util.List;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class DaoResponse<T> {

  private List<T> items;
  private String contentRange;
  private int acceptRange;
  private Boolean partialResponse;

  public DaoResponse(List<T> items, int skip, int limit, long total, int acceptRange) {
    this.items = items;
    this.contentRange = skip + "-" + limit + "/" + total;
    this.acceptRange = acceptRange;
    partialResponse =  skip > 0 || limit < total;
  }

  public List<T> getItems() {
    return items;
  }

  public String getContentRange() {
    return contentRange;
  }

  public int getAcceptRange() {
    return acceptRange;
  }

  public Boolean getPartialResponse() {
    return partialResponse;
  }
}
