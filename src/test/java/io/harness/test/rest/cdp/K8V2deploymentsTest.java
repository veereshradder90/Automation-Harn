package io.harness.test.rest.cdp;

import static org.assertj.core.api.Assertions.assertThat;

import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.environments.EnvironmentsHelper;
import io.harness.rest.helper.services.ServiceHelper;
import io.harness.rest.helper.workflows.WorkflowsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

public class K8V2deploymentsTest extends AbstractTest {
  ApplicationHelper applicationHelper = new ApplicationHelper();
  ServiceHelper serviceHelper = new ServiceHelper();
  EnvironmentsHelper environmentsHelper = new EnvironmentsHelper();
  WorkflowsHelper workflowsHelper = new WorkflowsHelper();
  CommonHelper commonHelper = new CommonHelper();
  String appName = commonHelper.createRandomName("CDP-Automation-", 3);
  String serviceName = "sample k8sv2 service";
  String appId = "";
  String serviceId = "";
  String envId = "";
  String infradefId = "";
  String workflowId = "";

  // This test needs to be extended once we have the code to add artifacts and workflows
  @Test
  public void K8SV2RollingDeploymentTest() throws FileNotFoundException {
    JsonPath appResponse = applicationHelper.createApplication(appName);
    Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
    appId = appResponse.getString("resource.appId");
    JsonPath serviceResponse = serviceHelper.createK8SV2Service(serviceName, appId);
    serviceId = serviceResponse.getString("resource.uuid");
    JsonPath envResponse = environmentsHelper.createEnvironment("sample env", "sample env description", "PROD", appId);
    envId = envResponse.getString("resource.uuid");
    JsonPath infraDefResponse = environmentsHelper.createGCPK8SInfraDefinition("gcp k8s", appId, envId);
    infradefId = infraDefResponse.getString("resource.uuid");
    JsonPath workflowResponse =
        workflowsHelper.createWorkflow("rolling k8s wf", "rolling wf", appId, envId, serviceId, infradefId, "ROLLING");
    workflowId = workflowResponse.getString("resource.uuid");
    serviceHelper.deleteService(serviceId, appId);
    applicationHelper.searchAndDeleteApp("CDP-Automation");
  }
}
