package com.fbellotti.api_ws_spring.response;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class ApiResponse<T> {

  private ResponseStatus status;
  private String message;
  private String code;
  private T object;
  private String contentRange;
  private String acceptRange;

  public void setApiResponse(T object, String code, String message, ResponseStatus status, String contentRange, String acceptRange) {
    setObject(object);
    setCode(code);
    setMessage(message);
    setStatus(status);
    setContentRange(contentRange);
    setAcceptRange(acceptRange);
  }

  public ResponseStatus getStatus() {
    return status;
  }

  private void setStatus(ResponseStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  private void setMessage(String message) {
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  private void setCode(String code) {
    this.code = code;
  }

  public T getObject() {
    return object;
  }

  private void setObject(T object) {
    this.object = object;
  }

  public String getContentRange() {
    return contentRange;
  }

  private void setContentRange(String contentRange) {
    this.contentRange = contentRange;
  }

  public String getAcceptRange() {
    return acceptRange;
  }

  private void setAcceptRange(String acceptRange) {
    this.acceptRange = acceptRange;
  }

  @Override
  public String toString() {
    return "RefApiResponse [status=" + status + ", message=" + message
        + ", code=" + code + "]";
  }
}
