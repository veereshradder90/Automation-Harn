package io.harness.test.rest.secrets;

import com.google.gson.JsonElement;

import io.harness.rest.helper.UsageScope.UsageScopeHelper;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.harness.rest.helper.services.ServiceHelper;
import io.harness.rest.helper.templatelibrary.TemplateLibraryHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Test(groups = {"PL", "Secrets", "PR", "QA"})
public class CustomSecretManagerFeatureValidation extends AbstractTest {
  ApplicationHelper applicationHelper = new ApplicationHelper();
  ServiceHelper serviceHelper = new ServiceHelper();
  SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
  SecretsHelper secretsHelper = new SecretsHelper();
  UsageScopeHelper usageScopeHelper = new UsageScopeHelper();
  TemplateLibraryHelper templateLibraryHelper = new TemplateLibraryHelper();
  String rbacApplicationId = null;
  String nonRbacApplicationId = null;
  String templateId = null;

  @BeforeClass
  public void setUp() {
    rbacApplicationId = applicationHelper.getApplicationId("RBAC Application").getString("resource.response[0].appId");
    nonRbacApplicationId = applicationHelper.getApplicationId("CVMockServer").getString("resource.response[0].appId");
    templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_no_var");
  }

  @DataProvider(name = "secretManagers")
  public Object[][] getSecretManager() {
    Object[][] obj = new Object[1][1];
    obj[0][0] = "CustomSMWithNoVar";
    return obj;
  }

  @Test(dataProvider = "secretManagers")
  public void testChangeLogForSecret(String secretManagerName) {
    String customSecreWithoutVar = commonHelper.createRandomName("csmvar");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get(secretManagerName);
    Response response = secretsHelper.addCustomSecret(customSecreWithoutVar, secretManagerId, null);
    String secretId = response.jsonPath().getString("resource");
    Map<String, String> editingValues = new HashMap<>();
    editingValues.put("name", customSecreWithoutVar);
    secretsHelper.editSecret(customSecreWithoutVar, editingValues);
    ArrayList<HashMap> changeLogs = secretsHelper.getChangeLog(customSecreWithoutVar, "SECRET_TEXT");
    Assert.assertEquals(2, changeLogs.size(), "Two change logs should exist");
    Assert.assertEquals("Changed usage restrictions", changeLogs.get(0).get("description").toString().trim(),
        "Two change logs should exist");
    Assert.assertEquals(
        "Created", changeLogs.get(1).get("description").toString().trim(), "Two change logs should exist");
    secretsHelper.deleteSecret(secretId);
  }

  @Test(dataProvider = "secretManagers")
  public void testSetupUsageForSecret(String secretManagerName) throws InterruptedException {
    String secretText = commonHelper.createRandomName("csmvar");
    String serviceName = commonHelper.createRandomName("secretservice");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get(secretManagerName);
    Response response = secretsHelper.addCustomSecret(secretText, secretManagerId, null);
    String secretId = response.jsonPath().getString("resource");
    String appId = applicationHelper.getApplicationId("RBAC Application").getString("resource.response[0].appId");
    JsonPath serviceResponse = serviceHelper.createK8SV2Service(serviceName, appId);
    String serviceId = serviceResponse.getString("resource.uuid");
    Map<String, String> variableMap = new HashMap<>();
    variableMap.put("appId", appId);
    variableMap.put("entityId", serviceId);
    variableMap.put("type", "ENCRYPTED_TEXT");
    variableMap.put("name", "dummy");
    variableMap.put("value", secretId);
    serviceHelper.addServiceVariable(variableMap);
    ArrayList<HashMap> setupUsage = secretsHelper.getSetupUsage(secretText, "SECRET_TEXT");
    Assert.assertEquals(1, setupUsage.size(), "Setup usage exist when secret used");
    HashMap entityDetaild = (HashMap) setupUsage.get(0).get("entity");
    Assert.assertEquals("encryptedValue", setupUsage.get(0).get("fieldName").toString().trim(),
        "field name should match to encrypted value");
    Assert.assertEquals(
        "SERVICE_VARIABLE", setupUsage.get(0).get("type").toString().trim(), "Service variable reference should exist");
    Assert.assertEquals(serviceId, entityDetaild.get("entityId"), "service reference should exist");
    Assert.assertEquals(appId, entityDetaild.get("appId"), "application reference should exist");
    serviceHelper.deleteService(serviceId, appId);
    Thread.sleep(5000);
    secretsHelper.deleteSecret(secretId);
  }

