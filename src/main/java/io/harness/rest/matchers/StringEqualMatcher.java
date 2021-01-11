package io.harness.rest.matchers;

public class StringEqualMatcher implements Matcher {
  @Override
  public boolean matches(Object expected, Object actual) {
    if (actual == null) {
      return false;
    }
    return expected.toString().equals(actual.toString());
  }
}
