package io.harness.rest.helper.provisioners;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.pipelines.PipelineConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ProvisionersHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath createProvisioner(
      RequestSpecification requestSpecification, String provisionerName, String type, String appId) {
    JsonObject pipelineObject = new JsonObject();
    pipelineObject.addProperty("name", provisionerName);
    pipelineObject.addProperty("infrastructureProvisionerType", type);
    pipelineObject.addProperty("scriptBody", "echo");
    requestSpecification.queryParams("appId", appId);
    requestSpecification.body(pipelineObject.toString());
    Response addProvisioner =
        genericRequestBuilder.postCall(requestSpecification, ProvisionersConstants.URI_PROVISIONER_CREATION);
    return addProvisioner.jsonPath();
  }

  public JsonPath getProvisionerId(String provisionerName, String appId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("search[0][field]", "name");
    requestSpecification.queryParam("search[0][op]", "CONTAINS");
    requestSpecification.queryParam("search[0][value]", provisionerName);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("routingId", defaultAccountId);
    Response pipelineIdResponse =
        genericRequestBuilder.getCall(requestSpecification, ProvisionersConstants.URI_PROVISIONER_SEARCH);
    return pipelineIdResponse.jsonPath();
  }

  public Response editProvisionerName(
      RequestSpecification requestSpecification, String provisionerName, String newName, String appId) {
    String uuid = getProvisionerId(provisionerName, appId).getString("resource.response[0].uuid");
    JsonObject provisionerData = new JsonObject();
    provisionerData.addProperty("name", newName);
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("uuid", uuid);
    Response editProvisionerResponse =
        genericRequestBuilder.putCall(requestSpecification, ProvisionersConstants.URI_PROVISIONER_UPDATE);
    return editProvisionerResponse;
  }

  public JsonPath deleteProvisioner(RequestSpecification requestSpecification, String provisionerName, String appId) {
    String uuid = getProvisionerId(provisionerName, appId).getString("resource.response[0].uuid");
    requestSpecification.queryParam("routingId", defaultAccountId);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("provisionerId", uuid);
    Response response =
        genericRequestBuilder.deleteCall(requestSpecification, ProvisionersConstants.URI_PROVISIONER_UPDATE);
    return response.jsonPath();
  }
}
