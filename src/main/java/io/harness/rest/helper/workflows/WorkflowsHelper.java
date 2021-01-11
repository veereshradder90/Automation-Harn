package io.harness.rest.helper.workflows;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.workflows.workflowSteps.jenkinsStep.WorkflowStepEnum;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;


import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class WorkflowsHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath createWorkflow(String workflowName, String workflowDescription, String appId, String envId,
      String serviceId, String infraDefinitionId, String workflowType) throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_WORKFLOW_CREATION);
    workflowData.addProperty("name", workflowName);
    workflowData.addProperty("description", workflowDescription);
    workflowData.addProperty("envId", envId);
    workflowData.addProperty("serviceId", serviceId);
    workflowData.addProperty("infraDefinitionId", infraDefinitionId);
    workflowData.getAsJsonObject("orchestrationWorkflow").addProperty("orchestrationWorkflowType", workflowType);

    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("sort[0][field]", "createdAt");
    requestSpecification.queryParam("sort[0][direction]", "DESC");
    requestSpecification.body(workflowData.toString());
    Response post = genericRequestBuilder.postCall(requestSpecification, WorkflowConstants.WORKFLOW_CREATION_URI);
    log.info(post.getBody().asString());
    return post.jsonPath();
  }

  public JsonPath createBuildWorkflow(String appId, String workflowType, String workflowName)
      throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_WORKFLOW_CREATION);
    workflowData.addProperty("name", workflowName);
    workflowData.remove("envId");
    workflowData.getAsJsonObject("orchestrationWorkflow").addProperty("orchestrationWorkflowType", workflowType);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("sort[0][field]", "createdAt");
    requestSpecification.queryParam("sort[0][direction]", "DESC");
    requestSpecification.body(workflowData.toString());
    Response post = genericRequestBuilder.postCall(requestSpecification, WorkflowConstants.WORKFLOW_CREATION_URI);
    log.info(post.getBody().asString());
    return post.jsonPath();
  }

  public JsonPath templatiseWorkflow(String workflowName, String workflowDescription, String appId, String workflowId,
      String envId, String serviceId, String infraDefinitionId, String templatiseInfra) throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_WORKFLOW_TEMPLATISATION);
    workflowData.addProperty("name", workflowName);
    workflowData.addProperty("description", workflowDescription);
    workflowData.addProperty("envId", envId);
    workflowData.addProperty("serviceId", serviceId);
    workflowData.addProperty("uuid", workflowId);
    workflowData.addProperty("infraDefinitionId", infraDefinitionId);
    JsonArray expressions = new JsonArray();
    JsonObject expression = new JsonObject();
    expression.addProperty("fieldName", "infraDefinitionId");
    expression.addProperty("expression", templatiseInfra);
    JsonObject metadata = new JsonObject();
    metadata.addProperty("entityType", "INFRASTRUCTURE_DEFINITION");
    metadata.addProperty("relatedField", "");
    expression.add("metadata", metadata);
    expressions.add(expression);

    workflowData.add("templateExpressions", expressions);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("sort[0][field]", "createdAt");
    requestSpecification.queryParam("sort[0][direction]", "DESC");
    requestSpecification.pathParam("workflowId", workflowId);
    requestSpecification.body(workflowData.toString());
    Response post = genericRequestBuilder.putCall(requestSpecification, WorkflowConstants.WORKFLOW_TEMPLATISE_URI);
    return post.jsonPath();
  }

  public JsonPath getWorkflowList(String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("sort[0][field]", "name");
    requestSpecification.queryParam("sort[0][direction]", "ASC");
    Response response = genericRequestBuilder.getCall(requestSpecification, WorkflowConstants.GET_WORKFLOW_LIST);
    return response.jsonPath();
  }

  public JsonPath getWorkflowDetails(String appId, String workflowId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("workflowId", workflowId);
    Response response = genericRequestBuilder.getCall(requestSpecification, WorkflowConstants.GET_WORKFLOW_DETAILS_URI);
    return response.jsonPath();
  }

  public String getWorkflowIdFromName(String appId, String workflowName) {
    String workflowId = null;
    JsonPath workflowList = getWorkflowList(appId);
    for (int wf = 0, size = workflowList.getList("resource.response").size(); wf < size; wf++) {
      if (workflowList.getString("resource.response[" + wf + "].name").equalsIgnoreCase(workflowName)) {
        workflowId = workflowList.getString("resource.response[" + wf + "].name");
      }
    }
    return workflowId;
  }

  public JsonPath getWorkflowVariables(String appId, String workflowId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("appId", appId);
    JsonObject workflowData = new JsonObject();
    workflowData.addProperty("workflowType", WorkflowTypes.ORCHESTRATION.toString());
    workflowData.addProperty("orchestrationId", workflowId);
    requestSpecification.body(workflowData.toString());
    Response response = genericRequestBuilder.postCall(requestSpecification, WorkflowConstants.WORKFLOW_VARIABLES);
    return response.jsonPath();
  }

  public JsonPath getInfraDefinitionOfTemplatisedWorkflow(
      String appId, String envId, String serviceId, String deploymentType) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("envId", envId);
    requestSpecification.queryParam("sort[0][field]", "createdAt");
    requestSpecification.queryParam("details", false);
    requestSpecification.queryParam("sort[0][direction]", "DESC");
    JsonArray serviceIds = new JsonArray();
    serviceIds.add(serviceId);
    JsonArray deploymentTypeFromMetaData = new JsonArray();
    deploymentTypeFromMetaData.add(deploymentType);
    JsonObject workflowData = new JsonObject();
    workflowData.add("deploymentTypeFromMetaData", deploymentTypeFromMetaData);
    workflowData.add("serviceIds", serviceIds);
    requestSpecification.body(workflowData.toString());
    Response response =
        genericRequestBuilder.postCall(requestSpecification, WorkflowConstants.WORKFLOW_INFRA_DEFINITIONS);
    return response.jsonPath();
  }

  public JsonPath getWorkflowId(String workflowName, String appId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("sort[0][field]", "name");
    requestSpecification.queryParam("sort[0][direction]", "ASC");
    requestSpecification.queryParam("search[0][field]", "keywords");
    requestSpecification.queryParam("search[0][op]", "CONTAINS");
    requestSpecification.queryParam("search[0][value]", workflowName);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("withTags", true);
    Response workflowIdResponse =
        genericRequestBuilder.getCall(requestSpecification, WorkflowConstants.GET_WORKFLOW_LIST);
    return workflowIdResponse.jsonPath();
  }

  public JsonPath createWorkflow(RequestSpecification requestSpecification, String workflowName,
      String workflowDescription, String appId, String envId, String serviceId, String infraDefinitionId,
      String workflowType) {
    try {
      JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_WORKFLOW_CREATION);
      workflowData.addProperty("name", workflowName);
      workflowData.addProperty("description", workflowDescription);
      workflowData.addProperty("envId", envId);
      workflowData.addProperty("serviceId", serviceId);
      workflowData.addProperty("infraDefinitionId", infraDefinitionId);
      workflowData.getAsJsonObject("orchestrationWorkflow").addProperty("orchestrationWorkflowType", workflowType);
      requestSpecification.queryParam("appId", appId);
      requestSpecification.queryParam("sort[0][field]", "createdAt");
      requestSpecification.queryParam("sort[0][direction]", "DESC");
      requestSpecification.body(workflowData.toString());
      Response workflowResponse =
          genericRequestBuilder.postCall(requestSpecification, WorkflowConstants.WORKFLOW_CREATION_URI);
      log.info(workflowResponse.getBody().asString());
      return workflowResponse.jsonPath();
    } catch (FileNotFoundException e) {
      log.info("Error" + e.getMessage());
    }
    return null;
  }

  public Response editWorkflowName(
      RequestSpecification requestSpecification, String workflowName, String newName, String appId) {
    JsonObject serviceData = new JsonObject();
    serviceData.addProperty("name", newName);
    requestSpecification.queryParam("routingId", defaultAccountId);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("sort[0][field]", "createdAt");
    requestSpecification.queryParam("sort[0][direction]", "DESC");
    String uuid = getWorkflowId(workflowName, appId).getString("resource.response[0].uuid");
    requestSpecification.pathParam("uuid", uuid);
    Response editResponse =
        genericRequestBuilder.putCall(requestSpecification, WorkflowConstants.WORKFLOW_TEMPLATISE_URI);
    return editResponse;
  }

  public JsonPath deleteWorkflow(RequestSpecification requestSpecification, String workflowName, String appId) {
    requestSpecification.queryParam("routingId", defaultAccountId);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("sort[0][field]", "createdAt");
    requestSpecification.queryParam("sort[0][direction]", "DESC");
    String uuid = getWorkflowId(workflowName, appId).getString("resource.response[0].uuid");
    requestSpecification.pathParam("workflowId", uuid);
    Response response =
        genericRequestBuilder.deleteCall(requestSpecification, WorkflowConstants.GET_WORKFLOW_DETAILS_URI);
    return response.jsonPath();
  }
}
