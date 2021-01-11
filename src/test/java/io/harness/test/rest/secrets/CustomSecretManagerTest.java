package io.harness.test.rest.secrets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.accessmanagement.users.UserHelper;
import io.harness.rest.helper.secretmanager.SecretManagerConstants;
import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.harness.rest.helper.secretmanager.SecretManagerType;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.harness.rest.helper.templatelibrary.TemplateLibraryHelper;
import io.harness.rest.utils.JsonHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Test(groups = {"PL", "Secrets", "PR", "QA"})
public class CustomSecretManagerTest extends AbstractTest {
  SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
  TemplateLibraryHelper templateLibraryHelper = new TemplateLibraryHelper();
  SecretsHelper secretsHelper = new SecretsHelper();
  JsonHelper jsonHelper = new JsonHelper();
  UserHelper userHelper = new UserHelper();

  @BeforeClass
  public void setupSM() {}

  @Test
  public void TestAdditionOfCustomSM() {
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_no_var");
    String customsm = commonHelper.createRandomName("customsm");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    Response response = secretManagerHelper.createCustomSM(customsm, secretProperties);
    Assert.assertTrue(response.getStatusCode() == 200, "Custom secret manager should be created successfully");
    String secretManagerId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretManagerId, "Custom secret manager should be created successfully");
    response = secretManagerHelper.deleteSecretManagerById(secretManagerId, SecretManagerType.CUSTOM_SM);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  public void TestDuplicateAdditionOfCustomSM() {
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_no_var");
    String customsm = commonHelper.createRandomName("customsm");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    Response response = secretManagerHelper.createCustomSM(customsm, secretProperties);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    response = secretManagerHelper.createCustomSM(customsm, secretProperties);
    Assert.assertEquals(400, response.getStatusCode(), "Duplicate secret manager should give proper error");
    String errorMessage = response.jsonPath().getString("responseMessages[0].message");
    Assert.assertTrue(errorMessage.contains("Could not save the custom secrets manager with name"),
        "proper error message should return from be");
  }

  @Test
  public void TestAdditionOfCustomSMWithParam() {
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_with_var");
    String customsm = commonHelper.createRandomName("customsm");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    HashMap<String, String> paramMap = new HashMap<>();
    paramMap.put("path", "guna/SECRET_TEXT/newtest");
    paramMap.put("key", "value");
    paramMap.put("engineName", "harness-test");
    JsonArray paramArray = jsonHelper.getJsonArrayForCustomSecretManager(paramMap);
    secretProperties.put("testVariables", paramArray);
    Response response = secretManagerHelper.createCustomSM(customsm, secretProperties);
    Assert.assertTrue(
        response.getStatusCode() == 200, "Custom secret manager should be created successfully with Params");
    String secretManagerId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretManagerId, "Custom secret manager should be created successfully with params");
    response = secretManagerHelper.deleteSecretManagerById(secretManagerId, SecretManagerType.CUSTOM_SM);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  public void TestCustomSMWithInvalidParam() {
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_with_var");
    String customsm = commonHelper.createRandomName("customsm");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    HashMap<String, String> paramMap = new HashMap<>();
    paramMap.put("path", "guna/SECRET_TEXT/newtest1");
    paramMap.put("key", "value");
    paramMap.put("engineName", "harness-test");
    JsonArray paramArray = jsonHelper.getJsonArrayForCustomSecretManager(paramMap);
    secretProperties.put("testVariables", paramArray);
    Response response = secretManagerHelper.createCustomSM(customsm, secretProperties);
    Assert.assertTrue(response.getStatusCode() == 400, "Custom secret manager should throw proper error");
    Assert.assertTrue(response.jsonPath()
                          .getString("responseMessages[0].message")
                          .equals("Empty or null value returned by custom shell script for Test Variables"),
        "Custom secret manager should throw proper error");
  }

  @Test
  public void TestCustomSMWithEmptyParam() {
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_with_var");
    String customsm = commonHelper.createRandomName("customsm");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    HashMap<String, String> paramMap = new HashMap<>();
    paramMap.put("path", "");
    paramMap.put("key", "");
    paramMap.put("engineName", "");
    JsonArray paramArray = jsonHelper.getJsonArrayForCustomSecretManager(paramMap);
    secretProperties.put("testVariables", paramArray);
    Response response = secretManagerHelper.createCustomSM(customsm, secretProperties);
    Assert.assertTrue(response.getStatusCode() == 400, "Custom secret manager should throw proper error");
    Assert.assertTrue(response.jsonPath()
                          .getString("responseMessages[0].message")
                          .equals("Empty or null value returned by custom shell script for Test Variables"),
        "Custom secret manager should throw proper error");
  }

