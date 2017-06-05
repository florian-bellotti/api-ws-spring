package com.fbellotti.api_ws_spring.model;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class DaoResponse<T> {

  private T object;
  private String contentRange;
  private int acceptRange;

  public DaoResponse(T object, int from, int to, long total, int acceptRange) {
    this.object = object;
    this.contentRange = from +  "-" + to  + "/" + total;
    this.acceptRange = acceptRange;
  }

  public T getObject() {
    return object;
  }

  public String getContentRange() {
    return contentRange;
  }

  public int getAcceptRange() {
    return acceptRange;
  }
}
