package io.harness.rest.helper.workflows.workflowSteps;/* Created by sonypriyadarshini 
on 11/08/20 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.workflows.WorkflowConstants;
import io.harness.rest.helper.workflows.WorkflowsHelper;
import io.harness.rest.helper.workflows.workflowSteps.jenkinsStep.WorkflowStepEnum;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class WorkflowStepHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
    WorkflowsHelper workflowsHelper=new WorkflowsHelper();

    public HashMap<String,String> getWorkflowStepDetails(String appId, String workflowId){
        HashMap<String,String> map=new HashMap<>();
        //get WF details for which steps need to added
        JsonPath workflowDetails = workflowsHelper.getWorkflowDetails(appId,workflowId);
        if(workflowDetails.getString("resource.orchestrationWorkflow.workflowPhases[0].name").equalsIgnoreCase("Phase 1")){
            map.put(WorkflowStepEnum.UUID.toString(),workflowDetails.getString("resource.orchestrationWorkflow.workflowPhases[0].uuid"));
            map.put(WorkflowStepEnum.PREPARE_STEPS_ID.toString(),workflowDetails.getString("resource.orchestrationWorkflow.workflowPhases[0].phaseSteps[0].uuid"));
            map.put(WorkflowStepEnum.COLLECT_ARTIFACTS_STEP_ID.toString(),workflowDetails.getString("resource.orchestrationWorkflow.workflowPhases[0].phaseSteps[1].uuid"));
            map.put(WorkflowStepEnum.WRAP_UP_STEP_ID.toString(),workflowDetails.getString("resource.orchestrationWorkflow.workflowPhases[0].phaseSteps[2].uuid"));
        }
        return map;
    }

    public JsonPath removeAllStepsInBuildWorkflow(String appId, String workflowId) throws FileNotFoundException {
        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
        //get WF details for which steps need to added
        HashMap<String,String> map=getWorkflowStepDetails(appId,workflowId);

        //form request to add step:
        JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_ADD_BUILD_WORKFLOW_STEP);
        workflowData.addProperty("uuid",map.get(WorkflowStepEnum.UUID.toString()));

        //update uuids in phaseSteps array for all steps
        JsonArray phaseSteps= workflowData.getAsJsonArray("phaseSteps");
        phaseSteps.get(0).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.PREPARE_STEPS_ID.toString()));
        phaseSteps.get(1).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.COLLECT_ARTIFACTS_STEP_ID.toString()));
        phaseSteps.get(2).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.WRAP_UP_STEP_ID.toString()));
        requestSpecification.body(workflowData.toString());
        requestSpecification.pathParam("workflowId",workflowId);
        requestSpecification.pathParam("phaseId",map.get(WorkflowStepEnum.UUID.toString()));
        requestSpecification.queryParams("appId",appId);
        Response post = genericRequestBuilder.putCall(requestSpecification, WorkflowConstants.WORKFLOW_UPDATE_URI);
        return post.jsonPath();
    }

    public RequestSpecification addWorkflowStepToBuildWf(String appId, String workflowId, String step) throws FileNotFoundException, ParseException {

        JsonObject stepobject= JsonParser.parseString(step).getAsJsonObject();

        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
        //get WF details for which steps are to be added
        HashMap<String,String> map=getWorkflowStepDetails(appId,workflowId);

        //form request to add step:
        JsonObject workflowData = jReader.readJSONFiles(WorkflowConstants.REQUEST_JSON_ADD_BUILD_WORKFLOW_STEP);
        workflowData.addProperty("uuid",map.get(WorkflowStepEnum.UUID.toString()));

        //create phaseSteps array
        JsonArray phaseSteps= workflowData.getAsJsonArray("phaseSteps");

        //form object for step 1- prepare_steps:
        phaseSteps.get(0).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.PREPARE_STEPS_ID.toString()));
        phaseSteps.get(0).getAsJsonObject().getAsJsonArray("steps").add(stepobject);

        //form steps 2 and 3: COLLECT_ARTIFACT and WRAP_UP
        phaseSteps.get(1).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.COLLECT_ARTIFACTS_STEP_ID.toString()));
        phaseSteps.get(2).getAsJsonObject().addProperty("uuid",map.get(WorkflowStepEnum.WRAP_UP_STEP_ID.toString()));

        requestSpecification.body(workflowData.toString());
        requestSpecification.pathParam("workflowId",workflowId);
        requestSpecification.pathParam("phaseId",map.get(WorkflowStepEnum.UUID.toString()));
        requestSpecification.queryParams("appId",appId);
        return requestSpecification;
    }
}