  @Test(dataProvider = "secretManagers")
  public void testSecretDeleteWhenItUsageScopeExist(String secretManagerName) throws InterruptedException {
    String secretText = commonHelper.createRandomName("csmvar");
    String serviceName = commonHelper.createRandomName("secretservice");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get(secretManagerName);
    Response response = secretsHelper.addCustomSecret(secretText, secretManagerId, null);
    String secretId = response.jsonPath().getString("resource");
    String appId = applicationHelper.getApplicationId("RBAC Application").getString("resource.response[0].appId");
    JsonPath serviceResponse = serviceHelper.createK8SV2Service(serviceName, appId);
    String serviceId = serviceResponse.getString("resource.uuid");
    Map<String, String> variableMap = new HashMap<>();
    variableMap.put("appId", appId);
    variableMap.put("entityId", serviceId);
    variableMap.put("type", "ENCRYPTED_TEXT");
    variableMap.put("name", "dummy");
    variableMap.put("value", secretId);
    serviceHelper.addServiceVariable(variableMap);
    ArrayList<HashMap> setupUsage = secretsHelper.getSetupUsage(secretText, "SECRET_TEXT");
    Assert.assertEquals(1, setupUsage.size(), "Setup usage exist when secret used");
    response = secretsHelper.deleteSecret(secretId);
    System.out.println(response.getBody().asString());
    Assert.assertEquals(400, response.getStatusCode(), "Secret should not delete when reference exist");
    Assert.assertTrue("Can not delete secret because it is still being used. See setup usage(s) of the secret.".equals(
        response.jsonPath().getString("responseMessages[0].message")));
    serviceHelper.deleteService(serviceId, appId);
    Thread.sleep(5000);
    response = secretsHelper.deleteSecret(secretId);
    Assert.assertEquals(200, response.getStatusCode(), "Secret should be delete after reference is removed");
  }

  @Test(dataProvider = "secretManagers")
  public void testSecretListingForSpecificApplication(String secretManagerName) {
    String secretManagerId = secretManagerHelper.getSecretManagerList().get(secretManagerName);
    String secretText = commonHelper.createRandomName("csm");
    String secretValue = commonHelper.createRandomName("test-value");
    Map<String, Object> defaultSecretMap = new HashMap<String, Object>();
    defaultSecretMap.put("kmsId", secretManagerId);
    defaultSecretMap.put("name", secretText);
    defaultSecretMap.put("value", secretValue);
    ArrayList<String> appList = new ArrayList<>();
    appList.add(rbacApplicationId);
    JsonElement usageScope = usageScopeHelper.getSpecificApplicationUsageScope(appList, "ALL");
    defaultSecretMap.put("usageRestrictions", usageScope);
    Response response = secretsHelper.addCustomSecret(secretText, secretManagerId, defaultSecretMap);
    String secretId = response.jsonPath().getString("resource");
    ArrayList<HashMap> variableSearchResult =
        secretsHelper.searchRegexSecretForApplication(secretText, "SECRET_TEXT", rbacApplicationId);
    Assert.assertNotNull(variableSearchResult, "Result set should not be null");
    Assert.assertEquals(1, variableSearchResult.size(), "Variable search should return 1 secret");
    Assert.assertTrue(variableSearchResult.get(0).get("name").equals(secretText), "proper variable should return");
    variableSearchResult =
        secretsHelper.searchRegexSecretForApplication(secretText, "SECRET_TEXT", nonRbacApplicationId);
    Assert.assertEquals(0, variableSearchResult.size(), "Variable search should not return for other application");
    secretsHelper.deleteSecret(secretId);
  }
}
