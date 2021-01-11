package io.harness.rest.helper.cvnextgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.helper.ng.environments.EnvironmentHelper;
import io.harness.rest.helper.ng.organizations.OrgConstants;
import io.harness.rest.helper.ng.organizations.OrganizationHelper;
import io.harness.rest.helper.ng.projects.ProjectConstants;
import io.harness.rest.helper.ng.projects.ProjectHelper;
import io.harness.rest.helper.ng.services.ServiceHelper;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * author: shaswat.deep
 */
@Slf4j
public class CVNGHelper extends CoreUtils {
  ProjectHelper projectHelper = new ProjectHelper();
  OrganizationHelper organizationHelper = new OrganizationHelper();
  EnvironmentHelper environmentHelper = new EnvironmentHelper();
  ServiceHelper serviceHelper = new ServiceHelper();

  public void createOrgForCV(String name) throws FileNotFoundException {
    JsonObject appReqData = jReader.readJSONFiles(OrgConstants.REQUEST_JSON_CV_ORG_DETAILS);
    appReqData.addProperty("name", name);
    appReqData.addProperty("identifier", name);

    String body = appReqData.toString();
    Response response = organizationHelper.createOrg(defaultAccountId, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created an org for CVNG");
  }

  public void deleteOrgForCV(String name) {
    Response response = organizationHelper.deleteOrgByIdentifiers(defaultAccountId, name);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Org cleanup after CV Test");
  }

  public void createProjectForCV(String orgIdentifier, String projName) throws FileNotFoundException {
    JsonObject appReqData = jReader.readJSONFiles(ProjectConstants.REQUEST_JSON_CV_PROJ_DETAILS);
    appReqData.addProperty("name", projName);
    appReqData.addProperty("identifier", projName);
    appReqData.addProperty("accountIdentifier", defaultAccountId);

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(defaultAccountId));
    appReqData.add("owners", jsonArray);

    String body = appReqData.toString();
    Response response = projectHelper.createProject(orgIdentifier, body);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Project for CVNG");
  }

  public void deleteProjForCV(String orgIdentifier, String projIdentifier) {
    Response response = projectHelper.deleteProjByIdentifiers(projIdentifier, orgIdentifier);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Project cleanup after CV Test");
  }

  public void createServiceForCV(String orgIdentifier, String projectIdentifier, String serviceIdentifier)
      throws FileNotFoundException {
    Map<String, Object> map = new HashMap<>();
    map.put("identifier", serviceIdentifier);
    map.put("orgIdentifier", orgIdentifier);
    map.put("projectIdentifier", projectIdentifier);
    map.put("name", serviceIdentifier);

    Response response = serviceHelper.postService(map);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Service for CV Test");
  }

  public void deleteServiceForCV(String serviceIdentifier) {
    Response response = serviceHelper.deleteService(serviceIdentifier);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Service cleanup after CV Test");
  }

  public void createEnvironmentForCV(String orgIdentifier, String projectIdentifier, String envIdentifier)
      throws FileNotFoundException {
    Map<String, Object> map = new HashMap<>();
    map.put("identifier", envIdentifier);
    map.put("orgIdentifier", orgIdentifier);
    map.put("projectIdentifier", projectIdentifier);
    map.put("name", envIdentifier);

    Response response = serviceHelper.postService(map);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Created a Environment for CV Test");
  }

  public void deleteEnvironmentForCV(String envIdentifier) {
    Response response = environmentHelper.deleteEnv(envIdentifier);
    Assert.assertEquals(response.statusCode(), 200);
    log.info("Environment cleanup after CV Test");
  }
}