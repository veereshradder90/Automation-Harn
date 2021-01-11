package io.harness.rest.helper.environments;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

@Slf4j
public class EnvironmentsHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  // create Environment using Json data created on fly
  public JsonPath createEnvironment(String name, String description, String environmentType, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject envData = new JsonObject();
    envData.addProperty("name", name);
    envData.addProperty("description", description);
    envData.addProperty("environmentType", environmentType);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.body(envData.toString());
    Response response = genericRequestBuilder.postCall(requestSpecification, EnvironmentConstants.ENV_CREATION_URI);
    // log.info(response.body().asString());
    return response.jsonPath();
  }

  // create Environment using Json data created on fly
  public JsonPath createEnvironmentRbac(String name, String description, String environmentType, String appId) {
    RequestSpecification requestSpecification =
        GenericRequestBuilder.getRequestSpecificationObject("dx-cp-rbac-user@mailinator.com", "Harness@123!");
    JsonObject envData = new JsonObject();
    envData.addProperty("name", name);
    envData.addProperty("description", description);
    envData.addProperty("environmentType", environmentType);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.body(envData.toString());
    Response response = genericRequestBuilder.postCall(requestSpecification, EnvironmentConstants.ENV_CREATION_URI);
    // log.info(response.body().asString());
    return response.jsonPath();
  }

  // Delete Environment using path parameters
  public JsonPath deleteEnvironment(String envId, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("envId", envId);
    Response response = genericRequestBuilder.deleteCall(requestSpecification, EnvironmentConstants.ENV_UPDATE_URI);
    // log.info(response.body().asString());
    return response.jsonPath();
  }

  public JsonPath createGCPK8SInfraDefinition(String infraDefName, String appId, String envId)
      throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject infradefdata = jReader.readJSONFiles(EnvironmentConstants.REQUEST_JSON_GCP_K8S_CREATION);
    infradefdata.addProperty("name", infraDefName);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("envId", envId);
    requestSpecification.body(infradefdata.toString());
    Response post = genericRequestBuilder.postCall(requestSpecification, EnvironmentConstants.INFRADEF_CREATE_URI);
    log.info(post.getBody().asString());
    return post.jsonPath();
  }

  public JsonPath createAWSSSHInfraDefinition(String infraDefName, String appId, String envId)
      throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject infradefdata = jReader.readJSONFiles(EnvironmentConstants.REQUEST_JSON_AWS_SSH_CREATION);
    infradefdata.addProperty("name", infraDefName);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("envId", envId);
    requestSpecification.body(infradefdata.toString());
    Response post = genericRequestBuilder.postCall(requestSpecification, EnvironmentConstants.INFRADEF_CREATE_URI);
    log.info(post.getBody().asString());
    return post.jsonPath();
  }
  public JsonPath createAWSSSHInfraDefinition(String infraDefName, String appId, String envId, String scopedToService)
      throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject infradefdata = jReader.readJSONFiles(EnvironmentConstants.REQUEST_JSON_AWS_SSH_CREATION);
    infradefdata.addProperty("name", infraDefName);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("envId", envId);
    JsonArray serviceIds = new JsonArray();
    serviceIds.add(scopedToService);
    infradefdata.add("scopedToServices", serviceIds);
    requestSpecification.body(infradefdata.toString());
    Response post = genericRequestBuilder.postCall(requestSpecification, EnvironmentConstants.INFRADEF_CREATE_URI);
    log.info(post.getBody().asString());
    return post.jsonPath();
  }

  public JsonPath createEnvironment(RequestSpecification requestSpecification, String environmentName, String appId) {
    try {
      JsonObject environmentData = jReader.readJSONFiles(EnvironmentConstants.REQUEST_ENVIRONMENT_CREATION_JSON);
      environmentData.addProperty("name", environmentName);
      environmentData.addProperty("description", "Automation");
      environmentData.addProperty("environmentType", "PROD");
      requestSpecification.queryParam("appId", appId);
      requestSpecification.body(environmentData.toString());
      Response addEnvironmentResponse =
          genericRequestBuilder.postCall(requestSpecification, EnvironmentConstants.ENV_CREATION_URI);
      return addEnvironmentResponse.jsonPath();
    } catch (FileNotFoundException e) {
      log.info("Error" + e.getMessage());
    }
    return null;
  }

  public JsonPath getEnvironmentId(String environmentName, String appId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("search[0][field]", "keywords");
    requestSpecification.queryParam("search[0][op]", "CONTAINS");
    requestSpecification.queryParam("search[0][value]", environmentName);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("withTags", true);
    Response serviceIdResponse =
        genericRequestBuilder.getCall(requestSpecification, EnvironmentConstants.ENV_CREATION_URI);
    return serviceIdResponse.jsonPath();
  }

  public Response editEnvironmentName(
      RequestSpecification requestSpecification, String environmentName, String newName, String appId) {
    String uuid = getEnvironmentId(environmentName, appId).getString("resource.response[0].uuid");
    JsonObject serviceData = new JsonObject();
    serviceData.addProperty("name", newName);
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("environmentId", uuid);
    Response editResponse = genericRequestBuilder.putCall(requestSpecification, EnvironmentConstants.ENV_UPDATE_URI);
    return editResponse;
  }

  public JsonPath deleteEnvironment(RequestSpecification requestSpecification, String envId, String appId) {
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("envId", envId);
    Response response = genericRequestBuilder.deleteCall(requestSpecification, EnvironmentConstants.ENV_UPDATE_URI);
    return response.jsonPath();
  }
}
