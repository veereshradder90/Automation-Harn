package io.harness.test.rest.cdc.applicationsTest;

import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest extends AbstractTest {
  ApplicationHelper applicationHelper = new ApplicationHelper();

  @Test(groups = {"CDC", "CDC_ONLY"})
  public void createApplicationTest() {
    String appName = commonHelper.createRandomName("CDC-Automation-");
    JsonPath appResponse = applicationHelper.createApplication(appName);
    Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));

    // assertion through AssertJ:
    assertThat(appResponse.getString("resource.name")).isEqualTo(appName);
  }

  @Test(groups = {"CDC", "CDC_ONLY"})
  public void getApplicationIdTest() {
    // create and getId
    String appName = commonHelper.createRandomName("CDC-Automation-");
    applicationHelper.createApplication(appName);
    JsonPath appResponse = applicationHelper.getApplicationId(appName);

    // assertion through AssertJ:
    assertThat(appResponse.getString("resource.response[0].appId")).isNotEmpty();
  }

  @Test(groups = {"CDC", "CDC_ONLY"})
  public void editApplicationTest() {
    String appName = commonHelper.createRandomName("CDC-Automation-");

    // create app
    applicationHelper.createApplication(appName);

    // edit
    String newAppName = appName + "new";
    JsonPath editResponse = applicationHelper.editApplication(appName, newAppName);
    assertThat(editResponse.getString("resource.name")).isEqualTo(newAppName);
  }

  @Test(groups = {"CDC", "CDC_ONLY"})
  public void deleteApplicationTest() {
    String appName = commonHelper.createRandomName("CDC-Automation-");

    // create app
    applicationHelper.createApplication(appName);

    // delete app
    Response editResponse = applicationHelper.deleteApp(appName, "name");
    assertThat(editResponse.getStatusCode()).isEqualTo(200);
  }

}
