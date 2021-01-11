package io.harness.rest.helper.cvnextgen.onboarding.appdynamics;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cvnextgen.onboarding.appdynamics.metricpackpojo.MetricPacks;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * author: shaswat.deep
 */
@Slf4j
public class CVNGMetricPackHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  Gson gson = new Gson();

  public Response postMetricPacks(String body, Map<String, Object> queryParams) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParams(queryParams);
    requestSpecification.body(body);
    log.info("Response for the metric pack");
    Response response =
        genericRequestBuilder.postCall(requestSpecification, CVNGAppDynamicsConstants.URI_CV_METRIC_DATA);

    return response;
  }

  public Response getThirdPartyApiCallLogs(String pathParam, String appId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParam("appId", appId);
    requestSpecification.pathParam("metricName", pathParam);
    Response response =
        genericRequestBuilder.getCall(requestSpecification, CVNGAppDynamicsConstants.URI_CV_3P_CALL_LOGS);
    return response;
  }

  public String convertMetricPackListToJsonString(List<MetricPacks> metricPacksList) {
    Type type = new TypeToken<List<MetricPacks>>() {}.getType();
    String json = gson.toJson(metricPacksList, type);

    return json;
  }

  public MetricPacks convertMetricsJsonToPojo(String jsonFilePath) throws FileNotFoundException {
    JsonObject appReqData = jReader.readJSONFiles(jsonFilePath);
    MetricPacks metricPacks = gson.fromJson(appReqData, MetricPacks.class);

    return metricPacks;
  }
}
