package io.harness.test.rest.cvnextgen.onboarding.projects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.harness.rest.common.ERROR_MESSAGES;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.cvnextgen.CVNGHelper;
import io.harness.rest.helper.ng.projects.ProjectConstants;
import io.harness.rest.helper.ng.projects.ProjectHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * author: shaswat.deep
 */
@Slf4j
@Test(groups = {"CVNG", "Projects", "LOCAL"}, enabled = true)
public class CVNGProjectsTest extends AbstractTest {
  ProjectHelper projectHelper = new ProjectHelper();
  CommonHelper commonHelper = new CommonHelper();
  CVNGHelper cvngHelper = new CVNGHelper();

  public static final String ORG_NAME_PREFIX = "cvng_org_";
  public static final String PROJ_NAME_PREFIX = "cvng_proj_";
  String orgName = commonHelper.createRandomName(ORG_NAME_PREFIX);
  List<String> projectIdentifier = new ArrayList<>();

  @BeforeClass
  public void setup() {
    try {
      cvngHelper.createOrgForCV(orgName);
    } catch (FileNotFoundException e) {
      log.info("File Not Found Exception");
    }
  }

  @Test(description = "Create project with unique name and default identifier")
  public void createProjectTest() throws FileNotFoundException {
    String projName = commonHelper.createRandomName(PROJ_NAME_PREFIX);
    projectIdentifier.add(projName);
    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", projectIdentifier.get(0));

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(defaultAccountId));

    appReqData.add("owners", jsonArray);
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Project for CVNG");

