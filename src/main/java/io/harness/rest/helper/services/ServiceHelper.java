package io.harness.rest.helper.services;

import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
public class ServiceHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  // create Service using Json data created on fly
  public JsonPath createK8SV2Service(String serviceName, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject serviceData = new JsonObject();
    serviceData.addProperty("name", serviceName);
    serviceData.addProperty("artifactType", ServiceConstants.K8SV2_SERVICE_ARTIFACT_TYPE);
    serviceData.addProperty("deploymentType", ServiceConstants.K8SV2_SERVICE_DEPLOYMENT_TYPE);
    serviceData.addProperty("k8sV2", true);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.body(serviceData.toString());
    Response response = genericRequestBuilder.postCall(requestSpecification, ServiceConstants.URI_SERVICE_CREATION);
    // log.info(response.body().asString());
    return response.jsonPath();
  }

  // Delete Service using path parameters
  public JsonPath deleteService(String serviceId, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("serviceId", serviceId);
    Response response = genericRequestBuilder.deleteCall(requestSpecification, ServiceConstants.URI_SERVICE_DELETION);
    // log.info(response.body().asString());
    return response.jsonPath();
  }

  public Response addServiceVariable(Map<String, String> variableProperties) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("appId", variableProperties.get("appId"));
    requestSpecification.queryParam("entityId", variableProperties.get("entityId"));
    requestSpecification.queryParam("entityType", "SERVICE");
    requestSpecification.queryParam("routingId", defaultAccountId);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", variableProperties.get("name"));
    jsonObject.addProperty("entityId", variableProperties.get("entityId"));
    jsonObject.addProperty("entityType", "SERVICE");
    jsonObject.addProperty("type", variableProperties.get("type"));
    jsonObject.addProperty("value", variableProperties.get("value"));
    requestSpecification.body(jsonObject.toString());
    Response response = GenericRequestBuilder.postCall(requestSpecification, ServiceConstants.URI_SERVICE_VARIABLES);
    return response;
  }

  // create SSH Service using Json data created on fly
  public JsonPath createSSHService(String serviceName, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject serviceData = new JsonObject();
    serviceData.addProperty("name", serviceName);
    serviceData.addProperty("artifactType", ServiceConstants.SSH_SERVICE_ARTIFACT_TYPE);
    serviceData.addProperty("deploymentType", ServiceConstants.SSH_SERVICE_DEPLOYMENT_TYPE);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.body(serviceData.toString());
    Response response = genericRequestBuilder.postCall(requestSpecification, ServiceConstants.URI_SERVICE_CREATION);
    // log.info(response.body().asString());
    return response.jsonPath();
  }

  public JsonPath createK8SV2Service(RequestSpecification requestSpecification, String serviceName, String appId) {
    try {
      JsonObject serviceData = jReader.readJSONFiles(ServiceConstants.REQUEST_SERVICE_CREATION_JSON);
      serviceData.addProperty("name", serviceName);
      serviceData.addProperty("artifactType", "DOCKER");
      serviceData.addProperty("deploymentType", "KUBERNETES");
      serviceData.addProperty("k8sV2", true);
      requestSpecification.queryParam("appId", appId);
      requestSpecification.body(serviceData.toString());
      Response addServiceResponse =
          genericRequestBuilder.postCall(requestSpecification, ServiceConstants.URI_SERVICE_CREATION);
      return addServiceResponse.jsonPath();
    } catch (FileNotFoundException e) {
      log.info("Error" + e.getMessage());
    }
    return null;
  }

  public JsonPath getServiceId(String serviceName, String appId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("sort[0][field]", "name");
    requestSpecification.queryParam("sort[0][direction]", "ASC");
    requestSpecification.queryParam("search[0][field]", "keywords");
    requestSpecification.queryParam("search[0][op]", "CONTAINS");
    requestSpecification.queryParam("search[0][value]", serviceName);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("withTags", true);
    Response serviceIdResponse =
        genericRequestBuilder.getCall(requestSpecification, ServiceConstants.URI_SERVICE_CREATION);
    return serviceIdResponse.jsonPath();
  }

  public Response editService(
      RequestSpecification requestSpecification, String serviceName, String newName, String appId) {
    JsonObject serviceData = new JsonObject();
    serviceData.addProperty("name", newName);
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("serviceId", getServiceId(serviceName, appId));
    Response editResponse = genericRequestBuilder.putCall(requestSpecification, ServiceConstants.URI_SERVICE_DELETION);
    return editResponse;
  }

  public Response deleteService(RequestSpecification requestSpecification, String serviceId, String appId) {
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("serviceId", serviceId);
    Response response = genericRequestBuilder.deleteCall(requestSpecification, ServiceConstants.URI_SERVICE_DELETION);
    return response;
  }
}
