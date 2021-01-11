package io.harness.test.rest.cvnextgen.onboarding.appdynamics;

import io.harness.rest.common.ERROR_MESSAGES;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.CVNGAppdynamicsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * author: shaswat.deep
 */
@Slf4j
@Test(groups = {"CVNG", "Appdynamics", "LOCAL"}, enabled = true)
public class CVNGAppdAppsTest extends AbstractTest {
  CVNGAppdynamicsHelper appdynamicsHelper = new CVNGAppdynamicsHelper();

  // ToDO [Shaswat]: Remove the connectorID once the connector function is available
  String connectorId = "YOgB1-WjSKyZWoCkuY7kBA";

  @Test(description = "Verify the list of Apps from the Appd Mock Server")
  public void getAppListFromAppdTest() {
    Response response = appdynamicsHelper.getAppdAppList(connectorId);

    Assert.assertEquals(response.statusCode(), 200);
    JsonPath appList = response.jsonPath();

    // Validate the size of App List
    Assert.assertEquals(appList.getList("resource").size(), 2);

    // Validate the name of App and ID
    Assert.assertEquals(appList.getString("resource[0].name"), "mock_server_app_0");
    Assert.assertEquals(appList.getInt("resource[0].id"), 1);

    Assert.assertEquals(appList.getString("resource[1].name"), "mock_server_app_1");
    Assert.assertEquals(appList.getInt("resource[1].id"), 2);
  }

  @Test(description = "Verify the error code and body when the Connector Id is invalid")
  public void getAppsWrongConnectorIDTest() {
    Response response = appdynamicsHelper.getAppdAppList("abcd");

    Assert.assertEquals(response.statusCode(), 500);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the connector ID is invalid");
  }

  // ToDO [Shaswat]: Remove the projectIdentifier with the method to get that
  @Test(description = "Verify the list of metric packs")
  public void getMetricPacksForAppd() {
    Response response = appdynamicsHelper.getMetricPacks("12345", "APP_DYNAMICS");

    Assert.assertEquals(response.statusCode(), 200);
    JsonPath metricList = response.jsonPath();

    // Validate the size of Metric List
    Assert.assertEquals(metricList.getList("resource").size(), 3);

    // Validation of fields for Metric Pack: Performance and Availability
    Assert.assertEquals(metricList.getString("resource[0].dataSourceType"), "APP_DYNAMICS");
    Assert.assertEquals(metricList.getString("resource[0].identifier"), "Performance");
    Assert.assertEquals(metricList.getList("resource[0].metrics").size(), 11);

    // Validation for Metrics for Metric Pack: Performance and Availability
    Assert.assertEquals(metricList.getString("resource[0].metrics[0].name"), "Stall Count");
    Assert.assertEquals(metricList.getList("resource[0].metrics[0].thresholds").size(), 0);

    Assert.assertEquals(metricList.getString("resource[0].metrics[1].name"), "Normal Average Response Time (ms)");
    Assert.assertEquals(metricList.getList("resource[0].metrics[1].thresholds").size(), 0);

    // Validation of fields for Metric Pack: Quality
    Assert.assertEquals(metricList.getString("resource[1].dataSourceType"), "APP_DYNAMICS");
    Assert.assertEquals(metricList.getString("resource[1].identifier"), "Quality");
    Assert.assertEquals(metricList.getList("resource[1].metrics").size(), 2);

    // Validation for Metrics for Metric Pack: Quality
    Assert.assertEquals(metricList.getString("resource[1].metrics[0].name"), "Number of Errors");
    Assert.assertEquals(metricList.getList("resource[1].metrics[0].thresholds").size(), 0);

    Assert.assertEquals(metricList.getString("resource[1].metrics[1].name"), "Errors per Minute");
    Assert.assertEquals(metricList.getList("resource[1].metrics[1].thresholds").size(), 0);
  }

