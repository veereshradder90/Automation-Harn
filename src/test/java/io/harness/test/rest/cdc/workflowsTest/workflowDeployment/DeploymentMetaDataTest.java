package io.harness.test.rest.cdc.workflowsTest.workflowDeployment;

/* Created by sonypriyadarshini
on 11/08/20 */

import io.harness.rest.helper.deployments.deploymentMetadata;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

public class DeploymentMetaDataTest extends AbstractTest {

    deploymentMetadata deploymentMetadata=new deploymentMetadata();
    String appId = "UbPE0fDeTKO-gpdfqZJ2_Q";
    boolean withDefaultArtifact = true;
    boolean withLastDeployedInfo = true;
    String workflowType = "PIPELINE";
    String pipelineId = "IMqDyxl4Tu29_YGOGHq-1g";

    @Test(groups = {"CDC_ONLY"})
    public void deploymentMetadataWithLastDeployedInfoTrue() throws FileNotFoundException {
        //deployment Metadata With Default Artifact True
        JsonPath deploymentMetadataResponse=deploymentMetadata.deploymentMetadataWithDefaultArtifact( appId,  withDefaultArtifact,  withLastDeployedInfo,  workflowType,  pipelineId);
        Assert.assertTrue(!deploymentMetadataResponse.getString("resource.artifactVariables[0].lastDeployedArtifactInfo.executionId").isEmpty());
        Assert.assertTrue(!deploymentMetadataResponse.getString("resource.artifactVariables[0].lastDeployedArtifactInfo.executionEntityType").isEmpty());
        Assert.assertTrue(!deploymentMetadataResponse.getString("resource.artifactVariables[0].lastDeployedArtifactInfo.executionEntityName").isEmpty());

    }
    @Test(groups = {"CDC_ONLY"})
    public void deploymentMetadataWithLastDeployedInfoFalse() throws FileNotFoundException {
        //deployment Metadata With Default Artifact False
        withLastDeployedInfo = false;
        JsonPath deploymentMetadataResponse=deploymentMetadata.deploymentMetadataWithDefaultArtifact( appId,  withDefaultArtifact,  withLastDeployedInfo,  workflowType,  pipelineId);
        Assert.assertTrue(deploymentMetadataResponse.get("resource.artifactVariables[0].lastDeployedArtifactInfo") ==null);
    }
}
