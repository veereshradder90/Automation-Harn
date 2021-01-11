package io.harness.rest.helper.workflows;

/* Created by sonypriyadarshini
on 11/08/20 */

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.workflows.workflowSteps.WorkflowStepNames;
import io.harness.rest.helper.workflows.workflowSteps.jenkinsStep.JenkinsStepConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;

public class WorkflowDeployHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  WorkflowsHelper workflowsHelper = new WorkflowsHelper();

  public JsonPath deployWorkflowWithoutTemplatisation(String appId, String workflowId) throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_WORKFLOW_DEPLOY);
    workflowData.addProperty("orchestrationId", workflowId);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.body(workflowData.toString());
    Response post = genericRequestBuilder.postCall(requestSpecification, WorkflowConstants.WORKFLOW_DEPLOY);
    return post.jsonPath();
  }

  public JsonPath deployWorkflowWithJenkinsTemplatised(String appId, String workflowId) throws FileNotFoundException {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_WORKFLOW_DEPLOY);
    workflowData.addProperty("orchestrationId", workflowId);
    JsonObject workflowvariableObject = new JsonObject();

    // call workflowvariable api to get the number of variables required for deployment
    JsonPath workflowVariables = workflowsHelper.getWorkflowVariables(appId, workflowId);
    for (int i = 0; i < workflowVariables.getList("resource.workflowVariables").size(); i++) {
      if (!workflowVariables.getJsonObject("resource.workflowVariables[" + i + "].metadata").equals(null)) {
        if (workflowVariables.getString("resource.workflowVariables[" + i + "].metadata.stateType")
                .equalsIgnoreCase(WorkflowStepNames.JENKINS.toString())) {
          workflowvariableObject.addProperty(workflowVariables.getString("resource.workflowVariables[" + i + "].name"),
              JenkinsStepConstants.JENKINS_CONNECTOR_ID);
        }
      }
    }
    workflowData.add("workflowVariables", workflowvariableObject);
    requestSpecification.queryParam("appId", appId);
    requestSpecification.body(workflowData.toString());
    Response post = genericRequestBuilder.postCall(requestSpecification, WorkflowConstants.WORKFLOW_DEPLOY);
    return post.jsonPath();
  }

  public JsonPath deployWorkflowWithoutTemplatisation(
      RequestSpecification requestSpecification, String appId, String workflowId) {
    try {
      JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_WORKFLOW_DEPLOY);
      workflowData.addProperty("orchestrationId", workflowId);
      requestSpecification.queryParam("appId", appId);
      requestSpecification.body(workflowData.toString());
      Response post = genericRequestBuilder.postCall(requestSpecification, WorkflowConstants.WORKFLOW_DEPLOY);
      return post.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }
}
