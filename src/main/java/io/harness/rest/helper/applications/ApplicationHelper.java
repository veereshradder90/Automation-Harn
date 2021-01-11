package io.harness.rest.helper.applications;

import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
public class ApplicationHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  // create App without file
  public JsonPath createApplication(String appName) {
    JsonObject appData = new JsonObject();
    appData.addProperty("name", appName);
    appData.addProperty("accountId", defaultAccountId);
    Response applicationResponse = genericRequestBuilder.postCall(appData, ApplicationConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  // create App without file
  public Response createApplication(RequestSpecification requestSpecification, String appName) {
    JsonObject appData = new JsonObject();
    appData.addProperty("name", appName);
    appData.addProperty("accountId", defaultAccountId);
    requestSpecification.body(appData.toString());
    Response applicationResponse =
        genericRequestBuilder.postCall(requestSpecification, ApplicationConstants.URI_APP_CREATION);
    return applicationResponse;
  }

  // create App without file
  public JsonPath createApplicationRbac(String appName) {
    RequestSpecification requestSpecification =
        GenericRequestBuilder.getRequestSpecificationObject("dx-cp-rbac-user@mailinator.com", "Harness@123!");
    JsonObject appData = new JsonObject();
    appData.addProperty("name", appName);
    appData.addProperty("accountId", defaultAccountId);
    requestSpecification.body(appData.toString());
    Response applicationResponse =
        genericRequestBuilder.postCall(requestSpecification, ApplicationConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  // get AppId from name
  public JsonPath getApplicationId(String appName) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("sort[0][field]", "name");
    requestSpecification.queryParam("sort[0][direction]", "ASC");
    requestSpecification.queryParam("search[0][field]", "keywords");
    requestSpecification.queryParam("search[0][op]", "CONTAINS");
    requestSpecification.queryParam("search[0][value]", appName);
    Response applicationResponse =
        genericRequestBuilder.getCall(requestSpecification, ApplicationConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath editApplication(String appName, String newAppName) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    JsonObject appData = new JsonObject();
    appData.addProperty("name", newAppName);
    appData.addProperty("accountId", defaultAccountId);
    // get appId from appName
    String appId = getApplicationId(appName).getString("resource.response[0].appId");
    appData.addProperty("uuid", appId);
    requestSpecification.body(appData.toString());
    requestSpecification.pathParam("appId", appId);
    Response editedApplicationResponse =
        genericRequestBuilder.putCall(requestSpecification, ApplicationConstants.URI_APP_EDIT_DELETE);
    return editedApplicationResponse.jsonPath();
  }

  public Response deleteApp(String appNameOrId, String type) {
    String appId = appNameOrId;
    if (type.equalsIgnoreCase("name")) {
      appId = getApplicationId(appNameOrId).getString("resource.response[0].appId");
    }
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.pathParam("appId", appId);
    Response deleteResponse =
        genericRequestBuilder.deleteCall(requestSpecification, ApplicationConstants.URI_APP_EDIT_DELETE);
    return deleteResponse;
  }

  public Response deleteApp(RequestSpecification requestSpecification, String appNameOrId, String type) {
    String appId = appNameOrId;
    if (type.equalsIgnoreCase("name")) {
      appId = getApplicationId(appNameOrId).getString("resource.response[0].appId");
    }
    requestSpecification.pathParam("appId", appId);
    Response deleteResponse =
        genericRequestBuilder.deleteCall(requestSpecification, ApplicationConstants.URI_APP_EDIT_DELETE);
    return deleteResponse;
  }

  public void searchAndDeleteApp(String appNameStartingWith) {
    JsonPath applications = getApplicationId(appNameStartingWith);
    List<JsonObject> appList = applications.getList("resource.response");
    for (int app = 0, size = appList.size(); app < size; app++) {
      RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
      requestSpecification.pathParam("appId", applications.getString("resource.response[" + app + "].appId"));
      genericRequestBuilder.deleteCall(requestSpecification, ApplicationConstants.URI_APP_EDIT_DELETE);
    }
  }

  // sample code[to be removed later] - create App using json file
  public JsonPath createApplicationUsingJsonFile(String appName) throws FileNotFoundException {
    JsonObject appReqData = jReader.readJSONFiles(ApplicationConstants.REQUEST_JSON_APP_CREATION);
    appReqData.addProperty("name", appName);
    appReqData.addProperty("accountId", defaultAccountId);
    appReqData.addProperty("description", "description test automation");
    Response post = genericRequestBuilder.postCall(appReqData, ApplicationConstants.URI_APP_CREATION);
    log.info(post.getBody().asString());
    return post.jsonPath();
  }
  
  public JsonPath createApp(RequestSpecification requestSpecification, String name) {
    JsonObject appData = new JsonObject();
    appData.addProperty("name", name);
    appData.addProperty("accountId", defaultAccountId);
    requestSpecification.body(appData.toString());
    Response applicationResponse =
        genericRequestBuilder.postCall(requestSpecification, ApplicationConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public Response deleteApplication(RequestSpecification requestSpecification, String appNameOrId, String type) {
    String appId = appNameOrId;
    if (type.equalsIgnoreCase("name")) {
      appId = getApplicationId(appNameOrId).getString("resource.response.appId");
    }

    requestSpecification.pathParam("appId", appId);
    Response deleteResponse =
        genericRequestBuilder.deleteCall(requestSpecification, ApplicationConstants.URI_APP_EDIT_DELETE);
    return deleteResponse;
  }
}
