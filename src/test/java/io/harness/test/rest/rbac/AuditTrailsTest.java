package io.harness.test.rest.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.accessmanagement.users.AuditTrailsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditTrailsTest extends AbstractTest {
  AuditTrailsHelper auditTrailsHelper = new AuditTrailsHelper();

  @Test
  public void auditTrailsTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_audittrails@mailinator.com", "Harness@123");
    JsonPath auditResponse = auditTrailsHelper.getAuditTrails(requestSpecificationObject);
    assertThat(
        auditResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }
}
