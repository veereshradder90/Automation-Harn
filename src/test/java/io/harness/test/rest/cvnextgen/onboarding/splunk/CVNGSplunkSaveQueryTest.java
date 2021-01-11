package io.harness.test.rest.cvnextgen.onboarding.splunk;

import com.google.gson.JsonObject;

import io.harness.rest.common.ERROR_MESSAGES;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.cvnextgen.CVNGHelper;
import io.harness.rest.helper.cvnextgen.onboarding.splunk.CVNGSplunkConstants;
import io.harness.rest.helper.cvnextgen.onboarding.splunk.CVNGSplunkHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

/**
 * author: shaswat.deep
 */
@Slf4j
@Test(groups = {"CVNG", "Splunk", "LOCAL"}, enabled = true)
public class CVNGSplunkSaveQueryTest extends AbstractTest {
  CVNGSplunkHelper splunkHelper = new CVNGSplunkHelper();
  CommonHelper commonHelper = new CommonHelper();
  CVNGHelper cvngHelper = new CVNGHelper();

  String productName = "Splunk Enterprise";

  String orgIdentifier = commonHelper.createRandomName("cvng_org_");
  String projectIdentifier = commonHelper.createRandomName("cvng_proj_");
  String serviceIdentifier = commonHelper.createRandomName("cvng_service_");
  String envIdentifier = commonHelper.createRandomName("cvng_env_");

  // ToDo [Shaswat]: Remove the connectorID once the connector function is available
  String connectorId = "HOrL8x6vSgejkIKS4UAm_g";

  @BeforeClass
  public void setup() {
    try {
      cvngHelper.createOrgForCV(orgIdentifier);
      cvngHelper.createProjectForCV(orgIdentifier, projectIdentifier);
      cvngHelper.createEnvironmentForCV(orgIdentifier, projectIdentifier, envIdentifier);
      cvngHelper.createServiceForCV(orgIdentifier, projectIdentifier, serviceIdentifier);
    } catch (FileNotFoundException e) {
      log.info("File Not Found Exception: " + e);
    }
  }

  @Test(description = "Verify the response for Saving a Splunk Query with Valid params")
  public void getSaveSplunkWithValidParamsTest() throws FileNotFoundException {
    Response responseSearches = splunkHelper.getSplunkSavedSearches(connectorId);
    Assert.assertEquals(responseSearches.statusCode(), 200);

    String queryTitle = responseSearches.jsonPath().getString("resource[0].title");
    String queryString = responseSearches.jsonPath().getString("resource[0].searchQuery");

    JsonObject appReqData = jReader.readJSONFiles(CVNGSplunkConstants.REQUEST_JSON_CV_SPLUNK_SAVED_QUERY);

    appReqData.addProperty("identifier", queryTitle);
    appReqData.addProperty("projectIdentifier", projectIdentifier);
    appReqData.addProperty("productName", productName);
    appReqData.addProperty("connectorId", connectorId);
    appReqData.addProperty("serviceIdentifier", serviceIdentifier);
    appReqData.addProperty("envIdentifier", envIdentifier);
    appReqData.addProperty("query", queryString);
    appReqData.addProperty("eventType", "Quality");
    appReqData.addProperty("serviceInstanceIdentifier", "source");

    String body = appReqData.toString();
    Response response = splunkHelper.putSplunkSaveQuery(body);
    Assert.assertEquals(response.statusCode(), 204);
  }

  @Test(description = "Verify the response for Saving a Splunk Query with Empty body")
  public void getSaveSplunkWithEmptyBodyTest() throws FileNotFoundException {
    JsonObject appReqData = jReader.readJSONFiles(CVNGSplunkConstants.REQUEST_JSON_CV_SPLUNK_SAVED_QUERY);

    String body = appReqData.toString();

    Response response = splunkHelper.putSplunkSaveQuery(body);
    Assert.assertEquals(response.statusCode(), 500);

    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.DEFAULT_ERROR_CODE.toString(), actualErrorMessage,
        "Error thrown when the request body is empty");
  }

  @Test(description = "Verify the response for Saving a Splunk Query with Missing identifier")
  public void getSaveSplunkWithInvalidIdentifierTest() throws FileNotFoundException {
    Response responseSearches = splunkHelper.getSplunkSavedSearches(connectorId);
    Assert.assertEquals(responseSearches.statusCode(), 200);

    String queryString = responseSearches.jsonPath().getString("resource[0].searchQuery");

    JsonObject appReqData = jReader.readJSONFiles(CVNGSplunkConstants.REQUEST_JSON_CV_SPLUNK_SAVED_QUERY);

    appReqData.addProperty("projectIdentifier", projectIdentifier);
    appReqData.addProperty("productName", productName);
    appReqData.addProperty("connectorId", connectorId);
    appReqData.addProperty("serviceIdentifier", serviceIdentifier);
    appReqData.addProperty("envIdentifier", envIdentifier);
    appReqData.addProperty("query", queryString);
    appReqData.addProperty("eventType", "Quality");
    appReqData.addProperty("serviceInstanceIdentifier", "source");

    String body = appReqData.toString();

    Response response = splunkHelper.putSplunkSaveQuery(body);
    Assert.assertEquals(response.statusCode(), 204);
  }

  @AfterClass
  public void cleanup() {
    cvngHelper.deleteEnvironmentForCV(envIdentifier);
    cvngHelper.deleteServiceForCV(serviceIdentifier);
    cvngHelper.deleteProjForCV(projectIdentifier, orgIdentifier);
    cvngHelper.deleteOrgForCV(orgIdentifier);
  }
}