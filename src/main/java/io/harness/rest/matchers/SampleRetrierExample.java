package io.harness.rest.matchers;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.utils.Retry;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SampleRetrierExample extends CoreUtils {
  public static void main(String[] args) {
    System.out.println("done");
    StringEqualMatcher matcher = new StringEqualMatcher();
    Retry<Object> retry = new Retry<Object>(5, 2000);
    Object retryOutput = retry.executeWithRetry(() -> new SampleRetrierExample().sample(), matcher, "nats3");
    System.out.println(retryOutput);

    ResponseStatusMatcher statusMatcher = new ResponseStatusMatcher();
    Object sampleOutput = retry.executeWithRetry(() -> new SampleRetrierExample().responseSample(), statusMatcher, 200);
    System.out.println("response is " + sampleOutput);
  }

  public String sample() {
    String str = "nats3";
    return str;
  }

  public Response responseSample() {
    // this is your actual post example
    // change the URI or call to post to test retry
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    Response response = GenericRequestBuilder.getCall(requestSpecificationObject, "/whitelist/isEnabled");
    return response;
  }
}
