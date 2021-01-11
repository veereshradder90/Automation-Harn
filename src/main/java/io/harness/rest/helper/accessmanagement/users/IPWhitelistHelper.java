package io.harness.rest.helper.accessmanagement.users;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class IPWhitelistHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath getIPWhitelist(RequestSpecification requestSpecification) {
    Response Response = genericRequestBuilder.getCall(requestSpecification, IPWhitelistConstants.IP_WHITELIST_URI);
    return Response.jsonPath();
  }

  public JsonPath addIPWhitelist(RequestSpecification requestSpecification, String ip, String status) {
    JsonObject ipData = new JsonObject();
    ipData.addProperty("ip", ip);
    ipData.addProperty("status", status);
    requestSpecification.body(ipData.toString());
    Response Response = genericRequestBuilder.getCall(requestSpecification, IPWhitelistConstants.IP_WHITELIST_URI);
    return Response.jsonPath();
  }
}
