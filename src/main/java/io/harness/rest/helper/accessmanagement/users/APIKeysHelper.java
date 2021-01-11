package io.harness.rest.helper.accessmanagement.users;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIKeysHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath getAPIKeys(RequestSpecification requestSpecification) {
    Response applicationResponse = genericRequestBuilder.getCall(requestSpecification, APIKeysConstants.API_KEYS_URI);
    return applicationResponse.jsonPath();
  }

  public JsonPath addAPIKeys(RequestSpecification requestSpecification, String name) {
    JsonObject apiData = new JsonObject();
    apiData.addProperty("name", name);
    requestSpecification.body(apiData.toString());
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, APIKeysConstants.API_KEYS_URI);
    return applicationResponse.jsonPath();
  }

  public JsonPath deleteAPIKeys(RequestSpecification requestSpecification, String name) {
    JsonObject apiData = new JsonObject();
    apiData.addProperty("name", name);
    requestSpecification.body(apiData.toString());
    Response applicationResponse =
        genericRequestBuilder.deleteCall(requestSpecification, APIKeysConstants.API_KEYS_URI);
    return applicationResponse.jsonPath();
  }
}
