package io.harness.rest.helper.templatelibrary;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemplateLibraryHelper extends CoreUtils {
  public String getTemplateLibraryId(String templateName) {
    Response response = searchForTemplate(templateName);
    if (response.getStatusCode() == 200) {
      String templateId = response.jsonPath().getString("resource.response[0].uuid");
      return templateId;
    }
    log.info("error while fetching template library" + response.getBody().asString());
    return null;
  }

  public Response searchForTemplate(String templateName) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("search[0][field]", "keywords");
    requestSpecification.queryParam("search[0][op]", "CONTAINS");
    requestSpecification.queryParam("search[0][value]", templateName);
    requestSpecification.queryParam("offset", 0);
    requestSpecification.queryParam("limit", "UNLIMITED");
    requestSpecification.queryParam("defaultVersion", false);
    Response response =
        GenericRequestBuilder.getCall(requestSpecification, TemplateLibraryConstants.URI_TEMPLATE_CREATION);
    return response;
  }

  public Response deleteTemplateLibrary(String templateName) {
    String templateLibraryId = getTemplateLibraryId(templateName);
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.pathParam("templateId", templateLibraryId);
    Response response =
        GenericRequestBuilder.deleteCall(requestSpecification, TemplateLibraryConstants.URI_TEMPLARE_UPDATE);
    return response;
  }

  public Response deleteTemplateLibrary(RequestSpecification requestSpecification, String templateName) {
    String templateLibraryId = getTemplateLibraryId(templateName);
    requestSpecification.pathParam("templateId", templateLibraryId);
    Response response =
        GenericRequestBuilder.deleteCall(requestSpecification, TemplateLibraryConstants.URI_TEMPLARE_UPDATE);
    return response;
  }
  
  public Response addTemplateLibrary(String name, String type, JsonObject templateObject) {
    JsonObject jsonObject = new JsonObject();
    ;
    jsonObject.add("templateObject", templateObject);
    jsonObject.addProperty("folderId", "QLdcWedPRVugdAUEMLjtbQ");
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("type", type);
    jsonObject.add("variables", new JsonArray());
    Response response = GenericRequestBuilder.postCall(jsonObject, TemplateLibraryConstants.URI_TEMPLATE_CREATION);
    return response;
  }

  public Response addTemplateLibrary(
      RequestSpecification requestSpecification, String name, String type, JsonObject templateObject) {
    JsonObject jsonObject = new JsonObject();
    ;
    jsonObject.add("templateObject", templateObject);
    jsonObject.addProperty("folderId", "QLdcWedPRVugdAUEMLjtbQ");
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("type", type);
    jsonObject.add("variables", new JsonArray());
    requestSpecification.body(jsonObject.toString());
    Response response =
        GenericRequestBuilder.postCall(requestSpecification, TemplateLibraryConstants.URI_TEMPLATE_CREATION);
    return response;
  }
}
