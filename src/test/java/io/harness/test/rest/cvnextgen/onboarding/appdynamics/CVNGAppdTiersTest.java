package io.harness.test.rest.cvnextgen.onboarding.appdynamics;

import io.harness.rest.common.ERROR_MESSAGES;
import io.harness.rest.helper.cvnextgen.CVNGHelper;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.CVNGAppDynamicsConstants;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.CVNGAppdynamicsHelper;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.CVNGMetricPackHelper;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.metricpackpojo.MetricPacks;
import io.harness.rest.utils.RestAssuredResponseParser;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: shaswat.deep
 */
@Slf4j
@Test(groups = {"CVNG", "Appdynamics", "LOCAL"}, enabled = true)
public class CVNGAppdTiersTest extends AbstractTest {
  CVNGMetricPackHelper appdynamicsMetricPackHelper = new CVNGMetricPackHelper();
  CVNGAppdynamicsHelper appdynamicsHelper = new CVNGAppdynamicsHelper();
  RestAssuredResponseParser restAssuredResponseParser = new RestAssuredResponseParser();
  CVNGHelper cvngHelper = new CVNGHelper();

  // ToDO [Shaswat]: Remove the connectorID once the connector function is available
  String connectorId = "YOgB1-WjSKyZWoCkuY7kBA"; //'ZZaOBFXpSlGciQB1V7W0zg' 'Gdnkg9VwSCimsb7KVA6A3A'
  // ToDo [Shaswat]: Remove the request Guid with proper variable
  String requestGuid = "1594123919225";
  // ToDo [Shaswat]: Remove the appId with proper method once available
  String applicationId = "V0JtPJEnSP6x391FsX0nKA";

  String orgIdentifier = commonHelper.createRandomName("cvng_org_");
  String projectIdentifier = commonHelper.createRandomName("cvng_proj_");

  @BeforeClass
  public void setup() {
    try {
      cvngHelper.createOrgForCV(orgIdentifier);
      cvngHelper.createProjectForCV(orgIdentifier, projectIdentifier);
    } catch (FileNotFoundException e) {
      log.info("File Not Found Exception: " + e);
    }
  }

  @Test(description = "Verify the response of Tier: pass_scenario_previous with Default Metric Pack: Performance ")
  public void validateMetricPackWithTierTest() throws FileNotFoundException {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);
    int appId = appList.jsonPath().getInt("resource[0].id");

    Response tiersList = appdynamicsHelper.getTiersList(connectorId, "" + appId);
    int tierId = tiersList.jsonPath().getInt("resource[3].id");

    // Query Params for the post call
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("connectorId", connectorId);
    queryParams.put("projectIdentifier", projectIdentifier);
    queryParams.put("appdAppId", appId);
    queryParams.put("appdTierId", tierId);
    queryParams.put("requestGuid", requestGuid);