    JsonPath projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(0));
    Assert.assertTrue(projDetails.getString("data.modules").contains("CV"));
  }

  @Test(description = "Create project with unique name and custom identifier")
  public void createProjectCustIdentifierTest() throws FileNotFoundException {
    String projName = commonHelper.createRandomName(PROJ_NAME_PREFIX);
    projectIdentifier.add("custom_" + projName);
    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", projectIdentifier.get(0));

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(defaultAccountId));

    appReqData.add("owners", jsonArray);
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Project with custom identifier for CVNG");

    JsonPath projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(0));
    Assert.assertTrue(projDetails.getString("data.modules").contains("CV"));
  }

  @Test(description = "Create project with unique name, custom identifier, Tags and Descriptions")
  public void createProjectAllDetailsTest() throws FileNotFoundException {
    String projName = commonHelper.createRandomName(PROJ_NAME_PREFIX);
    projectIdentifier.add("custom_" + projName);
    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", projectIdentifier.get(0));
    appReqData.addProperty("description", "Project description for CVNG all details");
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    JsonArray ownerArray = new JsonArray();
    ownerArray.add(new JsonPrimitive(defaultAccountId));
    appReqData.add("owners", ownerArray);

    JsonArray tagsArray = new JsonArray();
    tagsArray.add(new JsonPrimitive("CVNG1"));
    tagsArray.add(new JsonPrimitive("CVNG2"));
    tagsArray.add(new JsonPrimitive("CVNG3"));
    appReqData.add("tags", tagsArray);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Project with all details for CVNG");

    JsonPath projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(0));
    Assert.assertTrue(projDetails.getString("data.modules").contains("CV"));
  }

  @Test(description = "Create project with duplicate name and unique identifier")
  public void createProjectDuplicateNameTest() throws FileNotFoundException {
    String projName = commonHelper.createRandomName(PROJ_NAME_PREFIX);
    projectIdentifier.add(projName);
    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", projectIdentifier.get(0));

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(defaultAccountId));

    appReqData.add("owners", jsonArray);
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Project for CVNG");

    JsonPath projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.name"), projName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(0));

    projectIdentifier.add("unique_" + projName);
    appReqData.addProperty("identifier", projectIdentifier.get(1));

    body = appReqData.toString();
    response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Project created with duplicate name and unique identifier");

    projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.name"), projName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(1));
  }

  @Test(description = "Create project with duplicate name and duplicate identifier and verify the error code")
  public void createProjectDuplicateIdentifierTest() throws FileNotFoundException {
    String projName = commonHelper.createRandomName(PROJ_NAME_PREFIX);
    projectIdentifier.add(projName);
    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", projectIdentifier.get(0));

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(defaultAccountId));

    appReqData.add("owners", jsonArray);
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Project for CVNG");

    JsonPath projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.name"), projName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(0));

    response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 400);
    log.info("Project creation failed");

    JsonPath error = response.jsonPath();
    String actualErrorMessage = response.jsonPath().getString("code");
    Assert.assertEquals(ERROR_MESSAGES.DUPLICATE_FIELD.toString(), actualErrorMessage,
        "Error thrown when the Project Identifier is duplicate");
    Assert.assertEquals(error.getString("message"),
        "Project [" + projectIdentifier.get(0) + "] under Organization [" + orgName + "] already exists");
  }

  @Test(description = "Update project details")
  public void updateProjectDetailsTest() throws FileNotFoundException {
    String projName = commonHelper.createRandomName(PROJ_NAME_PREFIX);
    projectIdentifier.add(projName);
    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", projectIdentifier.get(0));

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(defaultAccountId));

    appReqData.add("owners", jsonArray);
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Project for CVNG");

    JsonPath projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(0));
    Assert.assertEquals(projDetails.getString("data.name"), projName);
    Assert.assertTrue(projDetails.getString("data.modules").contains("CV"));

    appReqData.addProperty("name", "Updated_" + projName);

    JsonArray tagsArray = new JsonArray();
    tagsArray.add(new JsonPrimitive("CVNG1"));
    tagsArray.add(new JsonPrimitive("CVNG2"));
    tagsArray.add(new JsonPrimitive("CVNG3"));
    appReqData.add("tags", tagsArray);

    body = appReqData.toString();
    response = projectHelper.updateProjByIdentifiers(projectIdentifier.get(0), orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Updated Project details for CVNG");

    projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projName);
    Assert.assertEquals(projDetails.getString("data.name"), "Updated_" + projName);
    Assert.assertTrue(projDetails.getString("data.modules").contains("CV"));
    Assert.assertTrue(projDetails.getString("data.tags").contains("CVNG3"));
  }

  @Test(description = "Update project identifier when a new identifier is passed as body")
  public void updateProjectIdentifierTest() throws FileNotFoundException {
    String projName = commonHelper.createRandomName(PROJ_NAME_PREFIX);
    projectIdentifier.add(projName);
    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", projectIdentifier.get(0));

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(defaultAccountId));

    appReqData.add("owners", jsonArray);
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Project for CVNG");

    JsonPath projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("data.organizationName"), orgName);
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(0));
    Assert.assertEquals(projDetails.getString("data.name"), projName);
    Assert.assertTrue(projDetails.getString("data.modules").contains("CV"));

    appReqData.addProperty("name", "Updated_" + projName);
    appReqData.addProperty("identifier", "Updated_" + projName);

    body = appReqData.toString();
    response = projectHelper.updateProjByIdentifiers(projectIdentifier.get(0), orgName, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Updated Project details for CVNG");

    projDetails = response.jsonPath();
    Assert.assertEquals(projDetails.getString("status"), "SUCCESS");
    Assert.assertEquals(projDetails.getString("data.identifier"), projectIdentifier.get(0));
  }

  @Test(description = "Update project identifier")
  public void createProjectInvalidApiTest() throws FileNotFoundException {
    String projName = commonHelper.createRandomName(PROJ_NAME_PREFIX);

    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", "");

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(defaultAccountId));

    appReqData.add("owners", jsonArray);
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgName, body);
    Assert.assertEquals(response.statusCode(), 400);
    log.info("Create a Project with invalid API for CVNG");

    JsonPath error = response.jsonPath();
    String actualErrorMessage = response.jsonPath().getString("code");
    Assert.assertEquals(ERROR_MESSAGES.INVALID_REQUEST.toString(), actualErrorMessage,
        "Error thrown when the Project creation API is missing Identifier");
    Assert.assertEquals(error.getString("status"), "FAILURE");
    Assert.assertEquals(error.getString("validationErrors[0].field"), "identifier");
  }

  @AfterMethod
  public void cleanUpProject() {
    for (String projectID : projectIdentifier) {
      cvngHelper.deleteProjForCV(orgName, projectID);
    }
    projectIdentifier.clear();
  }

  @AfterClass
  public void cleanup() {
    cvngHelper.deleteOrgForCV(orgName);
  }
}