  // ToDO [Shaswat]: Remove the projectIdentifier with the method to fetch that
  @Test(description = "Verify the error code and body when the Data Source is invalid")
  public void getMetricPacksForAppdInvalidDatasource() {
    Response response = appdynamicsHelper.getMetricPacks("12345", "ABC");

    Assert.assertEquals(response.statusCode(), 500);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the Data Source is invalid");
  }

  // ToDo [Shaswat]: This test will fail as the Project Identifier is bypassed
  @Test(description = "Verify the error code and body when the Project Identifier is invalid", enabled = false)
  public void getMetricPacksForAppdInvalidProjectId() {
    Response response = appdynamicsHelper.getMetricPacks("123", "APP_DYNAMICS");

    Assert.assertEquals(response.statusCode(), 500);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the Project Identifier is invalid");
  }

  // ToDO [Shaswat]: Remove the projectIdentifier with the method to get that
  @Test(description = "Verify the error code and body when the Data Source param is missing")
  public void getMetricPacksForAppdMissingDatasource() {
    Response response = appdynamicsHelper.getMetricPacksMissingDS("12345");

    Assert.assertEquals(response.statusCode(), 400);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(
        ERROR_MESSAGES.INVALID_ARGUMENT.toString(), actualErrorMessage, "Error thrown when the Data Source is missing");
  }

  // ToDO [Shaswat]: Write test once the Service Integration is available
  @Test(description = "Verify the Services available")
  public void getServicesForAppd() {}

  // ToDO [Shaswat]: Write test once the Service Integration is available
  @Test(description = "Verify the Services with Invalid AppId")
  public void getServicesForAppdInvalidAppId() {}

  @Test(description = "Verify the Tiers List with Valid parameters")
  public void getTiersForAppd() {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);

    int appId = appList.jsonPath().getInt("resource[0].id");
    Assert.assertEquals(appId, 1);

    Response response = appdynamicsHelper.getTiersList(connectorId, "" + appId);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath tiersList = response.jsonPath();

    // Validate the size of Tiers List
    Assert.assertEquals(tiersList.getList("resource").size(), 5);

    // Validate the details of tiers from the list
    Assert.assertEquals(tiersList.getInt("resource[0].id"), 5);
    Assert.assertEquals(tiersList.getString("resource[0].name"), "service_guard");
    Assert.assertEquals(tiersList.getString("resource[0].description"), "test description");

    Assert.assertEquals(tiersList.getInt("resource[1].id"), 1);
    Assert.assertEquals(tiersList.getString("resource[1].name"), "pass_scenario_canary");
  }

  @Test(description = "Verify the Tiers List with Invalid AppId")
  public void getTiersForAppdInvalidAppId() {
    Response response = appdynamicsHelper.getTiersList(connectorId, "20");
    Assert.assertEquals(response.statusCode(), 500);

    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(
        ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage, "Error thrown when the AppId is invalid");
  }

  @Test(description = "Verify the Tiers List with Invalid ConnectorId")
  public void getTiersForAppdInvalidConnectorId() {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);

    int appId = appList.jsonPath().getInt("resource[0].id");
    Assert.assertEquals(appId, 1);

    Response response = appdynamicsHelper.getTiersList("abcd", "" + appId);
    Assert.assertEquals(response.statusCode(), 500);

    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the ConnectorId is invalid");
  }

  @Test(description = "Verify the Tiers List with Missing ConnectorId")
  public void getTiersForAppdMissingConnectorId() {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);

    int appId = appList.jsonPath().getInt("resource[0].id");
    Assert.assertEquals(appId, 1);

    Response response = appdynamicsHelper.getTiersListMissingConnectorId("" + appId);
    Assert.assertEquals(response.statusCode(), 500);

    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the ConnectorId is missing");
  }

  @Test(description = "Verify the Tiers List with Missing AppId")
  public void getTiersForAppdMissingAppId() {
    Response response = appdynamicsHelper.getTiersListMissingAppId("" + connectorId);
    Assert.assertEquals(response.statusCode(), 500);

    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(
        ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage, "Error thrown when the AppId is missing");
  }
}
