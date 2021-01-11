package io.harness.rest.helper.workflows.workflowSteps.jenkinsStep;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.workflows.WorkflowConstants;
import io.harness.rest.helper.workflows.WorkflowTypes;
import io.harness.rest.helper.workflows.WorkflowsHelper;
import io.harness.rest.helper.workflows.workflowSteps.WorkflowStepHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

public class JenkinStepHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
    ApplicationHelper applicationHelper=new ApplicationHelper();
    WorkflowsHelper workflowsHelper=new WorkflowsHelper();
    WorkflowStepHelper workflowStepHelper=new WorkflowStepHelper();

    public JsonPath setUpForJenkinsStep(String appName,String workflowName) throws FileNotFoundException {
        //create app and a Build WF
        JsonPath appResponse = applicationHelper.createApplication(appName);
        JsonPath workflowResponse=workflowsHelper.createBuildWorkflow(appResponse.getString("resource.appId"), WorkflowTypes.BUILD.toString(),workflowName);
        return  workflowResponse;
    }

    public JsonPath addJenkinsStep(String appId, String workflowId,boolean isJenkinsServerTemplatised) throws FileNotFoundException {
        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
        //get WF details for which steps need to added
        HashMap<String,String> map=workflowStepHelper.getWorkflowStepDetails(appId,workflowId);

        //form request to add step:
        JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_ADD_BUILD_WORKFLOW_STEP);
        workflowData.addProperty("uuid",map.get(WorkflowStepEnum.UUID.toString()));

        //create phaseSteps array
        JsonArray phaseSteps= workflowData.getAsJsonArray("phaseSteps");

        //form object for step 1- prepare_steps:
        phaseSteps.get(0).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.PREPARE_STEPS_ID.toString()));
        JsonObject prepStep=jReader.readJSONFiles(JenkinsStepConstants.REQUEST_JSON_WORKFLOW_JENKINS_STEP);
        prepStep.getAsJsonObject("properties").addProperty("parentId",map.get(WorkflowStepEnum.PREPARE_STEPS_ID.toString()));
        phaseSteps.get(0).getAsJsonObject().getAsJsonArray("steps").add(prepStep);
        if(isJenkinsServerTemplatised==false){
            prepStep.getAsJsonObject("properties").add("templateExpressions",null);
        }

        //form steps 2 and 3: COLLECT_ARTIFACT and WRAP_UP
        phaseSteps.get(1).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.COLLECT_ARTIFACTS_STEP_ID.toString()));
        phaseSteps.get(2).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.WRAP_UP_STEP_ID.toString()));
        requestSpecification.body(workflowData.toString());
        requestSpecification.pathParam("workflowId",workflowId);
        requestSpecification.pathParam("phaseId",map.get(WorkflowStepEnum.UUID.toString()));
        requestSpecification.queryParams("appId",appId);
        Response post = genericRequestBuilder.putCall(requestSpecification, WorkflowConstants.WORKFLOW_UPDATE_URI);
        return post.jsonPath();
    }

}
