package io.harness.test.rest.cvnextgen.onboarding.appdynamics;

import io.harness.rest.common.ERROR_MESSAGES;
import io.harness.rest.helper.cvnextgen.CVNGHelper;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.CVNGAppDynamicsConstants;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.CVNGAppdynamicsHelper;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.CVNGMetricPackHelper;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.metricpackpojo.MetricPacks;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.metricpackpojo.Metrics;
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
public class CVNGAppdMetricPackTest extends AbstractTest {
  CVNGMetricPackHelper appdynamicsMetricPackHelper = new CVNGMetricPackHelper();
  CVNGAppdynamicsHelper appdynamicsHelper = new CVNGAppdynamicsHelper();
  RestAssuredResponseParser restAssuredResponseParser = new RestAssuredResponseParser();
  CVNGHelper cvngHelper = new CVNGHelper();

  // ToDO [Shaswat]: Remove the connectorID once the connector function is available
  String connectorId = "YOgB1-WjSKyZWoCkuY7kBA"; // Iu3WFHCBS_-QIGuqXtR-7w YOgB1-WjSKyZWoCkuY7kBA

  // ToDo [Shaswat]: Remove the request Guid with proper variable
  String requestGuid = "1595223405072";

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

  @Test(description = "Verify the response of One Tier with Default Metric Pack ")
  public void validateMetricPackWithTierTest() throws FileNotFoundException {
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
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "NO_DATA");

    List<Map<String, Object>> metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    Assert.assertTrue(
        restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Errors per Minute"));
    Map<String, Object> metricResponseValue =
        restAssuredResponseParser.getMapOfValueFromList(metricValueList, "metricName", "Errors per Minute");
    Assert.assertEquals(metricResponseValue.get("apiResponseStatus"), "NO_DATA");
    Assert.assertTrue(restAssuredResponseParser.checkValueInListOfMap(metricValueList, "metricName", "Stall Count"));
  }

  @Test(description = "Enable a new Metric in the Default Metric Pack after the response")
  public void validateResponseWithNewMetricEnabledTest() throws FileNotFoundException {
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
    List<Metrics> metricListRequest = metricPacks.getMetricsList();
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    // Converting the MetricPackList to a JsonString
    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath metricList = response.jsonPath();

    // Validation of details
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "NO_DATA");

    List<Map<String, Object>> metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 4);

    // Enabled a new metric
    log.info("Enabled Metric: " + metricListRequest.get(2).getMetricName());
    metricListRequest.get(2).setIncluded(true);

    body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    // After enabling the call returns 5
    JsonPath metricList1 = response.jsonPath();
    Assert.assertEquals(metricList1.getList("resource[0].values").size(), 5);
    Assert.assertTrue(restAssuredResponseParser.checkValueInListOfMap(
        metricList1.getList("resource[0].values"), "metricName", "Number of Very Slow Calls"));
  }

  @Test(description = "Validation of Tiers when all the metrics are disabled in the metric pack")
  public void validateResponseWithMetricsDisabledTest() throws FileNotFoundException {
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
    List<Metrics> metricListRequest = metricPacks.getMetricsList();
    for (Metrics m : metricListRequest) {
      if (m.isIncluded() == true)
        m.setIncluded(false);
    }
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    // Converting the MetricPackList to a JsonString
    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);

    JsonPath metricList = response.jsonPath();

    // Validation of details
    Assert.assertEquals(metricList.getString("resource[0].metricPackName"), "Performance");
    Assert.assertEquals(metricList.getString("resource[0].overallStatus"), "SUCCESS");

    List<Map<String, Object>> metricValueList = metricList.getList("resource[0].values");
    Assert.assertEquals(metricValueList.size(), 0);
  }

  @Test(description = "Verify the response for multiple metric packs after update")
  public void validateMultipleMetricPacksTest() throws FileNotFoundException {
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

    // Enable and Disable Multiple Metrics of All the Metric Packs
    List<Metrics> perfMetricListRequest = performanceMetricPacks.getMetricsList();
    List<Metrics> qualityMetricListRequest = qualityMetricPacks.getMetricsList();
    List<Metrics> resourcesMetricListRequest = resourcesMetricPacks.getMetricsList();

    perfMetricListRequest.get(2).setIncluded(true);
    perfMetricListRequest.get(1).setIncluded(true);
    perfMetricListRequest.get(3).setIncluded(false);

    qualityMetricListRequest.get(0).setIncluded(false);

    resourcesMetricListRequest.get(1).setIncluded(false);
    resourcesMetricListRequest.get(2).setIncluded(true);
    resourcesMetricListRequest.get(3).setIncluded(true);

    // Response and Validation after enabling and disabling multiple metrics
    body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 200);
    metricList = response.jsonPath();

    Assert.assertEquals(metricList.getList("resource[0].values").size(), 1);
    Assert.assertEquals(metricList.getList("resource[1].values").size(), 5);
    Assert.assertEquals(metricList.getList("resource[2].values").size(), 2);
  }

  @Test(description = "Verify the response of One Tier with Empty Metric Pack ")
  public void validateMetricPackWithEmptyMetricListTest() throws FileNotFoundException {
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

    MetricPacks metricPacks = new MetricPacks();
    metricPacks.setDataSourceType("APP_DYNAMICS");
    metricPacks.setIdentifier("Resources");
    metricPacks.setProjectIdentifier("12345");
    List<MetricPacks> metricPacksList = new ArrayList<>();
    metricPacksList.add(metricPacks);

    String body = appdynamicsMetricPackHelper.convertMetricPackListToJsonString(metricPacksList);
    Response response = appdynamicsMetricPackHelper.postMetricPacks(body, queryParams);
    Assert.assertEquals(response.statusCode(), 400);

    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(
        ERROR_MESSAGES.INVALID_ARGUMENT.toString(), actualErrorMessage, "Error thrown when the Metrics list is empty");
  }

  @AfterClass
  public void cleanup() {
    cvngHelper.deleteProjForCV(projectIdentifier, orgIdentifier);
    cvngHelper.deleteOrgForCV(orgIdentifier);
  }
}
