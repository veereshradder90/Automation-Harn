package io.harness.rest.helper.ng.environments;

import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * author: shaswat.deep
 */
@Slf4j
public class EnvironmentHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public Response getEnvsList(String orgIdentifier, String projectIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("orgIdentifier", orgIdentifier);
    requestSpecification.queryParam("projectIdentifier", projectIdentifier);
    Response response = genericRequestBuilder.getCall(requestSpecification, EnvironmentConstants.URI_ENVS);
    return response;
  }

  public Response postEnv(Map<String, Object> map) throws FileNotFoundException {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.body(createRequestBody(map, EnvironmentConstants.REQUEST_JSON_ENVIRONMENT));
    Response response = genericRequestBuilder.postCall(requestSpecification, EnvironmentConstants.URI_ENVS);

    return response;
  }

  public Response deleteEnv(String envIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("environmentIdentifier", envIdentifier);
    Response response = genericRequestBuilder.deleteCall(requestSpecification, EnvironmentConstants.URI_ENV_IDENTIFIER);
    return response;
  }

  private String createRequestBody(Map<String, Object> map, String path) throws FileNotFoundException {
    JsonObject appReqData = jReader.readJSONFiles(path);
    for (String name : map.keySet()) {
      appReqData.addProperty(name, (String) map.get(name));
    }
    return appReqData.toString();
  }
}
