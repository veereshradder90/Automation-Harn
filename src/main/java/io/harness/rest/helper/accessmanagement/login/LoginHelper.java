package io.harness.rest.helper.accessmanagement.login;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import com.google.gson.JsonObject;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.accessmanagement.users.UserConstants;
import io.harness.rest.utils.ConfigProperties;
import io.harness.rest.utils.SecretsProperties;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginHelper {
  public String loginUser(String email, String password) {
    String basicAuthValue = "Basic " + encodeBase64String(String.format("%s:%s", email, password).getBytes());
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("authorization", basicAuthValue);
    Response response = GenericRequestBuilder.postCall(jsonObject, UserConstants.USER_LOGIN);
    if (response.getStatusCode() != 200) {
      log.info("Login failed " + response.getBody().asString());
      return null;
    }
    log.info("Login Successfull for User :  " + email);
    String bearerToken = response.jsonPath().getString("resource.token");
    return bearerToken;
  }

  public String loginToDefaultAdminUser() {
    String defaultUser = new ConfigProperties().getConfig("DEFAULT_USER");
    String defaultUserPassword = new SecretsProperties().getSecret("DEFAULT_PASSWORD");

    String basicAuthValue =
        "Basic " + encodeBase64String(String.format("%s:%s", defaultUser, defaultUserPassword).getBytes());
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("authorization", basicAuthValue);
    Response response = GenericRequestBuilder.postCall(jsonObject, UserConstants.USER_LOGIN);
    if (response.getStatusCode() != 200) {
      log.info("Login failed " + response.getBody().asString());
      return null;
    }
    log.info("Login Successfull for User :  " + defaultUser);
    String bearerToken = response.jsonPath().getString("resource.token");
    return bearerToken;
  }
}
