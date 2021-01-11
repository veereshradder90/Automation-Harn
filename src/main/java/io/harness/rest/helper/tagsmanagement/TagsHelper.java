package io.harness.rest.helper.tagsmanagement;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TagsHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath createTag(RequestSpecification requestSpecification, String name) {
    JsonObject tagData = new JsonObject();
    tagData.addProperty("name", name);
    requestSpecification.body(tagData.toString());
    Response tagResponse = genericRequestBuilder.postCall(requestSpecification, TagsConstants.TAGS_URI);
    return tagResponse.jsonPath();
  }

  public JsonPath deleteTag(RequestSpecification requestSpecification, String name) {
    JsonObject tagData = new JsonObject();
    tagData.addProperty("name", name);
    requestSpecification.body(tagData.toString());
    Response tagResponse = genericRequestBuilder.deleteCall(requestSpecification, TagsConstants.TAGS_URI);
    return tagResponse.jsonPath();
  }
}
