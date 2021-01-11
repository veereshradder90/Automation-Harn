package io.harness.rest.helper.ng.projects;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * author: shaswat.deep
 */
@Slf4j
public class ProjectHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  // Get Project List based on Filter
  public Response getProjectListOnFilter(Map<String, Object> queryParams) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.queryParams(queryParams);
    log.info("List of Project List Based on Filter");
    Response response = genericRequestBuilder.getCall(requestSpecification, ProjectConstants.URI_ACCOUNT_PROJECTS);

    return response;
  }

  // Get Proj list for an Org
  public Response getProjectList(String orgIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("orgIdentifier", orgIdentifier);
    log.info("List of Projects in an Org");
    Response response = genericRequestBuilder.getCall(requestSpecification, ProjectConstants.URI_PROJECTS);

    return response;
  }

  // Create a Project
  public Response createProject(String orgIdentifier, String body) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("orgIdentifier", orgIdentifier);
    requestSpecification.body(body);
    log.info("Add a Project in the Org: " + orgIdentifier);
    Response response = genericRequestBuilder.postCall(requestSpecification, ProjectConstants.URI_PROJECTS);

    return response;
  }

  // Delete Project by Identifier
  public Response deleteProjByIdentifiers(String projectIdentifier, String orgIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("projectIdentifier", projectIdentifier);
    requestSpecification.pathParam("orgIdentifier", orgIdentifier);
    log.info("Delete the Project for the Org: " + orgIdentifier);
    Response response =
        genericRequestBuilder.deleteCall(requestSpecification, ProjectConstants.URI_PROJECTS_INDENTIFIERS);

    return response;
  }

  // Get the Project details by Identifier
  public Response getProjByIdentifiers(String projectIdentifier, String orgIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("projectIdentifier", projectIdentifier);
    requestSpecification.pathParam("orgIdentifier", orgIdentifier);
    log.info("Get the Project for the Org: " + orgIdentifier);
    Response response = genericRequestBuilder.getCall(requestSpecification, ProjectConstants.URI_PROJECTS_INDENTIFIERS);

    return response;
  }

  // Update the Project details
  public Response updateProjByIdentifiers(String projectIdentifier, String orgIdentifier, String body) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("projectIdentifier", projectIdentifier);
    requestSpecification.pathParam("orgIdentifier", orgIdentifier);
    requestSpecification.body(body);
    log.info("Update the Project for the Org: " + orgIdentifier);
    Response response = genericRequestBuilder.putCall(requestSpecification, ProjectConstants.URI_PROJECTS_INDENTIFIERS);

    return response;
  }
}
