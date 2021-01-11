package io.harness.rest.helper.deployments;

/* Created by juhiagrawal
on 20/08/20 */

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.workflows.WorkflowConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;

public class deploymentMetadata extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();


    public JsonPath deploymentMetadataWithDefaultArtifact(String appId, boolean withDefaultArtifact, boolean withLastDeployedInfo, String workflowType, String pipelineId) throws FileNotFoundException {
        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
        JsonObject deploymentMetadata = jReader.readJSONFiles(deploymentConstants.DEPLOYMENT_METADATA);
        deploymentMetadata.addProperty("workflowType", workflowType);
        deploymentMetadata.addProperty("pipelineId", pipelineId);
        requestSpecification.queryParam("routingId", defaultAccountId);
        requestSpecification.queryParam("appId", appId);
        requestSpecification.queryParam("withDefaultArtifact", withDefaultArtifact);
        requestSpecification.queryParam("withLastDeployedInfo", withLastDeployedInfo);
        requestSpecification.body(deploymentMetadata.toString());
        Response post = genericRequestBuilder.postCall(requestSpecification, deploymentConstants.DEPLOYMENT_METADATA_URI);
        return post.jsonPath();
    }


}
