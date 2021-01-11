package io.harness.rest.matchers;

import io.restassured.response.Response;

public class ResponseStatusMatcher implements Matcher {
  @Override
  public boolean matches(Object actual, Object expected) {
    if (actual == null) {
      return false;
    }
    Response response = (Response) actual;
    return response.getStatusCode() == Integer.parseInt(expected.toString());
  }
}
