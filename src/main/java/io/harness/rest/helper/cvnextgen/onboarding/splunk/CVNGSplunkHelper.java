package io.harness.rest.helper.cvnextgen.onboarding.splunk;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cvnextgen.CVNGConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * author: shaswat.deep
 */
@Slf4j
public class CVNGSplunkHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public Response getSplunkHistogram(String connectorId, String query) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("connectorId", connectorId);
    requestSpecification.queryParam("query", query);
    Response responseSplunkHistogram =
        genericRequestBuilder.getCall(requestSpecification, CVNGSplunkConstants.URI_CV_SPLUNK_HISTOGRAM);
    log.info("Splunk Histogram");
    return responseSplunkHistogram;
  }

  public Response getSplunkSamples(String connectorId, String query) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("connectorId", connectorId);
    requestSpecification.queryParam("query", query);
    Response responseSplunkSamples =
        genericRequestBuilder.getCall(requestSpecification, CVNGSplunkConstants.URI_CV_SPLUNK_SAMPLES);
    log.info("Splunk Samples");
    return responseSplunkSamples;
  }

  public Response getSplunkSavedSearches(String connectorId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("connectorId", connectorId);
    Response responseSplunkSavedSearches =
        genericRequestBuilder.getCall(requestSpecification, CVNGSplunkConstants.URI_CV_SPLUNK_SAVED_SEARCHES);
    log.info("Splunk Saved Searches");
    return responseSplunkSavedSearches;
  }

  public Response putSplunkSaveQuery(String body) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.body(body);
    Response responseSplunkSaved = genericRequestBuilder.putCall(requestSpecification, CVNGConstants.URI_CV_DS_CONFIG);
    log.info("Splunk Saving Search queries");
    return responseSplunkSaved;
  }
}