  @Test
  public void TestEditOfCustomSMWithParam() {
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_no_var");
    String customsm = commonHelper.createRandomName("customsm");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    Response response = secretManagerHelper.createCustomSM(customsm, secretProperties);
    Assert.assertTrue(response.getStatusCode() == 200, "Custom secret manager should be created successfully");
    String secretManagerId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretManagerId, "Custom secret manager should be created successfully");
    templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_with_var");
    secretProperties.put("templateId", templateId);
    HashMap<String, String> paramMap = new HashMap<>();
    paramMap.put("path", "guna/SECRET_TEXT/newtest");
    paramMap.put("key", "value");
    paramMap.put("engineName", "harness-test");
    JsonArray paramArray = jsonHelper.getJsonArrayForCustomSecretManager(paramMap);
    secretProperties.put("testVariables", paramArray);
    response = secretManagerHelper.editCustomSecretManager(customsm, secretManagerId, secretProperties);
    Assert.assertTrue(response.getStatusCode() == 200, "Custom secret manager should be edited successfully");
    secretManagerId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretManagerId, "Custom secret manager should be created successfully");
    response = secretManagerHelper.deleteSecretManagerById(secretManagerId, SecretManagerType.CUSTOM_SM);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  public void addSecretToCustomSecretManagerWithMissedVar() {
    String customSecreWithoutVar = commonHelper.createRandomName("csmvar");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("CustomSecretManager");
    HashMap<String, String> paramMap = new HashMap<>();
    paramMap.put("path", "guna/SECRET_TEXT/newtest");
    paramMap.put("key", "value");
    JsonArray paramArray = jsonHelper.getJsonArrayForCustomSecretManager(paramMap);
    HashMap<String, Object> properties = new HashMap<>();
    properties.put("parameters", paramArray);
    Response response = secretsHelper.addCustomSecret(customSecreWithoutVar, secretManagerId, properties);
    Assert.assertEquals(
        400, response.getStatusCode(), "secret creation should throw proper error code when invalid details passed");
    String errorMessage = response.jsonPath().getString("responseMessages[0].message");
    Assert.assertTrue(
        errorMessage.equals(
            "Invalid argument(s): The values for the variables engineName have not been provided as part of test parameters"),
        "Secret should be created successfully");
  }

  @Test
  public void addSecretToCustomSecretManagerWithVar() {
    String customSecreWithoutVar = commonHelper.createRandomName("csmvar");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("CustomSecretManager");
    HashMap<String, String> paramMap = new HashMap<>();
    paramMap.put("path", "guna/SECRET_TEXT/newtest");
    paramMap.put("key", "value");
    paramMap.put("engineName", "harness-test");
    JsonArray paramArray = jsonHelper.getJsonArrayForCustomSecretManager(paramMap);
    HashMap<String, Object> properties = new HashMap<>();
    properties.put("parameters", paramArray);
    Response response = secretsHelper.addCustomSecret(customSecreWithoutVar, secretManagerId, properties);
    Assert.assertEquals(200, response.getStatusCode(), "secret creation should be success with custom Sm");
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret should be created successfully");
    Assert.assertTrue(secretsHelper.isSecretExist(customSecreWithoutVar, secretManagerId),
        "secret creation should be success with custom Sm");
    secretsHelper.deleteSecret(secretId);
    Assert.assertFalse(secretsHelper.isSecretExist(customSecreWithoutVar, secretManagerId),
        "secret creation should be deleted successfully");
  }

  @Test
  public void addSecretToCustomSecretManagerWithoutVarForVarExpectation() {
    String customSecreWithoutVar = commonHelper.createRandomName("csmvar");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("CustomSecretManager");
    Response response = secretsHelper.addCustomSecret(customSecreWithoutVar, secretManagerId, null);
    Assert.assertEquals(400, response.getStatusCode(), "secret creation should thrown proper error");
    String errorMessage = response.jsonPath().getString("responseMessages[0].message");
    Assert.assertTrue(
        errorMessage.equals(
            "Invalid argument(s): The values for the variables path, engineName, key have not been provided as part of test parameters"),
        "Secret should be created successfully");
  }

  @Test
  public void addSecretToCustomSecretManagerWithoutVar() {
    String customSecreWithoutVar = commonHelper.createRandomName("csmvar");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("CustomSMWithNoVar");
    Response response = secretsHelper.addCustomSecret(customSecreWithoutVar, secretManagerId, null);
    Assert.assertEquals(200, response.getStatusCode(), "secret creation should be success without params");
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret should be created successfully without params");
    Assert.assertTrue(secretsHelper.isSecretExist(customSecreWithoutVar, secretManagerId), "secret should be exist");
    secretsHelper.deleteSecret(secretId);
    Assert.assertFalse(secretsHelper.isSecretExist(customSecreWithoutVar, secretManagerId), "secret should be exist");
  }

  @Test
  public void testDeletionOfTemplateWhenUsedInSecretManager() {
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_no_var");
    String customsm = commonHelper.createRandomName("customsm");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    Response response = secretManagerHelper.createCustomSM(customsm, secretProperties);
    Assert.assertTrue(response.getStatusCode() == 200, "Custom secret manager should be created successfully");
    String secretManagerId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretManagerId, "Custom secret manager should be created successfully");
    response = templateLibraryHelper.deleteTemplateLibrary("csm_automation_no_var");
    Assert.assertTrue(response.getStatusCode() == 400, "Template library should not delete");
    String errorMessage = response.jsonPath().getString("responseMessages[0].message");
    Assert.assertTrue(errorMessage.contains("Reason: SHELL_SCRIPT template(s) linked to WORKFLOW or SECRETS_MANAGER"),
        "template library should not be deleted");
    response = secretManagerHelper.deleteSecretManagerById(secretManagerId, SecretManagerType.CUSTOM_SM);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  public void testSecretManagerWithReadOnlyUser() {
    String defaultUser = configPropertis.getConfig("RBAC_TEST_USER");
    String defaultUserPassword = secretsProperties.getSecret("RBAC_TEST_USER_PASSWORD");
    Map<String, String> usersList = userHelper.getUsersList();
    String defaultUserUserId = usersList.get(configPropertis.getConfig("RBAC_TEST_USER"));
    userHelper.updateUserGroups(defaultUserUserId, null);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_no_var");
    String customsm = commonHelper.createRandomName("customsm");
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("executeOnDelegate", true);
    jsonObject.addProperty("name", customsm);
    jsonObject.addProperty("templateId", templateId);
    jsonObject.add("delegateSelectors", new JsonArray());
    jsonObject.add("testVariables", new JsonArray());
    requestSpecification.auth().oauth2(bearerToken);
    requestSpecification.body(jsonObject.toString());
    Response response = GenericRequestBuilder.putCall(requestSpecification, SecretManagerConstants.ADD_CUSTOM_SM);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].message");
    Assert.assertEquals(response.getStatusCode(), 400, "secret manager should not create with read only user");
    Assert.assertTrue(actualErrorMessage.equals("Invalid request: User not authorized"),
        "Error should thrown when user does not have access");
  }

  @Test
  public void testMigrationShouldNotSupportBetweenCustomSM() {
    String secretManagerIdWithVar = secretManagerHelper.getSecretManagerList().get("CustomSecretManager");
    String secretManagerIdWithoutVar = secretManagerHelper.getSecretManagerList().get("CustomSMWithNoVar");
    HashMap<String, String> migrationProperties = new HashMap<>();
    ;
    migrationProperties.put("fromEncryptionType", "CUSTOM");
    migrationProperties.put("fromKmsId", secretManagerIdWithVar);
    migrationProperties.put("toEncryptionType", "CUSTOM");
    migrationProperties.put("toKmsId", secretManagerIdWithoutVar);
    Response response = secretManagerHelper.migrateSecretManagers(migrationProperties);
    Assert.assertEquals(response.getStatusCode(), 400, "Migration should not supported");
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].message");
    Assert.assertTrue(
        actualErrorMessage.equals("Operation not supported: Cannot transfer secret to or from a custom secret manager"),
        "Error should thrown when migration is not supported");
  }
}
