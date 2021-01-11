package io.harness.test.rest.cdc.workflowsTest.workflowTemplatisationTest.templatisation;

import io.harness.rest.helper.workflows.WorkflowDeployHelper;
import io.harness.rest.helper.workflows.workflowSteps.WorkflowStepNames;
import io.harness.rest.helper.workflows.workflowSteps.jenkinsStep.JenkinStepHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

public class JenkinsTemplatizationTest extends AbstractTest {
    JenkinStepHelper jenkinStepHelper=new JenkinStepHelper();
    WorkflowDeployHelper workflowDeployHelper=new WorkflowDeployHelper();

    @Test(groups = {"CDC_ONLY"})
    public void jenkinsTemplatisationTest() throws FileNotFoundException {

        //create app and build WF
        String appName = commonHelper.createRandomName("CDC-Automation-");
        String wfName = commonHelper.createRandomName("CDC-AutoWF-");
        JsonPath appResponse = jenkinStepHelper.setUpForJenkinsStep(appName,wfName);
        String appId= appResponse.getString("resource.appId");
        String workflowId= appResponse.getString("resource.uuid");

        //add jenkinsStep with templatisation:
        JsonPath jenkinsStepResponse= jenkinStepHelper.addJenkinsStep(appId,workflowId,true);
        Assert.assertTrue(jenkinsStepResponse.getString("resource.phaseSteps[0].steps[0].type").equals(WorkflowStepNames.JENKINS.toString()));

        //deploy the WF
        JsonPath workflowDeploymentData= workflowDeployHelper.deployWorkflowWithJenkinsTemplatised(
                appId,workflowId);
        Assert.assertTrue(workflowDeploymentData.getString("resource.workflowId").equals(workflowId));
        Assert.assertTrue(workflowDeploymentData.getString("resource.appId").equals(appId));
    }
}
