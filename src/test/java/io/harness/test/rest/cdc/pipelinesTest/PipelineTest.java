package io.harness.test.rest.cdc.pipelinesTest;

/* Created by sonypriyadarshini
on 13/08/20 */

import io.harness.rest.helper.pipelines.PipelineHelper;
import io.harness.rest.helper.setUpHelper.ApplicationAndPipeline;
import io.harness.rest.helper.setUpHelper.ApplicationAndWorkflow;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

public class PipelineTest extends AbstractTest {
    ApplicationAndPipeline applicationAndPipeline=new ApplicationAndPipeline();
    PipelineHelper pipelineHelper=new PipelineHelper();
    ApplicationAndWorkflow applicationAndWorkflow=new ApplicationAndWorkflow();

    @Test(groups = {"CDC", "CDC_ONLY"})
    public void createPipelineTest() {
        String appName = commonHelper.createRandomName("CDC-Automation-");
        String pipelineName = commonHelper.createRandomName("CDC-Auto-Pipe");
        JsonPath pipelineResponse = applicationAndPipeline.createApplicationAndPipeline(appName,pipelineName);
        Assert.assertTrue(pipelineResponse.getString("resource.name").equalsIgnoreCase(pipelineName));
    }

    @Test(groups = {"CDC", "CDC_ONLY"})
    public void editPipelineTest() throws FileNotFoundException {
        String appName = commonHelper.createRandomName("CDC-Automation-");
        String pipelineName = commonHelper.createRandomName("CDC-Auto-Pipe");
        String editedName=pipelineName+"-edited";
        JsonPath pipelineResponse = applicationAndPipeline.createApplicationAndPipeline(appName,pipelineName);
        JsonPath editPipelineResponse = pipelineHelper.editPipeline(pipelineResponse.getString("resource.uuid"),
                pipelineResponse.getString("resource.appId"),editedName);
        Assert.assertTrue(editPipelineResponse.getString("resource.name").equalsIgnoreCase(editedName));
    }

    @Test(groups = {"CDC", "CDC_ONLY"})
    public void clonePipelineTest() {
        String appName = commonHelper.createRandomName("CDC-Automation-");
        String pipelineName = commonHelper.createRandomName("CDC-Auto-Pipe");
        String clonedPipelineName = pipelineName+"-cloned";
        JsonPath pipelineResponse = applicationAndPipeline.createApplicationAndPipeline(appName,pipelineName);
        JsonPath clonedPipelineResponse = pipelineHelper.clonePipeline(pipelineResponse.getString("resource.uuid"),
                pipelineResponse.getString("resource.appId"),clonedPipelineName);
        Assert.assertTrue(clonedPipelineResponse.getString("resource.name").equalsIgnoreCase(clonedPipelineName));
    }

    @Test(groups = {"CDC", "CDC_ONLY"})
    public void deletePipelineTest() {
        String appName = commonHelper.createRandomName("CDC-Automation-");
        String pipelineName = commonHelper.createRandomName("CDC-Auto-Pipe");
        JsonPath pipelineResponse = applicationAndPipeline.createApplicationAndPipeline(appName,pipelineName);
        JsonPath deletePipelineResponse = pipelineHelper.deletePipeline(pipelineResponse.getString("resource.uuid"),
                pipelineResponse.getString("resource.appId"));
        Assert.assertTrue(deletePipelineResponse.getString("responseMessages").equalsIgnoreCase("[]"));
        Assert.assertTrue(deletePipelineResponse.get("resource")==null);
    }

    @Test(groups = {"CDC", "CDC_ONLY"})
    public void addWfStageToPipelineTest() throws FileNotFoundException {
        String pipelineName = commonHelper.createRandomName("CDC-Auto-Pipe");
        JsonPath workflowResponse=applicationAndWorkflow.createApplicationAndWorkflow();
        JsonPath pipelineResponse=pipelineHelper.createPipeline(pipelineName,workflowResponse.getString("resource.appId"));
        JsonPath editPipelineResponse = pipelineHelper.addWfStageToPipeline(pipelineResponse.getString("resource.uuid"),
                workflowResponse.getString("resource.appId"),workflowResponse.getString("resource.uuid"));
        Assert.assertTrue(editPipelineResponse.getString("resource.name").equalsIgnoreCase(pipelineName));
    }

    @Test(groups = {"CDC", "CDC_ONLY"})
    public void addApprovalStageToPipelineTest() throws FileNotFoundException {
        JsonPath pipelineResponse=applicationAndPipeline.createApplicationAndPipeline();
        String appId= pipelineResponse.getString("resource.appId");
        String pipelineId= pipelineResponse.getString("resource.uuid");
        JsonPath editPipelineResponse = pipelineHelper.addHarnessApprovalStageToPipeline(pipelineId, appId);
        Assert.assertTrue(editPipelineResponse.getString("resource.uuid").equalsIgnoreCase(pipelineId));
        Assert.assertTrue(editPipelineResponse.getString("resource.appId").equalsIgnoreCase(appId));
    }
}
