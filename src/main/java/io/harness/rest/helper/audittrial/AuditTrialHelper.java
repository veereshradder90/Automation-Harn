package io.harness.rest.helper.audittrial;

import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuditTrialHelper extends CoreUtils {
  public Response getAuditTrial() {
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject filterData = new JsonObject();
    filterData.addProperty("preferenceType", "AUDIT_PREFERENCE");
    filterData.addProperty("offset", 0);
    filterData.addProperty("lastNDays", 7);
    filterData.addProperty("startTime", 1597767141524l);
    filterData.addProperty("endTime", 1598371941524l);
    filterData.addProperty("includeAccountLevelResources", true);
    filterData.addProperty("includeAppLevelResources", true);
    requestSpecificationObject.queryParam("offset", 0);
    requestSpecificationObject.queryParam("limit", 40);
    requestSpecificationObject.queryParam("filter", filterData);
    Response response = GenericRequestBuilder.getCall(requestSpecificationObject, AuditTrialConstants.AUDIT_TRIAL);
    return response;
  }

  public Response getAuditTrial(RequestSpecification requestSpecificationObject) {
    JsonObject filterData = new JsonObject();
    filterData.addProperty("preferenceType", "AUDIT_PREFERENCE");
    filterData.addProperty("offset", 0);
    filterData.addProperty("lastNDays", 7);
    filterData.addProperty("startTime", 1597767141524l);
    filterData.addProperty("endTime", 1598371941524l);
    filterData.addProperty("includeAccountLevelResources", true);
    filterData.addProperty("includeAppLevelResources", true);
    requestSpecificationObject.queryParam("offset", 0);
    requestSpecificationObject.queryParam("limit", 40);
    requestSpecificationObject.queryParam("filter", filterData);
    Response response = GenericRequestBuilder.getCall(requestSpecificationObject, AuditTrialConstants.AUDIT_TRIAL);
    return response;
  }
}
