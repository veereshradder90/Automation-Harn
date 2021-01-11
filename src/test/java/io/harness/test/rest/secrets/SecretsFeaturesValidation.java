package io.harness.test.rest.secrets;

import com.google.gson.JsonElement;

import io.harness.rest.helper.UsageScope.UsageScopeHelper;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.secretmanager.SecretManagerConstants;
import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.harness.rest.helper.services.ServiceHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Test(groups = {"PL", "Secrets", "PR", "QA"})
public class SecretsFeaturesValidation extends AbstractTest {
  ApplicationHelper applicationHelper = new ApplicationHelper();
  ServiceHelper serviceHelper = new ServiceHelper();
  SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
  SecretsHelper secretsHelper = new SecretsHelper();
  UsageScopeHelper usageScopeHelper = new UsageScopeHelper();
  String rbacApplicationId = null;
  String nonRbacApplicationId = null;

  @BeforeClass
  public void setUp() {
    rbacApplicationId = applicationHelper.getApplicationId("RBAC Application").getString("resource.response[0].appId");
    nonRbacApplicationId = applicationHelper.getApplicationId("CVMockServer").getString("resource.response[0].appId");
  }

  @DataProvider(name = "secretManagers")
  public Object[][] getSecretManager() {
    Object[][] obj = new Object[4][1];
    obj[0][0] = SecretManagerConstants.VAULT_SECRET_MANAGER;
    obj[1][0] = SecretManagerConstants.DEFAULT_GCP_SECRET_MANAGER;
    obj[2][0] = SecretManagerConstants.GOOGKE_KMS;
    obj[3][0] = SecretManagerConstants.AZURE_SECRET_MANAGER;
    return obj;
  }

  @Test(dataProvider = "secretManagers")
  public void testChangeLogForSecret(String secretManagerName) {
    String secretText = commonHelper.createRandomName("vaultsm");
    String secretValue = commonHelper.createRandomName("test-value");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get(secretManagerName);
    Response response = secretsHelper.addSecretText(secretText, secretValue, secretManagerId);
    String secretId = response.jsonPath().getString("resource");
    Map<String, String> editingValues = new HashMap<>();
    editingValues.put("name", secretText);
    editingValues.put("value", secretValue + "dummy");
    secretsHelper.editSecret(secretText, editingValues);
    ArrayList<HashMap> changeLogs = secretsHelper.getChangeLog(secretText, "SECRET_TEXT");
    Assert.assertEquals(2, changeLogs.size(), "Two change logs should exist");
    Assert.assertEquals("Changed value & usage restrictions", changeLogs.get(0).get("description").toString().trim(),
        "Two change logs should exist");
    Assert.assertEquals(
        "Created", changeLogs.get(1).get("description").toString().trim(), "Two change logs should exist");
    secretsHelper.deleteSecret(secretId);
  }

  @Test(dataProvider = "secretManagers")
  public void testSetupUsageForSecret(String secretManagerName) throws InterruptedException {
    String secretText = commonHelper.createRandomName("vaultsm");
    String secretValue = commonHelper.createRandomName("test-value");
    String serviceName = commonHelper.createRandomName("secretservice");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get(secretManagerName);
    Response response = secretsHelper.addSecretText(secretText, secretValue, secretManagerId);
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
    String secretText = commonHelper.createRandomName("vaultsm");
    String secretValue = commonHelper.createRandomName("test-value");
    String serviceName = commonHelper.createRandomName("secretservice");
    String secretManagerId = secretManagerHelper.getSecretManagerList().get(secretManagerName);
    Response response = secretsHelper.addSecretText(secretText, secretValue, secretManagerId);
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
    String secretText = commonHelper.createRandomName("vaultsm");
    String secretValue = commonHelper.createRandomName("test-value");
    Map<String, String> defaultSecretMap = new HashMap<String, String>();
    defaultSecretMap.put("kmsId", secretManagerId);
    defaultSecretMap.put("name", secretText);
    defaultSecretMap.put("value", secretValue);
    ArrayList<String> appList = new ArrayList<>();
    appList.add(rbacApplicationId);
    JsonElement usageScope = usageScopeHelper.getSpecificApplicationUsageScope(appList, "ALL");
    Response response = secretsHelper.addSecretWithUsageScope(defaultSecretMap, usageScope);
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

  @Test(dataProvider = "secretManagers")
  public void testSecretListingWhenSecretCreatedWithNoUsageScope(String secretManagerName) {
    String defaultSecretManagerId = secretManagerHelper.getSecretManagerList().get(secretManagerName);
    String secretName = commonHelper.createRandomName("secretText-");
    Map<String, String> defaultSecretMap = new HashMap<String, String>();
    defaultSecretMap.put("name", secretName);
    defaultSecretMap.put("value", secretName);
    defaultSecretMap.put("kmsId", defaultSecretManagerId);
    Response response = secretsHelper.addSecretWithUsageScope(defaultSecretMap, null);
    String secretId = response.jsonPath().getString("resource");
    ArrayList<HashMap> variableSearchResult =
        secretsHelper.searchRegexSecretForApplication(secretName, "SECRET_TEXT", rbacApplicationId);
    Assert.assertEquals(0, variableSearchResult.size(), "Variable search should not return for other application");
    secretsHelper.deleteSecret(secretId);
  }
}
