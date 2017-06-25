package com.fbellotti.api_ws_spring.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
@XmlRootElement(name="error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorRemoteResponse {

  /**
   * error identifier
   */
  private String code;

  /**
   * message describing condition
   */
  private String message;

  public ErrorRemoteResponse() {
    //empty
  }

  public ErrorRemoteResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }


  public String getMessage() {
    return message;
  }
}
