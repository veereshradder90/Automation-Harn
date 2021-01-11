package io.harness.test.rest.cvnextgen.onboarding.splunk;

import io.harness.rest.common.ERROR_MESSAGES;
import io.harness.rest.helper.cvnextgen.onboarding.splunk.CVNGSplunkHelper;
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
@Test(groups = {"CVNG", "Splunk", "LOCAL"}, enabled = true)
public class CVNGSplunkQueryTest extends AbstractTest {
  CVNGSplunkHelper splunkHelper = new CVNGSplunkHelper();

  // ToDO [Shaswat]: Remove the connectorID once the connector function is available
  String connectorId = "HOrL8x6vSgejkIKS4UAm_g";

  @Test(description = "Verify the query list for Splunk Mock Server")
  public void getQueryListForSplunkTest() {
    Response response = splunkHelper.getSplunkSavedSearches(connectorId);
    Assert.assertEquals(response.statusCode(), 200);
    JsonPath queryList = response.jsonPath();

    // Validate the size of Query List
    Assert.assertEquals(queryList.getList("resource").size(), 8);
  }

  @Test(description = "Verify the Error message for Splunk with Invalid Connector Id")
  public void getQueryListForSplunkInvalidConnectorTest() {
    Response response = splunkHelper.getSplunkSavedSearches("abcd");
    Assert.assertEquals(response.statusCode(), 500);

    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the connector ID is invalid");
  }

  @Test(description = "Verify the Histogram Data for the Query: Errors in the last 24 hours")
  public void getHistogramForQueryTest() {
    Response response = splunkHelper.getSplunkSavedSearches(connectorId);
    Assert.assertEquals(response.statusCode(), 200);
    JsonPath queryList = response.jsonPath();

    Assert.assertEquals(queryList.getList("resource").size(), 8);
    String query = queryList.getString("resource[0].searchQuery");

    Response histogramResponse = splunkHelper.getSplunkHistogram(connectorId, query);
    Assert.assertEquals(histogramResponse.statusCode(), 200);
    JsonPath histogramBars = histogramResponse.jsonPath();

    Assert.assertEquals(histogramBars.getString("resource.query"),
        "error OR failed OR severe OR ( sourcetype=access_* ( 404 OR 500 OR 503 ) )");
    Assert.assertTrue(histogramBars.getList("resource.bars").size() > 0);
    Assert.assertEquals(histogramBars.getInt("resource.intervalMs"), 21600000);
  }

  @Test(description = "Verify the Sample Logs and Service Instance Fields for the Query: Errors in the last 24 hours")
  public void getSamplesForQueryTest() {
    Response response = splunkHelper.getSplunkSavedSearches(connectorId);
    Assert.assertEquals(response.statusCode(), 200);
    JsonPath queryList = response.jsonPath();

    Assert.assertEquals(queryList.getList("resource").size(), 8);
    String query = queryList.getString("resource[0].searchQuery");

    Response sampleResponse = splunkHelper.getSplunkSamples(connectorId, query);
    Assert.assertEquals(sampleResponse.statusCode(), 200);
    JsonPath samplesData = sampleResponse.jsonPath();

    Assert.assertTrue(samplesData.getString("resource.rawSampleLogs").contains("java.lang.InterruptedException"));
    Assert.assertEquals(samplesData.getMap("resource.sample").size(), 6);
    Assert.assertEquals(samplesData.getString("resource.sample.source"), "http:httpCollector");
  }

  @Test(description = "Verify the error code for Histogram call with invalid Connector Id")
  public void getHistogramWithInvalidConnectorIdTest() {
    Response response = splunkHelper.getSplunkSavedSearches(connectorId);
    Assert.assertEquals(response.statusCode(), 200);
    JsonPath queryList = response.jsonPath();

    Assert.assertEquals(queryList.getList("resource").size(), 8);
    String query = queryList.getString("resource[0].searchQuery");

    Response histogramResponse = splunkHelper.getSplunkHistogram("abcd", query);
    Assert.assertEquals(histogramResponse.statusCode(), 500);

    String actualErrorMessage = histogramResponse.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the connector ID is invalid");
  }

  @Test(description = "Verify the error code for Samples call with invalid Connector Id")
  public void getSampleWithInvalidConnectorIdTest() {
    Response response = splunkHelper.getSplunkSavedSearches(connectorId);
    Assert.assertEquals(response.statusCode(), 200);
    JsonPath queryList = response.jsonPath();

    Assert.assertEquals(queryList.getList("resource").size(), 8);
    String query = queryList.getString("resource[0].searchQuery");

    Response sampleResponse = splunkHelper.getSplunkSamples("abcd", query);
    Assert.assertEquals(sampleResponse.statusCode(), 500);

    String actualErrorMessage = sampleResponse.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the connector ID is invalid");
  }

  @Test(description = "Verify the Histogram Data for the Empty Query")
  public void getHistogramWithEmptyQueryTest() {
    Response histogramResponse = splunkHelper.getSplunkHistogram(connectorId, "");
    Assert.assertEquals(histogramResponse.statusCode(), 200);

    String actualErrorMessage = histogramResponse.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(
        ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage, "Error thrown when the query is empty");
  }

  @Test(description = "Verify the Sample Logs and Service Instance Fields for the Empty Query")
  public void getSamplesWithEmptyQueryTest() {
    Response sampleResponse = splunkHelper.getSplunkSamples(connectorId, "");
    Assert.assertEquals(sampleResponse.statusCode(), 200);

    String actualErrorMessage = sampleResponse.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(
        ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage, "Error thrown when the query is empty");
  }
}