package io.harness.test.rest.rbac;

import io.harness.rest.helper.templatelibrary.TemplateLibraryHelper;
import io.harness.test.rest.base.AbstractTest;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateLibraryTest extends AbstractTest {
  TemplateLibraryHelper templateLibraryHelper = new TemplateLibraryHelper();
  /*
  @Test
  public void templateLibraryTest() {
      RequestSpecification requestSpecificationObject =
  GenericRequestBuilder.getRequestSpecificationObject("rbac_templatelibrary@mailinator.com", "Harness@123");

      JsonPath templateResponse = templateLibraryHelper.createShellTemplateLibrary(requestSpecificationObject);

      assertThat(templateResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not
  authorized"));
  }*/

  @Test
  public void deleteTemplateLibraryTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_templatelibrary@mailinator.com", "Harness@123");

    Response deleteResponse =
        templateLibraryHelper.deleteTemplateLibrary(requestSpecificationObject, "Platform-Sanity");
    assertThat(deleteResponse.getStatusCode()).isEqualTo(400);
  }
}
