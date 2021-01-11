package io.harness.rest.helper.setUpHelper;

/* Created by sonypriyadarshini
on 11/08/20 */

import io.harness.rest.core.CoreUtils;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.workflows.WorkflowTypes;
import io.harness.rest.helper.workflows.WorkflowsHelper;
import io.harness.rest.helper.workflows.workflowSteps.WorkflowStepHelper;
import io.restassured.path.json.JsonPath;


import java.io.FileNotFoundException;

public class ApplicationAndWorkflow extends CoreUtils {
    WorkflowsHelper workflowsHelper=new WorkflowsHelper();
    ApplicationHelper applicationHelper=new ApplicationHelper();
    CommonHelper commonHelper=new CommonHelper();
    WorkflowStepHelper workflowStepHelper=new WorkflowStepHelper();

    public JsonPath createApplicationAndWorkflow(String appName, String workflowName) throws FileNotFoundException {
        //create app, Build WF, remove install step from build WF
        JsonPath appResponse = applicationHelper.createApplication(appName);
        JsonPath workflowResponse=workflowsHelper.createBuildWorkflow(appResponse.getString("resource.appId"),
                WorkflowTypes.BUILD.toString(),workflowName);
        return  workflowResponse;
    }

    public JsonPath createApplicationAndWorkflow() throws FileNotFoundException {
        //create app, Build WF, remove all steps from build WF
        JsonPath appResponse = applicationHelper.createApplication(commonHelper.createRandomName("CDC-Automation-"));
        JsonPath workflowResponse=workflowsHelper.createBuildWorkflow(appResponse.getString("resource.appId"),
                WorkflowTypes.BUILD.toString(),commonHelper.createRandomName("CDC-AutoWF-"));
        String appId= workflowResponse.getString("resource.appId");
        String workflowId= workflowResponse.getString("resource.uuid");
        workflowStepHelper.removeAllStepsInBuildWorkflow(appId,workflowId);
        return  workflowResponse;
    }

}
