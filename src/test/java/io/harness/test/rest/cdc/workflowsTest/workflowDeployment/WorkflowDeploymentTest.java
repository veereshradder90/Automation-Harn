package io.harness.test.rest.cdc.workflowsTest.workflowDeployment;

/* Created by sonypriyadarshini
on 11/08/20 */

import io.harness.rest.helper.setUpHelper.ApplicationAndWorkflow;
import io.harness.rest.helper.workflows.WorkflowDeployHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

public class WorkflowDeploymentTest extends AbstractTest {

    WorkflowDeployHelper workflowDeployHelper=new WorkflowDeployHelper();
    ApplicationAndWorkflow applicationAndWorkflow=new ApplicationAndWorkflow();

    @Test(groups = {"CDC_ONLY"})
    public void workflowDeployWithoutTemplatisationTest() throws FileNotFoundException {
        //create app and build WF
        JsonPath workflowResponse=applicationAndWorkflow.createApplicationAndWorkflow();
        String appId= workflowResponse.getString("resource.appId");
        String workflowId= workflowResponse.getString("resource.uuid");

        //deploy workflow
        JsonPath workflowDeploymentData= workflowDeployHelper.deployWorkflowWithoutTemplatisation(
                appId,workflowId);

        Assert.assertTrue(workflowDeploymentData.getString("resource.workflowId").equals(workflowId));
        Assert.assertTrue(workflowDeploymentData.getString("resource.appId").equals(appId));
    }
}
