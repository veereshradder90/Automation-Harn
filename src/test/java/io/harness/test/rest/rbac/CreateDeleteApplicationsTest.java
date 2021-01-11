package io.harness.test.rest.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateDeleteApplicationsTest extends AbstractTest {
  ApplicationHelper applicationHelper = new ApplicationHelper();

  @Test
  public void createApplicationTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_createapplication@mailinator.com", "Harness@123");
    JsonPath addResponse = applicationHelper.createApp(requestSpecificationObject, "dummy");
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  public void deleteApplicationTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_createapplication@mailinator.com", "Harness@123");
    Response deleteResponse =
        applicationHelper.deleteApplication(requestSpecificationObject, "platform application", "name");
    assertThat(deleteResponse.getStatusCode()).isEqualTo(400);
  }
}
