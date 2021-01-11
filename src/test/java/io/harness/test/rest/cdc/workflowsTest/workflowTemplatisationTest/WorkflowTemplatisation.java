package io.harness.test.rest.cdc.workflowsTest.workflowTemplatisationTest;

import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.environments.EnvironmentsHelper;
import io.harness.rest.helper.services.ServiceHelper;
import io.harness.rest.helper.workflows.WorkflowsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

public class WorkflowTemplatisation extends AbstractTest {
    ApplicationHelper applicationHelper = new ApplicationHelper();
    ServiceHelper serviceHelper = new ServiceHelper();
    EnvironmentsHelper environmentsHelper = new EnvironmentsHelper();
    WorkflowsHelper workflowsHelper = new WorkflowsHelper();
    CommonHelper commonHelper = new CommonHelper();

    String serviceName = "sample SSH service";
    String appId;
    String serviceId;
    String envId;
    String infradefId;
    String workflowId;

    // This test covers infra-definition API call to fetch list of infra's associated with service & Environment
    @Test(groups = {"CDC_ONLY"})
    @SneakyThrows(FileNotFoundException.class)
    public void workflowTemplatiseInfraDefinitionTest() {
        String appName = commonHelper.createRandomName("CDC-Automation", 3);
        JsonPath appResponse = applicationHelper.createApplication(appName);
        assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        appId = appResponse.getString("resource.appId");
        JsonPath serviceResponse = serviceHelper.createSSHService(serviceName, appId);
        serviceId = serviceResponse.getString("resource.uuid");
        String deploymentType = serviceResponse.getString("resource.deploymentType");
        JsonPath envResponse = environmentsHelper.createEnvironment("sample env", "sample env description", "PROD", appId);
        envId = envResponse.getString("resource.uuid");
        JsonPath infraDefResponse = environmentsHelper.createAWSSSHInfraDefinition("awsinfra", appId, envId);
        infradefId = infraDefResponse.getString("resource.uuid");
        JsonPath workflowResponse =
                workflowsHelper.createWorkflow("Infradef Automation wf", "wf desc", appId, envId, serviceId, infradefId, "BASIC");
        workflowId = workflowResponse.getString("resource.uuid");
        workflowsHelper.templatiseWorkflow("Infradef Automation wf", "wf desc2", appId, workflowId, envId, serviceId, infradefId, "${infra_temp}");
        JsonPath infraDefinitionResponse = workflowsHelper.getInfraDefinitionOfTemplatisedWorkflow(appId, envId, serviceId, deploymentType);
        String actualInfraId = infraDefinitionResponse.getString("resource.response.uuid");

        assertTrue("created infra definition was not listed under infra dropdown of deployment window", actualInfraId.contains(infradefId));
    }

    /** This test covers infra-definition API call when infra is scoped to specific service.
     Test is to fetch list of infra's associated with service & Environment **/
    @Test(groups = {"CDC_ONLY"})
    @SneakyThrows(FileNotFoundException.class)
    public void workflowTemplatiseInfraDefinitionScopedToServiceTest() {
        String appName = commonHelper.createRandomName("CDC-Automation", 3);
        JsonPath appResponse = applicationHelper.createApplication(appName);
        assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        appId = appResponse.getString("resource.appId");
        JsonPath serviceResponse = serviceHelper.createSSHService(serviceName, appId);
        serviceId = serviceResponse.getString("resource.uuid");
        String deploymentType = serviceResponse.getString("resource.deploymentType");
        JsonPath envResponse = environmentsHelper.createEnvironment("sample env", "sample env description", "PROD", appId);
        envId = envResponse.getString("resource.uuid");
        JsonPath infraDefResponse = environmentsHelper.createAWSSSHInfraDefinition("awsinfra", appId, envId,serviceId);
        infradefId = infraDefResponse.getString("resource.uuid");
        JsonPath workflowResponse =
                workflowsHelper.createWorkflow("Infradef Automation wf", "wf desc", appId, envId, serviceId, infradefId, "BASIC");
        workflowId = workflowResponse.getString("resource.uuid");
        workflowsHelper.templatiseWorkflow("Infradef Automation wf", "wf desc2", appId, workflowId, envId, serviceId, infradefId, "${infra_temp}");
        JsonPath infraDefinitionResponse = workflowsHelper.getInfraDefinitionOfTemplatisedWorkflow(appId, envId, serviceId, deploymentType);
        String actualInfraId = infraDefinitionResponse.getString("resource.response.uuid");

        assertTrue("created infra definition was not listed under infra dropdown of deployment window", actualInfraId.contains(infradefId));
    }
}
