package io.harness.test.rest.secrets;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map;

public class SecretsValidationUtil extends CoreUtils {
  CommonHelper commonHelper = new CommonHelper();
  SecretsHelper secretsHelper = new SecretsHelper();

  public void validateSecretAddition(String secretManagerId) {
    String secretText = commonHelper.createRandomName("test-secret");
    Response response = secretsHelper.addSecretText(secretText, secretText, secretManagerId);
    Assert.assertEquals(200, response.getStatusCode(), "secret creation should be success");
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret should be created successfully");
    Assert.assertTrue(secretsHelper.isSecretExist(secretText, secretManagerId), "secret should be exist");
    secretsHelper.deleteSecret(secretId);
    Assert.assertFalse(secretsHelper.isSecretExist(secretText, secretManagerId), "secret should be exist");
  }

  public void validateSecretAddition(String secretText, String secretValue, String secretManagerId) {
    Response response = secretsHelper.addSecretText(secretText, secretValue, secretManagerId);
    Assert.assertEquals(200, response.getStatusCode(), "secret creation should be success");
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret should be created successfully");
    Assert.assertTrue(secretsHelper.isSecretExist(secretText, secretManagerId), "secret should be exist");
    secretsHelper.deleteSecret(secretId);
    Assert.assertFalse(secretsHelper.isSecretExist(secretText, secretManagerId), "secret should be exist");
  }

  public void validateSecretEdit(
      String secretText, String secretValue, Map<String, String> editingValues, String secretManagerId) {
    Response response = secretsHelper.addSecretText(secretText, secretValue, secretManagerId);
    Assert.assertEquals(200, response.getStatusCode(), "secret creation should be success");
    String secretId = response.jsonPath().getString("resource");
    Assert.assertNotNull(secretId, "Secret should be created successfully");
    response = secretsHelper.editSecret(secretText, editingValues);
    Assert.assertEquals(200, response.getStatusCode(), "secret edit should be success");
    boolean editResult = response.jsonPath().getBoolean("resource");
    Assert.assertTrue(editResult, "edit should be success");
    Assert.assertTrue(
        secretsHelper.isSecretExist(editingValues.get("name"), secretManagerId), "secret should be edited as expected");
    secretsHelper.deleteSecret(secretId);
    Assert.assertFalse(
        secretsHelper.isSecretExist(editingValues.get("name"), secretManagerId), "secret should be exist");
  }
}
