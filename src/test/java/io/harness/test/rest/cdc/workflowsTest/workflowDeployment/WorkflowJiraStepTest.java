package io.harness.test.rest.cdc.workflowsTest.workflowDeployment;

import io.harness.rest.helper.jira.JiraHelper;
import io.harness.rest.helper.setUpHelper.ApplicationAndWorkflow;
import io.harness.rest.helper.workflows.WorkflowDeployHelper;
import io.harness.rest.helper.workflows.workflowSteps.WorkflowStepNames;
import io.harness.rest.helper.workflows.workflowSteps.jiraStep.JiraEnum;
import io.harness.rest.helper.workflows.workflowSteps.jiraStep.JiraStepHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

public class WorkflowJiraStepTest extends AbstractTest {
    WorkflowDeployHelper workflowDeployHelper=new WorkflowDeployHelper();
    JiraStepHelper jiraStepHelper=new JiraStepHelper();
    JiraHelper jiraHelper=new JiraHelper();
    ApplicationAndWorkflow applicationAndWorkflow=new ApplicationAndWorkflow();

    @Test(groups = {"CDC_ONLY"})
    public void jiraStepInWorkflowTest() throws FileNotFoundException, ParseException {

        //create app and build WF
        JsonPath workflowResponse=applicationAndWorkflow.createApplicationAndWorkflow();
        String appId= workflowResponse.getString("resource.appId");
        String workflowId= workflowResponse.getString("resource.uuid");

        //get a valid jira connectorid
        String jiraConnectorId=jiraHelper.getValidJiraConnector(appId);
//        String jiraConnectorId=jiraHelper.getJiraConnectorIdByName(appId,"Jagannath_JiraTest");

        //add jenkinsStep with templatisation:
        JsonPath jiraStepResponse= jiraStepHelper.addJiraStep(appId,workflowId, JiraEnum.CREATE_TICKET.toString(),
                jiraHelper.getJiraTestProjectKeyByName(appId,jiraConnectorId,"Test"),JiraEnum.Bug.toString(),jiraConnectorId);
        Assert.assertTrue(jiraStepResponse.getString("resource.phaseSteps[0].steps[0].type").equals(WorkflowStepNames.JIRA_CREATE_UPDATE.toString()));

        //deploy the WF
        JsonPath workflowDeploymentData= workflowDeployHelper.deployWorkflowWithoutTemplatisation(
                appId,workflowId);
        Assert.assertTrue(workflowDeploymentData.getString("resource.workflowId").equals(workflowId));
        Assert.assertTrue(workflowDeploymentData.getString("resource.appId").equals(appId));
    }
}
