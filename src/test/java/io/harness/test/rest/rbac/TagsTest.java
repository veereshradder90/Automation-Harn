package io.harness.test.rest.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.tagsmanagement.TagsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagsTest extends AbstractTest {
  TagsHelper tagsHelper = new TagsHelper();

  @Test
  public void createTagsTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_managetags@mailinator.com", "Harness@123");
    JsonPath createTagsResponse = tagsHelper.createTag(requestSpecificationObject, "dummy");
    assertThat(createTagsResponse.getString("responseMessages.message")
                   .equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  public void deleteTagsTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_managetags@mailinator.com", "Harness@123");
    JsonPath deleteTagsResponse = tagsHelper.deleteTag(requestSpecificationObject, "Platform-Sanity");
    assertThat(deleteTagsResponse.getString("responseMessages.message")
                   .equalsIgnoreCase("Invalid request: User not authorized"));
  }
}
