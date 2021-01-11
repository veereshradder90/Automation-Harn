package io.harness.rest.helper.accessmanagement.users;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthenticationSettingsHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath getLoginSettings(RequestSpecification requestSpecification) {
    Response applicationResponse =
        genericRequestBuilder.getCall(requestSpecification, AuthenticationSettingsConstants.GET_LOGIN_SETTINGS);
    return applicationResponse.jsonPath();
  }
}
