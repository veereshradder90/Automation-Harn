package io.harness.rest.core;

import static io.restassured.RestAssured.given;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import io.harness.rest.utils.ConfigProperties;
import io.harness.rest.utils.SecretsProperties;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

/*
  @author : nataraja.maruthi
 */
@Slf4j
public class RestAssuredClient {
  private static RequestSpecProvider rqProvider = new RequestSpecProvider();
  private RequestSpecification customRequestSpec;
  private static SecretsProperties secrets = new SecretsProperties();
  private static ConfigProperties configs = new ConfigProperties();
  @Getter @Setter private static String bearerToken = null;
  @Getter @Setter private static String defaultAccount = configs.getConfig("DEFAULT_ACCOUNT");

  protected RequestSpecification getCustomisedRequestSpecification() {
    if (bearerToken == null) {
      bearerToken = generateNewBearerToken();
    }
    RequestSpecification defaultRequestSpec =
        portal().auth().oauth2(bearerToken).queryParam("accountId", defaultAccount);
    return defaultRequestSpec;
  }


  protected RequestSpecification getCustomisedRequestSpecificationRbac(String userName , String passWord) {
    if (bearerToken == null) {
      bearerToken = generateNewBearerTokenRbac( userName ,  passWord);
    }
    RequestSpecification defaultRequestSpec =
            portal().auth().oauth2(bearerToken).queryParam("accountId", "-IWqU3BSSjqPbISDIW036Q");
    return defaultRequestSpec;
  }

  public RequestSpecification getBasicRequestSpecification() {
    customRequestSpec = portal();
    return customRequestSpec;
  }

  private static RequestSpecification portal() {
    return given().spec(rqProvider.useDefaultSpec());
  }

  public String loginUser(String email, String password) {
    String basicAuthValue = "Basic " + encodeBase64String(String.format("%s:%s", email, password).getBytes());
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("authorization", basicAuthValue);
    Response response = portal().body(jsonObject.toJSONString()).post("/users/login");
    if (response.getStatusCode() != 200) {
      log.info("Login failed " + response.getBody().asString());
      return null;
    }
    log.info("Login Successfull for User :  " + email);
    String bearerToken = response.jsonPath().getString("resource.token");
    return bearerToken;
  }

  public Boolean isTokenValid(String bearerToken) {
    if (bearerToken == null) {
      return false;
    }
    Response response =
        portal().auth().oauth2(bearerToken).queryParam("accountId", defaultAccount).get("/whitelist/isEnabled");
    return response.getStatusCode() == 200;
  }

  public String generateNewBearerToken() {
    String defaultUser = System.getProperty("DEFAULT_USER");
    if (defaultUser == null) {
      defaultUser = configs.getConfig("DEFAULT_USER");
    }
    String defaultPassword = secrets.getSecret("DEFAULT_PASSWORD");
    log.info("Generating new Bearer token for User {} " + defaultUser);
    String bearerToken = loginUser(defaultUser, defaultPassword);
    return bearerToken;
  }


  public String generateNewBearerTokenRbac(String userName, String passWord) {
    log.info("Generating new Bearer token for User {} " + userName);
    String bearerToken = loginUser(userName, passWord);
    return bearerToken;
  }

  public boolean logoutUser(String userId, String bearerToken, String accountId) {
    Response response = portal()
                            .auth()
                            .oauth2(bearerToken)
                            .body("routingId=" + accountId)
                            .queryParam("routingId", accountId)
                            .post("/users/" + userId + "/logout");
    if (response.getStatusCode() != 200) {
      log.info("logout failed " + response.getBody().asString());
    }
    return response.getStatusCode() == 200;
  }
}