    // Creating a metric with One Metric Pack: Performance and Availability
    MetricPacks metricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_PERFORMANCE_METRIC_PACKS);
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    // Converting the MetricPackList to a JsonString
    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    log.info("Metric Pack: " + body);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath metricList = response.jsonPath();

    // Validation of details
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "SUCCESS");

    List<Map<String, Object>> metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    Assert.assertTrue(
        restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Errors per Minute"));
    Map<String, Object> metricResponseValue =
        restAssuredResponseParser.getMapOfValueFromList(metricValueList, "metricName", "Errors per Minute");
    Assert.assertEquals(metricResponseValue.get("apiResponseStatus"), "SUCCESS");
    Assert.assertTrue(restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Stall Count"));
  }

  @Test(description = "Verify the response of Multiple Tiers with Default Metric Pack: Performance ")
  public void validateMetricPackWithMultipleTierTest() throws FileNotFoundException {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);
    int appId = appList.jsonPath().getInt("resource[0].id");

    Response tiersList = appdynamicsHelper.getTiersList(connectorId, "" + appId);
    int tierId = tiersList.jsonPath().getInt("resource[3].id");

    // Query Params for the post call
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("connectorId", connectorId);
    queryParams.put("projectIdentifier", projectIdentifier);
    queryParams.put("appdAppId", appId);
    queryParams.put("appdTierId", tierId);
    queryParams.put("requestGuid", requestGuid);

    // Creating a metric with One Metric Pack: Performance and Availability
    MetricPacks metricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_PERFORMANCE_METRIC_PACKS);
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    // Converting the MetricPackList to a JsonString
    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    log.info("Metric Pack: " + body);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath metricList = response.jsonPath();

    // Validation of details
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "SUCCESS");

    List<Map<String, Object>> metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    Assert.assertTrue(
        restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Errors per Minute"));
    Map<String, Object> metricResponseValue =
        restAssuredResponseParser.getMapOfValueFromList(metricValueList, "metricName", "Errors per Minute");
    Assert.assertEquals(metricResponseValue.get("apiResponseStatus"), "SUCCESS");
    Assert.assertTrue(restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Stall Count"));

    // For different tier with status as No Data
    tierId = tiersList.jsonPath().getInt("resource[1].id");
    queryParams.put("appdTierId", "" + tierId);

    response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    metricList = response.jsonPath();

    // Validation of details
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "NO_DATA");

    metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    Assert.assertTrue(
        restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Errors per Minute"));
    metricResponseValue =
        restAssuredResponseParser.getMapOfValueFromList(metricValueList, "metricName", "Errors per Minute");
    Assert.assertEquals(metricResponseValue.get("apiResponseStatus"), "NO_DATA");
    Assert.assertTrue(restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Stall Count"));

    // Status as Failed
    tierId = tiersList.jsonPath().getInt("resource[2].id");
    queryParams.put("appdTierId", "" + tierId);

    response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    metricList = response.jsonPath();

    // Validation of details
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "FAILED");

    metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    Assert.assertTrue(
        restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Errors per Minute"));
    metricResponseValue =
        restAssuredResponseParser.getMapOfValueFromList(metricValueList, "metricName", "Errors per Minute");
    Assert.assertEquals(metricResponseValue.get("apiResponseStatus"), "FAILED");
    Assert.assertTrue(restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Stall Count"));
  }

  @Test(description = "Verify the response for multiple metric packs for multiple tiers")
  public void validateMultMetricPackWithMultTiersTest() throws FileNotFoundException {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);
    int appId = appList.jsonPath().getInt("resource[0].id");

    Response tiersList = appdynamicsHelper.getTiersList(connectorId, "" + appId);
    int tierId = tiersList.jsonPath().getInt("resource[1].id");

    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("connectorId", connectorId);
    queryParams.put("projectIdentifier", projectIdentifier);
    queryParams.put("appdAppId", appId);
    queryParams.put("appdTierId", tierId);
    queryParams.put("requestGuid", requestGuid);

    MetricPacks performanceMetricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_PERFORMANCE_METRIC_PACKS);
    MetricPacks qualityMetricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_QUALITY_METRIC_PACKS);
    MetricPacks resourcesMetricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_RESOURCES_METRIC_PACKS);
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(performanceMetricPacks);
    metricPacksList.add(qualityMetricPacks);
    metricPacksList.add(resourcesMetricPacks);

    // Converting the MetricPackList to a JsonString
    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath metricList = response.jsonPath();

    // Validation of details for Performance Metric Pack
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Quality");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "NO_DATA");

    Assert.assertEquals(metricList.getList("resource[0].values").size(), 2);

    // Validation of details for Quality Metric Pack
    Assert.assertEquals(metricList.getString("resource[1].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[1].overallStatus"), "NO_DATA");

    Assert.assertEquals(metricList.getList("resource[1].values").size(), 4);

    // Validation of details for Resources Metric Pack
    Assert.assertEquals(metricList.getString("resource[2].metricPackName"), "Resources");
    Assert.assertEquals(metricList.getString("resource[2].overallStatus"), "SUCCESS");

    Assert.assertEquals(metricList.getList("resource[2].values").size(), 1);

    // Status as Failed
    tierId = tiersList.jsonPath().getInt("resource[2].id");
    queryParams.put("appdTierId", "" + tierId);

    response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    metricList = response.jsonPath();

    // Validation of details
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Quality");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "FAILED");

    Assert.assertEquals(metricList.getList("resource[0].values").size(), 2);

    // Validation of details for Quality Metric Pack
    Assert.assertEquals(metricList.getString("resource[1].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[1].overallStatus"), "FAILED");

    Assert.assertEquals(metricList.getList("resource[1].values").size(), 4);

    // Validation of details for Resources Metric Pack
    Assert.assertEquals(metricList.getString("resource[2].metricPackName"), "Resources");
    Assert.assertEquals(metricList.getString("resource[2].overallStatus"), "FAILED");

    Assert.assertEquals(metricList.getList("resource[2].values").size(), 1);
  }

  @Test(description = "Verify the error response for Invalid Body")
  public void validateMetricPackWithInvalidBodyTest() throws FileNotFoundException {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);
    int appId = appList.jsonPath().getInt("resource[0].id");

    Response tiersList = appdynamicsHelper.getTiersList(connectorId, "" + appId);
    int tierId = tiersList.jsonPath().getInt("resource[1].id");

    // Query Params for the post call
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("connectorId", connectorId);
    queryParams.put("projectIdentifier", projectIdentifier);
    queryParams.put("appdAppId", appId);
    queryParams.put("appdTierId", tierId);
    queryParams.put("requestGuid", requestGuid);

    MetricPacks metricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_PERFORMANCE_METRIC_PACKS);
    metricPacks.setProjectIdentifier("");
    metricPacks.setDataSourceType("");
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 500);

    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(
        ERROR_MESSAGES.INVALID_ARGUMENT.toString(), actualErrorMessage, "Error thrown when the Metrics list is empty");
  }

  @Test(description = "Verify the response of 3P API Call logs with Success as overall status")
  public void validate3pCallWithSuccessTest() throws FileNotFoundException {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);
    int appId = appList.jsonPath().getInt("resource[0].id");

    Response tiersList = appdynamicsHelper.getTiersList(connectorId, "" + appId);
    int tierId = tiersList.jsonPath().getInt("resource[3].id");

    // Query Params for the post call
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("connectorId", connectorId);
    queryParams.put("projectIdentifier", projectIdentifier);
    queryParams.put("appdAppId", appId);
    queryParams.put("appdTierId", tierId);
    queryParams.put("requestGuid", requestGuid);

    // Creating a metric with One Metric Pack: Performance and Availability
    MetricPacks metricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_PERFORMANCE_METRIC_PACKS);
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    // Converting the MetricPackList to a JsonString
    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    log.info("Metric Pack: " + body);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath metricList = response.jsonPath();

    // Validation of details
    String metricPackName = metricList.getString("resource[0].metricPackName");
    Assert.assertEquals(metricPackName, "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "SUCCESS");

    List<Map<String, Object>> metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    int count = 0;

    for (Map<String, Object> map : metricValueList) {
      if (map.get("apiResponseStatus").equals("SUCCESS")) {
        count++;
      }
    }
    Assert.assertEquals(count, 4);
  }

  @Test(description = "Verify the response of 3P API Call logs with No Data as overall status")
  public void validate3pCallWithNoDataTest() throws FileNotFoundException {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);
    int appId = appList.jsonPath().getInt("resource[0].id");

    Response tiersList = appdynamicsHelper.getTiersList(connectorId, "" + appId);
    int tierId = tiersList.jsonPath().getInt("resource[1].id");

    // Query Params for the post call
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("connectorId", connectorId);
    queryParams.put("projectIdentifier", projectIdentifier);
    queryParams.put("appdAppId", appId);
    queryParams.put("appdTierId", tierId);
    queryParams.put("requestGuid", requestGuid);

    // Creating a metric with One Metric Pack: Performance and Availability
    MetricPacks metricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_PERFORMANCE_METRIC_PACKS);
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    // Converting the MetricPackList to a JsonString
    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    log.info("Metric Pack: " + body);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath metricList = response.jsonPath();

    // Validation of details
    String metricPackName = metricList.getString("resource[0].metricPackName");
    Assert.assertEquals(metricPackName, "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "NO_DATA");

    List<Map<String, Object>> metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    int count = 0;

    for (Map<String, Object> map : metricValueList) {
      if (map.get("apiResponseStatus").equals("NO_DATA")) {
        String metricName = ((String) map.get("metricName"));
        String metricNameWithPercentage = metricName.replaceAll(" ", "%20");
        String pathParam = metricPackName + ":" + metricName + ":" + requestGuid;

        Response response3P = appdynamicsMetricPackHelper.getThirdPartyApiCallLogs(pathParam, applicationId);

        Assert.assertEquals(response3P.statusCode(), 200);
        JsonPath apiCallLogs = response3P.jsonPath();

        Assert.assertEquals(apiCallLogs.getString("resource.response[0].request[0].name"), "Url");
        Assert.assertTrue(apiCallLogs.getString("resource.response[0].request[0].value").contains(metricPackName));
        Assert.assertTrue(
            apiCallLogs.getString("resource.response[0].request[0].value").contains(metricNameWithPercentage));

        Assert.assertEquals(apiCallLogs.getString("resource.response[0].request[1].name"), "METHOD");
        Assert.assertEquals(apiCallLogs.getString("resource.response[0].request[1].value"), "GET");

        Assert.assertEquals(apiCallLogs.getString("resource.response[0].response[0].name"), "Status Code");
        Assert.assertEquals(apiCallLogs.getInt("resource.response[0].response[0].value"), 200);

        count++;
      }
    }
    Assert.assertEquals(count, 2);
  }

  @Test(description = "Verify the response of 3P API Call logs with Error as overall status")
  public void validate3pCallWithErrorTest() throws FileNotFoundException {
    Response appList = appdynamicsHelper.getAppdAppList(connectorId);
    int appId = appList.jsonPath().getInt("resource[0].id");

    Response tiersList = appdynamicsHelper.getTiersList(connectorId, "" + appId);
    int tierId = tiersList.jsonPath().getInt("resource[2].id");

    // Query Params for the post call
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("connectorId", connectorId);
    queryParams.put("projectIdentifier", projectIdentifier);
    queryParams.put("appdAppId", appId);
    queryParams.put("appdTierId", tierId);
    queryParams.put("requestGuid", requestGuid);

    // Creating a metric with One Metric Pack: Performance and Availability
    MetricPacks metricPacks = appdynamicsMetricPackHelper.convertMetricsJsonToPojo(
        CVNGAppDynamicsConstants.REQUEST_JSON_CV_PERFORMANCE_METRIC_PACKS);
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    // Converting the MetricPackList to a JsonString
    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    log.info("Metric Pack: " + body);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath metricList = response.jsonPath();

    // Validation of details
    String metricPackName = metricList.getString("resource[0].metricPackName");
    Assert.assertEquals(metricPackName, "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "FAILED");

    List<Map<String, Object>> metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    int count = 0;

    for (Map<String, Object> map : metricValueList) {
      if (map.get("apiResponseStatus").equals("FAILED")) {
        String metricName = ((String) map.get("metricName"));
        String metricNameWithPercentage = metricName.replaceAll(" ", "%20");
        String pathParam = metricPackName + ":" + metricName + ":" + requestGuid;

        Response response3P = appdynamicsMetricPackHelper.getThirdPartyApiCallLogs(pathParam, applicationId);

        Assert.assertEquals(response3P.statusCode(), 200);
        JsonPath apiCallLogs = response3P.jsonPath();

        Assert.assertEquals(apiCallLogs.getString("resource.response[0].request[0].name"), "Url");
        Assert.assertTrue(apiCallLogs.getString("resource.response[0].request[0].value").contains(metricPackName));
        Assert.assertTrue(
            apiCallLogs.getString("resource.response[0].request[0].value").contains(metricNameWithPercentage));

        String requestName = apiCallLogs.getString("resource.response[0].request[1].name");
        int index = 1;
        if (requestName.equalsIgnoreCase("RETRY"))
          index = 2;
        Assert.assertEquals(apiCallLogs.getString("resource.response[0].request[1].name"), "RETRY");
        Assert.assertEquals(apiCallLogs.getInt("resource.response[0].request[1].value"), 2);

        Assert.assertEquals(apiCallLogs.getString("resource.response[0].request[" + index + "].name"), "METHOD");
        Assert.assertEquals(apiCallLogs.getString("resource.response[0].request[" + index + "].value"), "GET");

        Assert.assertEquals(apiCallLogs.getString("resource.response[0].response[0].name"), "Status Code");
        Assert.assertEquals(apiCallLogs.getInt("resource.response[0].response[0].value"), 400);

        Assert.assertEquals(apiCallLogs.getString("resource.response[0].response[1].name"), "Response Body");
        Assert.assertTrue(
            apiCallLogs.getString("resource.response[0].response[1].value").contains("InvalidFormatException"));
        count++;
      }
    }
    Assert.assertEquals(count, 1);
  }

  @AfterClass
  public void cleanup() {
    cvngHelper.deleteProjForCV(projectIdentifier, orgIdentifier);
    cvngHelper.deleteOrgForCV(orgIdentifier);
  }
}
