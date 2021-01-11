package io.harness.test.rest.rbac;

import io.harness.rest.common.ERROR_MESSAGES;
import io.harness.rest.core.RestAssuredClient;
import io.harness.rest.helper.accessmanagement.users.UserGroupHelper;
import io.harness.rest.helper.accessmanagement.users.UserHelper;
import io.harness.rest.helper.secretmanager.SecretManagerConstants;
import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.harness.rest.utils.ConfigProperties;
import io.harness.rest.utils.SecretsProperties;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Test(groups = {"PL", "RBAC", "PR", "QA"})
public class UsageScopeWithoutEnvironmentTest extends AbstractTest {
  SecretsHelper secretsHelper = new SecretsHelper();
  RestAssuredClient restClient = new RestAssuredClient();
  UserHelper userHelper = new UserHelper();
  UserGroupHelper userGroupHelper = new UserGroupHelper();
  SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
  String defaultUserUserId = null;
  String defaultUser = null;
  String defaultUserPassword = null;
  Map<String, String> userGroupList = new HashMap<String, String>();
  Map<String, String> defaultSecretMap = new HashMap<String, String>();
  SecretsProperties secretsProperties = new SecretsProperties();
  ConfigProperties configPropertis = new ConfigProperties();

  @BeforeClass
  public void setup() {
    Map<String, String> usersList = userHelper.getUsersList();
    defaultUser = configPropertis.getConfig("RBAC_TEST_USER");
    defaultUserPassword = secretsProperties.getSecret("RBAC_TEST_USER_PASSWORD");
    defaultUserUserId = usersList.get(configPropertis.getConfig("RBAC_TEST_USER"));
    userGroupList = userGroupHelper.getUserGroupList();
    String defaultSecretManagerId =
        secretManagerHelper.getSecretManagerList().get(SecretManagerConstants.DEFAULT_GCP_SECRET_MANAGER);
    defaultSecretMap.put("kmsId", defaultSecretManagerId);
  }

  @AfterClass
  public void teardown() {
    secretsHelper.bearerToken = restClient.getBearerToken();
  }

