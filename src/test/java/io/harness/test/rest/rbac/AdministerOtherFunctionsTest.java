package io.harness.test.rest.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.accessmanagement.users.APIKeysHelper;
import io.harness.rest.helper.accessmanagement.users.AuthenticationSettingsHelper;
import io.harness.rest.helper.accessmanagement.users.IPWhitelistHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AdministerOtherFunctionsTest extends AbstractTest {
  AuthenticationSettingsHelper authenticationSettingsHelper = new AuthenticationSettingsHelper();
  APIKeysHelper apiKeysHelper = new APIKeysHelper();
  IPWhitelistHelper ipWhitelistHelper = new IPWhitelistHelper();

  @Test
  public void authenticationSettingsTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_otherfunctions@mailinator.com", "Harness@123");
    JsonPath authResponse = authenticationSettingsHelper.getLoginSettings(requestSpecificationObject);
    assertThat(
        authResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  public void apiKeysTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_otherfunctions@mailinator.com", "Harness@123");
    // get API keys
    JsonPath apiResponse = apiKeysHelper.getAPIKeys(requestSpecificationObject);
    assertThat(
        apiResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // add API keys
    JsonPath addapiResponse = apiKeysHelper.addAPIKeys(requestSpecificationObject, "dummy");
    assertThat(
        addapiResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // delete API keys
    JsonPath deleteapiResponse = apiKeysHelper.deleteAPIKeys(requestSpecificationObject, "platform-sanity");
    assertThat(deleteapiResponse.getString("responseMessages.message")
                   .equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  public void ipWhitelistTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_otherfunctions@mailinator.com", "Harness@123");
    // get IP Whitelist
    JsonPath ipResponse = ipWhitelistHelper.getIPWhitelist(requestSpecificationObject);
    assertThat(
        ipResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // add IP Whitelist
    JsonPath addIpResponse = ipWhitelistHelper.addIPWhitelist(requestSpecificationObject, "0.0.0.0", "DISABLED");
    assertThat(
        addIpResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }
}
