package io.harness.rest.helper.cvnextgen.timeseries;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cvnextgen.CVNGConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * author: shaswat.deep
 */
@Slf4j
public class CVNGTimeseriesHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public Response getMetricGroupData(Map<String, Object> queryParams) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParams(queryParams);
    log.info("Get Timeseries Metric Group Data");
    Response response = genericRequestBuilder.getCall(requestSpecification, CVNGConstants.URI_CV_TIMESERIES_GROUP_DATA);

    return response;
  }

  public Response getMetricTemplate(String cvConfigId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("cvConfigId", cvConfigId);
    log.info("Get Timeseries Metric Template");
    Response response =
        genericRequestBuilder.getCall(requestSpecification, CVNGConstants.URI_CV_TIMESERIES_METRIC_TEMPLATE);

    return response;
  }
}
