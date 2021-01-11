package io.harness.rest.core;

import com.google.gson.JsonObject;

import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericRequestBuilder {
  static RestAssuredClient restClient = new RestAssuredClient();

  public static RequestSpecification getRequestSpecificationObject() {
    RequestSpecification requestSpecification = restClient.getCustomisedRequestSpecification();
    return requestSpecification;
  }

  public static RequestSpecification getRequestSpecificationObjectNG() {
    RequestSpecification requestSpecification = restClient.getCustomisedRequestSpecification();
    String env = System.getProperty("env.type");
    if (env.equalsIgnoreCase("QA") || env.equalsIgnoreCase("PROD"))
      requestSpecification.basePath("/gateway");
    else
      requestSpecification.basePath("");
    if (env.equalsIgnoreCase("LOCAL"))
      requestSpecification.port(8181);
    return requestSpecification;
  }

  public static RequestSpecification getRequestSpecificationObject(String email, String password) {
    String bearerToken = restClient.loginUser(email, password);
    RequestSpecification requestSpecification = restClient.getCustomisedRequestSpecification();
    requestSpecification.auth().oauth2(bearerToken);
    return requestSpecification;
  }

  public static RequestSpecification getMultipartRequestSpecification() {
    RequestSpecification requestSpecification = restClient.getCustomisedRequestSpecification();
    requestSpecification.config(RestAssuredConfig.config().encoderConfig(
        EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.JSON)));
    requestSpecification.header("content-type", "multipart/form-data");
    return requestSpecification;
  }

  public static Response postCall(JsonObject jsonReqBody, String uri) {
    RequestSpecification requestSpecification = getRequestSpecificationObject();
    requestSpecification.body(jsonReqBody.toString());
    Response responseData = postCall(requestSpecification, uri);
    return responseData;
  }

  public static Response postCall(RequestSpecification requestSpecification, String uri) {
    log.info("URI in post call:  " + uri);
    log.info("Request Body:  " + ((RequestSpecificationImpl) requestSpecification).getBody());
    Response responseData = requestSpecification.post(uri);
    log.info("Status Code: " + String.valueOf(responseData.getStatusCode()));
    log.info("Response Body: " + responseData.getBody().asString());
    return responseData;
  }

  public static Response putCall(JsonObject jsonReqBody, String uri) {
    RequestSpecification requestSpecification = getRequestSpecificationObject();
    requestSpecification.body(jsonReqBody.toString());
    Response responseData = putCall(requestSpecification, uri);
    return responseData;
  }

  public static Response putCall(RequestSpecification requestSpecification, String uri) {
    Response responseData;
    log.info("URI:  " + uri);
    log.info("Request Body:  " + ((RequestSpecificationImpl) requestSpecification).getBody());
    responseData = requestSpecification.put(uri);
    log.info("Status Code: " + String.valueOf(responseData.getStatusCode()));
    log.info("Response Body: " + responseData.getBody().asString());
    return responseData;
  }

  public static Response getCall(String uri) {
    RequestSpecification requestSpecificationObject = getRequestSpecificationObject();
    Response responseData = getCall(requestSpecificationObject, uri);
    return responseData;
  }

  public static Response getCall(RequestSpecification requestSpecification, String uri) {
    Response responseData;
    log.info("URI:  " + uri);
    log.info("Request Body:  " + ((RequestSpecificationImpl) requestSpecification).getBody());
    responseData = requestSpecification.get(uri);
    log.info("Status Code: " + String.valueOf(responseData.getStatusCode()));
    log.info("Response Body: " + responseData.getBody().asString());
    return responseData;
  }

  public static Response deleteCall(String uri) {
    RequestSpecification requestSpecification = getRequestSpecificationObject();
    Response responseData = deleteCall(requestSpecification, uri);
    return responseData;
  }

  public static Response deleteCall(RequestSpecification requestSpecification, String uri) {
    Response responseData;
    log.info("URI for delete:  " + uri);
    log.info("Request Body:  " + ((RequestSpecificationImpl) requestSpecification).getBody());
    responseData = requestSpecification.delete(uri);
    log.info("Status Code: " + String.valueOf(responseData.getStatusCode()));
    log.info("Response Body: " + responseData.getBody().asString());
    return responseData;
  }
}