  @Test
  public void adminUserCreateSecretWithNoEnvironment() {
    String secretName = commonHelper.createRandomName("secretText-");
    List<String> localUserGroupList = new ArrayList<>();
    localUserGroupList.add(userGroupList.get("Account Administrator"));
    userHelper.updateUserGroups(defaultUserUserId, localUserGroupList);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;
    defaultSecretMap.put("name", secretName);
    defaultSecretMap.put("value", secretName);
    Response response = secretsHelper.addSecretWithUsageScope(defaultSecretMap, null);
    Assert.assertEquals(response.getStatusCode(), 200);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "secret should be created successfully");
    Assert.assertTrue(
        secretsHelper.isSecretExist(secretName), "Secret should be created as expected and it should list in secrets ");
    Map<String, String> secretProperties = secretsHelper.getSecretProperties(secretName);
    Assert.assertEquals(secretProperties.get("scopedToAccount"), false, "scope to account should be false");
    secretsHelper.deleteSecret(secretId);
    Assert.assertFalse(secretsHelper.isSecretExist(secretName), "Secret should be deleted successfully ");
  }

  @Test
  public void readOnlyUserCreateSecretWithNoEnvironment() {
    String secretName = commonHelper.createRandomName("secretText-");
    userHelper.updateUserGroups(defaultUserUserId, null);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;
    defaultSecretMap.put("name", secretName);
    defaultSecretMap.put("value", secretName);
    Response response = secretsHelper.addSecretWithUsageScope(defaultSecretMap, null);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.USER_NOT_AUTHORIZED_DUE_TO_USAGE_RESTRICTIONS.toString(), actualErrorMessage,
        "Error should thrown when user does not have access");
    restClient.logoutUser(defaultUserUserId, bearerToken, defaultAccountId);
  }

  @Test
  public void specificAppUserCreateSecretWithNoEnvironment() {
    String secretName = commonHelper.createRandomName("secretText-");
    List<String> localUserGroupList = new ArrayList<>();
    localUserGroupList.add(userGroupList.get("SpecificAppAllPermission"));
    userHelper.updateUserGroups(defaultUserUserId, localUserGroupList);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;

    defaultSecretMap.put("name", secretName);
    defaultSecretMap.put("value", secretName);
    Response response = secretsHelper.addSecretWithUsageScope(defaultSecretMap, null);

    Assert.assertEquals(response.getStatusCode(), 200);
    String secretId = response.jsonPath().getString("resource");

    Assert.assertNotNull(secretId, "secret should be created successfully");
    Assert.assertTrue(
        secretsHelper.isSecretExist(secretName), "Secret should be created as expected and it should list in secrets ");
    Map<String, String> secretProperties = secretsHelper.getSecretProperties(secretName);
    Assert.assertEquals(secretProperties.get("scopedToAccount"), false, "scope to account should be false");
    secretsHelper.deleteSecret(secretId);
  }

  @Test
  public void createSecretWithReadOnlyUser() {
    userHelper.updateUserGroups(defaultUserUserId, null);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;
    String secretName = commonHelper.createRandomName("secretText-");
    Response response = secretsHelper.addSecretText(secretName, secretName);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.USER_NOT_AUTHORIZED_DUE_TO_USAGE_RESTRICTIONS.toString(), actualErrorMessage,
        "Error should thrown when user does not have access");
    restClient.logoutUser(defaultUserUserId, bearerToken, defaultAccountId);
  }

  @Test
  public void createSecretWithAdminUser() {
    List<String> localUserGroupList = new ArrayList<>();
    localUserGroupList.add(userGroupList.get("Account Administrator"));
    userHelper.updateUserGroups(defaultUserUserId, localUserGroupList);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    String secretName = commonHelper.createRandomName("secretText-");
    secretsHelper.bearerToken = bearerToken;
    Response response = secretsHelper.addSecretText(secretName, secretName);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret should be created successfully");
    secretsHelper.deleteSecret(secretId);
    restClient.logoutUser(defaultUserUserId, bearerToken, defaultAccountId);
  }

  @Test
  public void createAccountSecretWithReadOnlyUser() {
    String secretName = commonHelper.createRandomName("secretText-");
    userHelper.updateUserGroups(defaultUserUserId, null);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;
    Response response = secretsHelper.addScopeToAccountSecret(secretName, secretName);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.NOT_ACCOUNT_MGR_NOR_HAS_ALL_APP_ACCESS.toString(), actualErrorMessage,
        "Error should thrown when user does not have access");
    restClient.logoutUser(defaultUserUserId, bearerToken, defaultAccountId);
  }

  @Test
  public void createAccountSecretWithAdminUser() {
    String secretName = commonHelper.createRandomName("secretText-");
    List<String> localUserGroupList = new ArrayList<String>();
    localUserGroupList.add(userGroupList.get("Account Administrator"));
    userHelper.updateUserGroups(defaultUserUserId, localUserGroupList);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;
    Response response = secretsHelper.addScopeToAccountSecret(secretName, secretName);
    Assert.assertEquals(response.getStatusCode(), 200);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId);
    Map<String, String> secretProperties = secretsHelper.getSecretProperties(secretName);
    Assert.assertEquals(secretProperties.get("scopedToAccount"), true, "scope to account should be true");
    secretsHelper.deleteSecret(secretId);
    restClient.logoutUser(defaultUserUserId, bearerToken, defaultAccountId);
  }

  @Test
  public void createAccountSecretWithAllAppPermission() {
    String secretName = commonHelper.createRandomName("secretText-");
    List<String> localUserGroupList = new ArrayList<String>();
    localUserGroupList.add(userGroupList.get("AllApplicationPermissionOnly"));
    userHelper.updateUserGroups(defaultUserUserId, localUserGroupList);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;
    Response response = secretsHelper.addScopeToAccountSecret(secretName, secretName);
    Assert.assertEquals(response.getStatusCode(), 200);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId);
    Map<String, String> secretProperties = secretsHelper.getSecretProperties(secretName);
    Assert.assertEquals(secretProperties.get("scopedToAccount"), true, "scope to account should be true");
    secretsHelper.deleteSecret(secretId);
    restClient.logoutUser(defaultUserUserId, bearerToken, defaultAccountId);
  }

  @Test
  public void createAccountSecretWithAllAppAllEnvPermissionOnly() {
    String secretName = commonHelper.createRandomName("secretText-");
    List<String> localUserGroupList = new ArrayList<String>();
    localUserGroupList.add(userGroupList.get("AppCreationAndAllEnvironmentUpdate"));
    userHelper.updateUserGroups(defaultUserUserId, localUserGroupList);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;
    Response response = secretsHelper.addScopeToAccountSecret(secretName, secretName);
    Assert.assertEquals(response.getStatusCode(), 200);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId);
    Map<String, String> secretProperties = secretsHelper.getSecretProperties(secretName);
    Assert.assertEquals(secretProperties.get("scopedToAccount"), true, "scope to account should be true");
    secretsHelper.deleteSecret(secretId);
    restClient.logoutUser(defaultUserUserId, bearerToken, defaultAccountId);
  }

  @Test
  public void createAccountSecretWithSpecificAppOnly() {
    String secretName = commonHelper.createRandomName("secretText-");
    List<String> localUserGroupList = new ArrayList<String>();
    localUserGroupList.add(userGroupList.get("SpecificAppAllPermission"));
    userHelper.updateUserGroups(defaultUserUserId, localUserGroupList);
    String bearerToken = restClient.loginUser(defaultUser, defaultUserPassword);
    secretsHelper.bearerToken = bearerToken;
    Response response = secretsHelper.addScopeToAccountSecret(secretName, secretName);
    String actualErrorMessage = response.jsonPath().getString("responseMessages[0].code");
    Assert.assertEquals(ERROR_MESSAGES.NOT_ACCOUNT_MGR_NOR_HAS_ALL_APP_ACCESS.toString(), actualErrorMessage,
        "Error should thrown when user does not have access");
    restClient.logoutUser(defaultUserUserId, bearerToken, defaultAccountId);
  }
}
/*
  This automation is for PL-10521, It checks environment deletion when secret is referenced by specific environment
 */
