package io.harness.test.rest.secrets;

import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.harness.rest.helper.secretmanager.SecretManagerType;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.Map;

@Test(groups = {"PL", "SecretManager", "PR", "QA", "PROD"})
public class SecretManagerTest extends AbstractTest {
  SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
  SecretsHelper secretsHelper = new SecretsHelper();
  SecretsValidationUtil secretsValidationUtil = new SecretsValidationUtil();

  @Test
  public void createHashicorpVaultWithTokenTest() {
    String hashicorp_vault = commonHelper.createRandomName("hashicorp_vault");
    Response response = secretManagerHelper.createVaultSecretManager(hashicorp_vault);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be created successfully");
    String vaultSecretManagerId = response.jsonPath().getString("resource");
    Map<String, String> secretManagerList = secretManagerHelper.getSecretManagerList();
    Assert.assertTrue(secretManagerList.containsKey(hashicorp_vault), "newly created SM should exist");
    Assert.assertEquals(vaultSecretManagerId, secretManagerList.get(hashicorp_vault), "newly created SM should exist");

    secretsValidationUtil.validateSecretAddition(vaultSecretManagerId);

    response = secretManagerHelper.deleteSecretManagerByName(hashicorp_vault, SecretManagerType.HASHICORP_VAULT);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  public void createHashicorpVaultWithAppRoleTest() {
    String hashicorp_vault = commonHelper.createRandomName("hashicorp_vault");
    Response response = secretManagerHelper.createVaultSecretManagerWithAppRole(hashicorp_vault);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be created successfully");
    String vaultSecretManagerId = response.jsonPath().getString("resource");
    Map<String, String> secretManagerList = secretManagerHelper.getSecretManagerList();
    Assert.assertTrue(secretManagerList.containsKey(hashicorp_vault), "newly created SM should exist");
    Assert.assertEquals(vaultSecretManagerId, secretManagerList.get(hashicorp_vault), "newly created SM should exist");

    secretsValidationUtil.validateSecretAddition(vaultSecretManagerId);

    response = secretManagerHelper.deleteSecretManagerByName(hashicorp_vault, SecretManagerType.HASHICORP_VAULT);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  @Ignore // Configure the AWS KMS details to run this one
  public void createAwsKmsTest() {
    String awsKms = commonHelper.createRandomName("awsKms");
    Response response = secretManagerHelper.createAwsKmsSecretManager(awsKms, SecretManagerType.AWS_KMS);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be created successfully");
    String vaultSecretManagerId = response.jsonPath().getString("resource");
    Map<String, String> secretManagerList = secretManagerHelper.getSecretManagerList();
    Assert.assertTrue(secretManagerList.containsKey(awsKms), "newly created SM should exist");
    Assert.assertEquals(vaultSecretManagerId, secretManagerList.get(awsKms), "newly created SM should exist");

    secretsValidationUtil.validateSecretAddition(vaultSecretManagerId);

    response = secretManagerHelper.deleteSecretManagerByName(awsKms, SecretManagerType.AWS_KMS);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  @Ignore // Configure the AWS KMS details to run this one
  public void createAwsSMTest() {
    String awssm = commonHelper.createRandomName("awssm");
    Response response = secretManagerHelper.createAwsKmsSecretManager(awssm, SecretManagerType.AWS_SM);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be created successfully");
    String vaultSecretManagerId = response.jsonPath().getString("resource");
    Map<String, String> secretManagerList = secretManagerHelper.getSecretManagerList();
    Assert.assertTrue(secretManagerList.containsKey(awssm), "newly created SM should exist");
    Assert.assertEquals(vaultSecretManagerId, secretManagerList.get(awssm), "newly created SM should exist");

    secretsValidationUtil.validateSecretAddition(vaultSecretManagerId);

    response = secretManagerHelper.deleteSecretManagerByName(awssm, SecretManagerType.AWS_SM);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  public void createAzureVaultsTest() {
    String azureVault = commonHelper.createRandomName("azureVault");
    Response response = secretManagerHelper.createAzureVaultSecretManager(azureVault);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be created successfully");
    String vaultSecretManagerId = response.jsonPath().getString("resource");
    Map<String, String> secretManagerList = secretManagerHelper.getSecretManagerList();
    Assert.assertTrue(secretManagerList.containsKey(azureVault), "newly created SM should exist");
    Assert.assertEquals(vaultSecretManagerId, secretManagerList.get(azureVault), "newly created SM should exist");

    secretsValidationUtil.validateSecretAddition(vaultSecretManagerId);

    response = secretManagerHelper.deleteSecretManagerByName(azureVault, SecretManagerType.AZURE_VAULT);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }

  @Test
  public void createGoogleKMSTest() {
    String gcpkms = commonHelper.createRandomName("gcpkms");
    Response response = secretManagerHelper.createGoogleKMS(gcpkms);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be created successfully");
    String secretManagerId = response.jsonPath().getString("resource");
    Map<String, String> secretManagerList = secretManagerHelper.getSecretManagerList();
    Assert.assertTrue(secretManagerList.containsKey(gcpkms), "newly created SM should exist");
    Assert.assertEquals(secretManagerId, secretManagerList.get(gcpkms), "newly created SM should exist");

    secretsValidationUtil.validateSecretAddition(secretManagerId);

    response = secretManagerHelper.deleteSecretManagerByName(gcpkms, SecretManagerType.GCP_KMS);
    Assert.assertEquals(200, response.getStatusCode(), "Secret Manager should be deleted successfully");
    Assert.assertTrue(response.jsonPath().getBoolean("resource"), "newly created SM should be deleted successfully");
  }
}
