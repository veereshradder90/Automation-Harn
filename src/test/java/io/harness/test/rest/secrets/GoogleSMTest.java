package io.harness.test.rest.secrets;

import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.harness.rest.helper.secretmanager.SecretManagerType;
import io.harness.rest.helper.secrets.SecretConstants;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Test(groups = {"PL", "Secrets", "PR", "QA", "PROD"})
public class GoogleSMTest extends AbstractTest {
  SecretsHelper secretsHelper = new SecretsHelper();
  SecretsValidationUtil secretsValidationUtil = new SecretsValidationUtil();
  SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
  String secretManagerId = null;
  String secretNamePrefix = "test-secret";
  String google_kms = null;

  @BeforeClass
  public void setupSM() {
    google_kms = commonHelper.createRandomName("gcpVault");
    Response response = secretManagerHelper.createGoogleKMS(google_kms);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be created successfully");
    secretManagerId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretManagerId, "Vault sm shuold not be null");
    log.info("vault sm id is : " + secretManagerId);
  }

  @Test
  public void testAdditionOfInlineSecretToVault() {
    secretsValidationUtil.validateSecretAddition(secretManagerId);
  }

  @Test
  public void testEditOfInlineSecretToVault() {
    String secretText = commonHelper.createRandomName(secretNamePrefix);
    String secretValue = commonHelper.createRandomName("test-value");
    String editedSecretText = secretText + "_edited";
    Map<String, String> editingValues = new HashMap<>();
    editingValues.put("name", editedSecretText);
    editingValues.put("value", secretValue);
    secretsValidationUtil.validateSecretEdit(secretText, secretValue, editingValues, secretManagerId);
  }

  @Test
  public void testSearchForSecret() {
    String secretText = commonHelper.createRandomName(secretNamePrefix);
    String secretValue = commonHelper.createRandomName("test-value");
    Response response = secretsHelper.addSecretText(secretText, secretValue, secretManagerId);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret should be created successfully");
    Map<String, String> secretProperties = secretsHelper.searchSecret(secretText, SecretConstants.SECRET_TEXT);
    Assert.assertEquals(secretText, secretProperties.get("name"), "Secret search should work as expected");
    secretsHelper.deleteSecret(secretId);
  }

  @Test
  public void testScopeToAccountSecret() {
    String secretText = commonHelper.createRandomName(secretNamePrefix);
    String secretValue = commonHelper.createRandomName("test-value");
    Response response = secretsHelper.addScopeToAccountSecret(secretText, secretValue, secretManagerId);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "scope to account secret should be created successfully");
    Map<String, String> secretProperties = secretsHelper.searchSecret(secretText, SecretConstants.SECRET_TEXT);
    Assert.assertEquals(
        secretText, secretProperties.get("name"), "scope to account secret should be created successfully");
    Object scopedToAccount = secretProperties.get("scopedToAccount");
    Assert.assertTrue(Boolean.valueOf(scopedToAccount.toString()), "Secret should be deleted successfully");
    secretsHelper.deleteSecret(secretId);
  }

  @Test
  public void testSecretFileAddition() {
    String secretName = commonHelper.createRandomName(secretNamePrefix);
    Response response = secretsHelper.addSecretFile(secretName, "echo samplevalue", secretManagerId);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret file should be created successfully");
    Assert.assertTrue(
        secretsHelper.isSecretFileExist(secretName, secretManagerId), "Secret file should be created successfully");
    secretsHelper.deleteFile(secretId);
    Assert.assertTrue(
        !secretsHelper.isSecretFileExist(secretName, secretManagerId), "Secret file should be deleted successfully");
  }

  @Test
  public void testSecretFileEdit() {
    String secretName = commonHelper.createRandomName(secretNamePrefix);
    String editedSecretName = secretName + "_edit";
    Response response = secretsHelper.addSecretFile(secretName, "echo samplevalue", secretManagerId);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret file should be created successfully");
    secretsHelper.editSecretFile(secretName, editedSecretName, "echo samplevalueedited");
    Assert.assertTrue(secretsHelper.isSecretFileExist(editedSecretName, secretManagerId),
        "Secret file should be created successfully");
    Assert.assertTrue(
        !secretsHelper.isSecretFileExist(secretName, secretManagerId), "Secret file should be edited successfully");
    secretsHelper.deleteFile(secretId);
    Assert.assertTrue(!secretsHelper.isSecretFileExist(editedSecretName, secretManagerId),
        "Secret file should be deleted successfully");
  }

  @Test
  public void testSecretFileSearch() {
    String secretName = commonHelper.createRandomName(secretNamePrefix);
    Response response = secretsHelper.addSecretFile(secretName, "echo samplevalue", secretManagerId);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret file should be created successfully");
    Map<String, String> secretProperties = secretsHelper.searchSecret(secretName, SecretConstants.SECRET_FILE);
    Assert.assertEquals(secretName, secretProperties.get("name"), "Secret file should be searched successfully");
    secretsHelper.deleteFile(secretId);
  }

  @Test
  public void testScopeToAccountSecretFileSearch() {
    String secretName = commonHelper.createRandomName(secretNamePrefix);
    Response response = secretsHelper.addSecretFile(secretName, "echo samplevalue", secretManagerId, true);
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret file should be created successfully");
    Map<String, String> secretProperties = secretsHelper.searchSecret(secretName, SecretConstants.SECRET_FILE);
    Object scopedToAccount = secretProperties.get("scopedToAccount");
    Assert.assertTrue(Boolean.valueOf(scopedToAccount.toString()), "Secret file should be searched successfully");
    secretsHelper.deleteFile(secretId);
  }

  @AfterClass
  public void clearSecrets() {
    ArrayList<HashMap> hashMaps = secretsHelper.searchRegexSecret(secretNamePrefix, SecretConstants.SECRET_TEXT);
    for (HashMap<String, String> secret : hashMaps) {
      secretsHelper.deleteSecret(secret.get("uuid"));
    }
    hashMaps = secretsHelper.searchRegexSecret(secretNamePrefix, SecretConstants.SECRET_FILE);
    for (HashMap<String, String> secret : hashMaps) {
      secretsHelper.deleteFile(secret.get("uuid"));
    }
    log.info("all secrets are deleted");
    secretManagerHelper.deleteSecretManagerByName(google_kms, SecretManagerType.GCP_KMS);
  }
}
