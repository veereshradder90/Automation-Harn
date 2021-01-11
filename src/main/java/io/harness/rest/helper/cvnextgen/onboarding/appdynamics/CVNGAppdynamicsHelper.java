package io.harness.rest.helper.cvnextgen.onboarding.appdynamics;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * author: shaswat.deep
 */
@Slf4j
public class CVNGAppdynamicsHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  // Remove the accountID parameter and change to default accountID
  public Response getAppdAppList(String settingId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("settingId", settingId);
    log.info("List of Apps for the AppD connector id: " + settingId);
    Response response = genericRequestBuilder.getCall(requestSpecification, CVNGAppDynamicsConstants.URI_CV_APPS_LIST);

    return response;
  }

  public Response getMetricPacks(String projectIdentifier, String dataSourceType) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("projectIdentifier", projectIdentifier);
    requestSpecification.queryParam("dataSourceType", dataSourceType);
    log.info("Get Metric Packs for the ProjectID: " + projectIdentifier + " and for the DataSource: " + dataSourceType);
    Response response =
        genericRequestBuilder.getCall(requestSpecification, CVNGAppDynamicsConstants.URI_CV_METRIC_PACK_METRICSPACKS);

    return response;
  }

  public Response getMetricPacksMissingDS(String projectIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("projectIdentifier", projectIdentifier);
    log.info("Get Metric Packs Missing param DS for the ProjectID: " + projectIdentifier);
    Response response =
        genericRequestBuilder.getCall(requestSpecification, CVNGAppDynamicsConstants.URI_CV_METRIC_PACK_METRICSPACKS);

    return response;
  }

  public Response getTiersList(String settingId, String appdynamicsAppId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("settingId", settingId);
    requestSpecification.queryParam("appdynamicsAppId", appdynamicsAppId);
    log.info(
        "Get Tiers List for the Connector ID: " + settingId + " and for the appdynamicsAppId: " + appdynamicsAppId);
    Response response = genericRequestBuilder.getCall(requestSpecification, CVNGAppDynamicsConstants.URI_CV_TIERS_LIST);

    return response;
  }

  public Response getTiersListMissingAppId(String settingId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("settingId", settingId);
    log.info("Get Tiers List for the Connector ID: " + settingId + " and for the missing appdynamicsAppId");
    Response response = genericRequestBuilder.getCall(requestSpecification, CVNGAppDynamicsConstants.URI_CV_TIERS_LIST);

    return response;
  }

  public Response getTiersListMissingConnectorId(String appdynamicsAppId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("appdynamicsAppId", appdynamicsAppId);
    log.info("Get Tiers List for the Connector ID is missing and for the appdynamicsAppId: " + appdynamicsAppId);
    Response response = genericRequestBuilder.getCall(requestSpecification, CVNGAppDynamicsConstants.URI_CV_TIERS_LIST);

    return response;
  }
}