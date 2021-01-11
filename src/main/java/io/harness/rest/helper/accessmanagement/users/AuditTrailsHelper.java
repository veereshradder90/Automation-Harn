package io.harness.rest.helper.accessmanagement.users;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuditTrailsHelper extends CoreUtils {
  private GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath getAuditTrails(RequestSpecification requestSpecification) {
    Response Response = genericRequestBuilder.getCall(requestSpecification, AuditTrailsConstants.GET_AUDIT_TRAILS);
    return Response.jsonPath();
  }
}
