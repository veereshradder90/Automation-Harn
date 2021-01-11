package io.harness.rest.helper.accessmanagement.apikeys;

import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.List;

public class APiKeysHelper extends CoreUtils {
  public Response getApiKeyList() {
    Response response = GenericRequestBuilder.getCall(APIKeyConstants.API_KEY);
    return response;
  }

  public String getApiKeyId(String name) {
    Response response = GenericRequestBuilder.getCall(APIKeyConstants.API_KEY);
    List<HashMap<String, String>> responseData = response.jsonPath().get("resource.response");
    for (HashMap apiKeyData : responseData) {
      if (name.equalsIgnoreCase(apiKeyData.get("name").toString())) {
        return apiKeyData.get("uuid").toString();
      }
    }
    return null;
  }

  public Response getApiKeyList(RequestSpecification requestSpecificationObject) {
    Response response = GenericRequestBuilder.getCall(requestSpecificationObject, APIKeyConstants.API_KEY);
    return response;
  }

  public Response addApiKey(String name) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", name);
    Response response = GenericRequestBuilder.postCall(jsonObject, APIKeyConstants.API_KEY);
    return response;
  }

  public Response addApiKey(RequestSpecification requestSpecificationObject, String name) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", name);
    requestSpecificationObject.body(jsonObject.toString());
    Response response = GenericRequestBuilder.postCall(requestSpecificationObject, APIKeyConstants.API_KEY);
    return response;
  }

  public Response deleteApiKey(String name) {
    String apiKeyId = getApiKeyId(name);
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecificationObject.pathParam("api_key_id", apiKeyId);
    Response response = GenericRequestBuilder.deleteCall(requestSpecificationObject, APIKeyConstants.API_KEY);
    return response;
  }

  public Response deleteApiKey(RequestSpecification requestSpecificationObject, String name) {
    String apiKeyId = getApiKeyId(name);
    requestSpecificationObject.pathParam("api_key_id", apiKeyId);
    Response response = GenericRequestBuilder.deleteCall(requestSpecificationObject, APIKeyConstants.API_KEY);
    return response;
  }
}
