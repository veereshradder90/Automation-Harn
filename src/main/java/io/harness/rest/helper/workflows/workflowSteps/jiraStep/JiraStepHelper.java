package io.harness.rest.helper.workflows.workflowSteps.jiraStep;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.workflows.WorkflowConstants;
import io.harness.rest.helper.workflows.workflowSteps.WorkflowStepHelper;
import io.harness.rest.utils.CommonUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class JiraStepHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
    WorkflowStepHelper workflowStepHelper=new WorkflowStepHelper();
    CommonHelper commonHelper=new CommonHelper();
    CommonUtils commonUtils=new CommonUtils();

    public JsonPath addJiraStep(String appId, String workflowId, String jiraAction,String project,String issueType, String jiraConnectorId) throws FileNotFoundException, ParseException {
        RequestSpecification requestSpecification ;
        //get WF details for which steps are to be added
        HashMap<String,String> requestMap=new HashMap<>();
        requestMap.put("name",commonHelper.createRandomName("CDCAuto"));
        requestMap.put("project",project);
        requestMap.put("issueType",issueType);
        requestMap.put("jiraAction",jiraAction);
        requestMap.put("jiraConnectorId",jiraConnectorId);
        requestMap.put("issueId",null);
        if(jiraAction.equals(JiraEnum.UPDATE_TICKET.toString())){
            requestMap.put("issueId","");
        }

        //form object for step 1- prepare_steps:
        JsonObject jiraStep=jReader.readJSONFiles(JiraStepConstants.REQUEST_JSON_WORKFLOW_JIRA_STEP);
        String jiraStepString=commonUtils.createRequestBody(jiraStep,requestMap);
        requestSpecification=workflowStepHelper.addWorkflowStepToBuildWf(appId,workflowId,jiraStepString);
        Response post = genericRequestBuilder.putCall(requestSpecification, WorkflowConstants.WORKFLOW_UPDATE_URI);
        return post.jsonPath();
    }
}
