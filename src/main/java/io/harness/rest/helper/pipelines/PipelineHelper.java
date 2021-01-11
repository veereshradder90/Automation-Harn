package io.harness.rest.helper.pipelines;

/* Created by sonypriyadarshini
on 13/08/20 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.userGroupsApprovals.UserGroupApprovalHelper;
import io.harness.rest.helper.workflows.WorkflowConstants;
import io.harness.rest.utils.CommonUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class PipelineHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  CommonUtils commonUtils = new CommonUtils();
  UserGroupApprovalHelper userGroupApprovalHelper = new UserGroupApprovalHelper();

  public JsonPath createPipeline(String pipelineName, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject pipelineObject = new JsonObject();
    pipelineObject.addProperty("name", pipelineName);
    pipelineObject.addProperty("description", pipelineName + " created by restassured automation");
    requestSpecification.queryParams("appId", appId);
    requestSpecification.body(pipelineObject.toString());
    Response pipeline = genericRequestBuilder.postCall(requestSpecification, PipelineConstants.PIPELINE_CREATION_URI);
    return pipeline.jsonPath();
  }

  public JsonPath getPipelineInfo(String pipelineId, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParams("search[0][value]", pipelineId);
    requestSpecification.queryParams("appId", appId);
    Response pipeline = genericRequestBuilder.getCall(requestSpecification, PipelineConstants.PIPELINE_CREATION_URI);
    return pipeline.jsonPath();
  }

  public JsonPath editPipeline(String pipelineId, String appId, String editedPipelineName)
      throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject pipelineData = jReader.readJSONFiles(PipelineConstants.REQUEST_JSON_PIPELINE_EDIT);
    pipelineData.addProperty("name", editedPipelineName);
    pipelineData.addProperty("uuid", pipelineId);
    pipelineData.add("pipelineStages", new JsonArray());
    requestSpecification.queryParams("appId", appId);
    requestSpecification.pathParam("pipelineId", pipelineId);
    requestSpecification.body(pipelineData.toString());
    Response pipeline = genericRequestBuilder.putCall(requestSpecification, PipelineConstants.PIPELINE_EDIT_URI);
    return pipeline.jsonPath();
  }

  public JsonPath addWfStageToPipeline(String pipelineId, String appId, String workflowId)
      throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject pipelineData = jReader.readJSONFiles(PipelineConstants.REQUEST_JSON_PIPELINE_EDIT);
    JsonPath pipeLineInfo = getPipelineInfo(pipelineId, appId);
    HashMap<String, String> requestMap = new HashMap<>();
    requestMap.put("name", pipeLineInfo.getString("resource.response[0].name"));
    requestMap.put("uuid", pipelineId);
    requestMap.put("workflowId", workflowId);
    String requestbody = commonUtils.createRequestBody(pipelineData, requestMap);
    requestSpecification.queryParams("appId", appId);
    requestSpecification.pathParam("pipelineId", pipelineId);
    requestSpecification.body(requestbody);
    Response pipeline = genericRequestBuilder.putCall(requestSpecification, PipelineConstants.PIPELINE_EDIT_URI);
    return pipeline.jsonPath();
  }

  public JsonPath addHarnessApprovalStageToPipeline(String pipelineId, String appId) throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject pipelineData = jReader.readJSONFiles(PipelineConstants.REQUEST_JSON_PIPELINE_HARNESS_APPROVAL_STAGE);
    JsonPath pipeLineInfo = getPipelineInfo(pipelineId, appId);
    HashMap<String, String> requestMap = new HashMap<>();
    requestMap.put("name", pipeLineInfo.getString("resource.response[0].name"));
    requestMap.put("uuid", pipelineId);
    requestMap.put("userGroups", userGroupApprovalHelper.getUserGroupIdByName("Account Administrator"));
    String requestbody = commonUtils.createRequestBody(pipelineData, requestMap);
    requestSpecification.queryParams("appId", appId);
    requestSpecification.pathParam("pipelineId", pipelineId);
    requestSpecification.body(requestbody);
    Response pipeline = genericRequestBuilder.putCall(requestSpecification, PipelineConstants.PIPELINE_EDIT_URI);
    return pipeline.jsonPath();
  }

  public JsonPath clonePipeline(String pipelineId, String appId, String clonePipelineName) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject pipelineObject = new JsonObject();
    pipelineObject.addProperty("name", clonePipelineName);
    pipelineObject.addProperty("description", clonePipelineName + " created by restassured automation");
    requestSpecification.queryParams("appId", appId);
    requestSpecification.pathParam("pipelineId", pipelineId);
    requestSpecification.body(pipelineObject.toString());
    Response pipeline = genericRequestBuilder.postCall(requestSpecification, PipelineConstants.PIPELINE_CLONE_URI);
    return pipeline.jsonPath();
  }

  public JsonPath deletePipeline(String pipelineId, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParams("appId", appId);
    requestSpecification.pathParam("pipelineId", pipelineId);
    Response pipeline = genericRequestBuilder.deleteCall(requestSpecification, PipelineConstants.PIPELINE_EDIT_URI);
    return pipeline.jsonPath();
  }

  public JsonPath createPipeline(RequestSpecification requestSpecification, String pipelineName, String appId) {
    JsonObject pipelineObject = new JsonObject();
    pipelineObject.addProperty("name", pipelineName);
    pipelineObject.addProperty("description", "Automation");
    requestSpecification.queryParams("appId", appId);
    requestSpecification.body(pipelineObject.toString());
    Response pipeline = genericRequestBuilder.postCall(requestSpecification, PipelineConstants.PIPELINE_CREATION_URI);
    return pipeline.jsonPath();
  }

  public JsonPath getPipelineId(String pipelineName, String appId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("sort[0][field]", "name");
    requestSpecification.queryParam("sort[0][direction]", "ASC");
    requestSpecification.queryParam("search[0][field]", "keywords");
    requestSpecification.queryParam("search[0][op]", "CONTAINS");
    requestSpecification.queryParam("search[0][value]", pipelineName);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.queryParam("accountId", defaultAccountId);
    Response pipelineIdResponse =
        genericRequestBuilder.getCall(requestSpecification, PipelineConstants.PIPELINE_CREATION_URI);
    return pipelineIdResponse.jsonPath();
  }

  public Response editPipelineName(
      RequestSpecification requestSpecification, String pipelineName, String newName, String appId) {
    JsonObject workflowData = new JsonObject();
    workflowData.addProperty("name", newName);
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("appId", appId);
    String uuid = getPipelineId(pipelineName, appId).getString("resource.response[0].uuid");
    requestSpecification.pathParam("uuid", uuid);
    Response editPipelineResponse =
        genericRequestBuilder.putCall(requestSpecification, PipelineConstants.PIPELINE_EDIT_URI);
    return editPipelineResponse;
  }

  public JsonPath deletePipeline(RequestSpecification requestSpecification, String pipelineName, String appId) {
    String uuid = getPipelineId(pipelineName, appId).getString("resource.response[0].uuid");
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("pipelineId", uuid);
    Response response = genericRequestBuilder.deleteCall(requestSpecification, PipelineConstants.PIPELINE_EDIT_URI);
    return response.jsonPath();
  }
}