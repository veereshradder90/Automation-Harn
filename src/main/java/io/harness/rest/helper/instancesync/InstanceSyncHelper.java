package io.harness.rest.helper.instancesync;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Nataraja.Maruthi
 * @apiNote This is the helper class for instance sync details
 */
public class InstanceSyncHelper extends CoreUtils {
  public Response getApplicationInstanceSummary(String appId, String groupByType) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParams("appId", appId);
    requestSpecification.queryParams("groupBy", groupByType);
    Response response = GenericRequestBuilder.getCall(requestSpecification, InstanceSyncConstants.APP_INSTANCE_SUMMARY);
    return response;
  }

  public Response getApplicationInstanceCount(String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParams("appId", appId);
    requestSpecification.queryParams("offset", 0);
    requestSpecification.queryParams("limit", 1000);
    Response response = GenericRequestBuilder.getCall(requestSpecification, InstanceSyncConstants.APP_INSTANCE_COUNT);
    return response;
  }

  public Response getServiceInstanceStats(String appId, String serviceId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParams("appId", appId);
    requestSpecification.queryParams("serviceId", serviceId);
    Response response =
        GenericRequestBuilder.getCall(requestSpecification, InstanceSyncConstants.SERVICE_INSTANCE_STATS);
    return response;
  }

  public Response getInstanceDetails(String instanceId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParams("instanceId", instanceId);
    Response response = GenericRequestBuilder.getCall(requestSpecification, InstanceSyncConstants.INSTANCE_DETAILS);
    return response;
  }

  public Response getInstanceDetails(String applicationId, String serviceId) {
    Response response = getServiceInstanceStats(applicationId, serviceId);
    ArrayList<HashMap> resource =
        response.jsonPath().getJsonObject("resource[0].instanceStatsByArtifactList.instanceStats.entitySummaryList[0]");
    HashMap map = resource.get(0);
    Response instanceResponse = getInstanceDetails(map.get("id").toString());
    return instanceResponse;
  }
}
