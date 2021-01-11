package io.harness.rest.helper.tags;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TagsHelper extends CoreUtils {
  public Response addTags(String name, String[] allowedValues) {
    JsonObject jsonObject = new JsonObject();
    JsonArray allowedValuesArray = new JsonArray();
    for (String email : allowedValues) {
      allowedValuesArray.add(email);
    }
    jsonObject.add("allowedValues", allowedValuesArray);
    jsonObject.addProperty("key", name);
    Response response = GenericRequestBuilder.postCall(jsonObject, TagsConstants.TAGS);
    return response;
  }

  public Response addTags(RequestSpecification requestSpecification, String name, String[] allowedValues) {
    JsonObject jsonObject = new JsonObject();
    JsonArray allowedValuesArray = new JsonArray();
    for (String email : allowedValues) {
      allowedValuesArray.add(email);
    }
    jsonObject.add("allowedValues", allowedValuesArray);
    jsonObject.addProperty("key", name);
    requestSpecification.body(jsonObject.toString());
    Response response = GenericRequestBuilder.postCall(requestSpecification, TagsConstants.TAGS);
    return response;
  }

  public Response deleteTags(String name) {
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecificationObject.queryParam("key", name);
    Response response = GenericRequestBuilder.deleteCall(TagsConstants.TAGS);
    return response;
  }

  public Response deleteTags(RequestSpecification requestSpecificationObject, String name) {
    requestSpecificationObject.queryParam("key", name);
    Response response = GenericRequestBuilder.deleteCall(TagsConstants.TAGS);
    return response;
  }
}
