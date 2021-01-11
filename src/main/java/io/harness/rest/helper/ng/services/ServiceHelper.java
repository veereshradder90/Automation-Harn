package io.harness.rest.helper.ng.services;

import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;
import java.util.Map;

public class ServiceHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public Response getServiceList(String orgIdentifier, String projectIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("orgIdentifier", orgIdentifier);
    requestSpecification.queryParam("projectIdentifier", projectIdentifier);
    Response response = genericRequestBuilder.getCall(requestSpecification, ServiceConstants.URI_SERVICES);
    return response;
  }

  public Response postService(Map<String, Object> map) throws FileNotFoundException {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.body(createRequestBody(map, ServiceConstants.REQUEST_JSON_SERVICE));
    Response response = genericRequestBuilder.postCall(requestSpecification, ServiceConstants.URI_SERVICES);

    return response;
  }

  public Response deleteService(String serviceIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("serviceIdentifier", serviceIdentifier);
    Response response = genericRequestBuilder.deleteCall(requestSpecification, ServiceConstants.URI_SERVICE_IDENTIFIER);
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
