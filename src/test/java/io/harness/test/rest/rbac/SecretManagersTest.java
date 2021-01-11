package io.harness.test.rest.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.harness.rest.helper.secretmanager.SecretManagerType;
import io.harness.rest.helper.templatelibrary.TemplateLibraryHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

@Test(groups = {"PL", "RBAC", "QA"})
public class SecretManagersTest extends AbstractTest {
  SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
  TemplateLibraryHelper templateLibraryHelper = new TemplateLibraryHelper();
  String rbacUser = configPropertis.getConfig("RBAC_ACCOUNT_LEVEL_USER");
  String rbacPassword = secretsProperties.getSecret("RBAC_TEST_USER_PASSWORD_QA");

  @Test
  // add Azure Vault- User not authorized
  public void addAzureVaultTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String azureName = commonHelper.createRandomName("Azure-Secret-Manager-");
    Response addResponse = secretManagerHelper.createAzureVaultSecretManager(requestSpecificationObject, azureName);
    assertThat(addResponse.getStatusCode()).isEqualTo(400);
  }

  @Test
  // delete Azure Vault- User not authorized
  public void deleteAzureVaultTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("Azure- DO NOT DELETE");
    JsonPath deleteResponse = secretManagerHelper.deleteSecretManagerById(
        requestSpecificationObject, secretManagerId, SecretManagerType.AZURE_VAULT);
    assertThat(
        deleteResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // add HashiCorp Vault- User not authorized
  public void addHashiCorpVaultTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String hashiName = commonHelper.createRandomName("HashiCorp-Secret-Manager-");
    Response addResponse = secretManagerHelper.createHashiCorpVault(requestSpecificationObject, hashiName);
    assertThat(addResponse.getStatusCode()).isEqualTo(400);
  }

  @Test
  // delete HashiCorp Vault- User not authorized
  public void deleteHashiCorpVaultTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("HashiCorp- DO NOT DELETE");
    JsonPath deleteResponse = secretManagerHelper.deleteSecretManagerById(
        requestSpecificationObject, secretManagerId, SecretManagerType.HASHICORP_VAULT);
    assertThat(
        deleteResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // add AWS KMS- User not authorized
  public void addAWSKMSTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String awskmsName = commonHelper.createRandomName("AWS-KMS-");
    Response addResponse = secretManagerHelper.createAWSKMS(requestSpecificationObject, awskmsName);
    assertThat(addResponse.getStatusCode()).isEqualTo(400);
  }

  @Test
  // delete AWS KMS- User not authorized
  public void deleteAWSKMSTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("AWS KMS- DO NOT DELETE");
    JsonPath deleteResponse = secretManagerHelper.deleteSecretManagerById(
        requestSpecificationObject, secretManagerId, SecretManagerType.AWS_KMS);
    assertThat(
        deleteResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // add AWS Secret Manager- User not authorized
  public void addAWSSecretManagerTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String awsName = commonHelper.createRandomName("AWS-Secret-Manager");
    Response addResponse = secretManagerHelper.createAWSSecretManager(requestSpecificationObject, awsName);
    assertThat(addResponse.getStatusCode()).isEqualTo(400);
  }

  @Test
  // delete AWS Secret Manager- User not authorized
  public void deleteAWSSecretManagerTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("AWS Secret Manager- DO NOT DELETE");
    JsonPath deleteResponse = secretManagerHelper.deleteSecretManagerById(
        requestSpecificationObject, secretManagerId, SecretManagerType.AWS_SM);
    assertThat(
        deleteResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // add Custom Secret Manager- User not authorized
  public void addCustomSecretManagerTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_no_var");
    String customName = commonHelper.createRandomName("Custom-Secret-Manager");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    Response addResponse =
        secretManagerHelper.createCustomSecretManager(requestSpecificationObject, customName, secretProperties);
    assertThat(addResponse.getStatusCode()).isEqualTo(400);
  }

  @Test
  // edit Custom Secret Manager- User not authorized
  public void editCustomSecretManagerTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String templateId = templateLibraryHelper.getTemplateLibraryId("csm_automation_no_var");
    Map<String, Object> secretProperties = new HashMap<>();
    secretProperties.put("templateId", templateId);
    Response editResponse = secretManagerHelper.editCustomSecretManager(
        requestSpecificationObject, "Custom-Secret-Manager-update", "4SoppSuHSY2ABNFogHgaEQ", secretProperties);
    assertThat(editResponse.getStatusCode()).isEqualTo(400);
  }

  @Test
  // delete Custom Secret Manager- User not authorized
  public void deleteCustomSecretManagerTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("Custom- DO NOT DELETE");
    JsonPath deleteResponse = secretManagerHelper.deleteSecretManagerById(
        requestSpecificationObject, secretManagerId, SecretManagerType.CUSTOM_SM);
    assertThat(
        deleteResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // add Google KMS- User not authorized
  public void googleKMSTest() {
    String googleName = commonHelper.createRandomName("Google-KMS-");
    Response addResponse = secretManagerHelper.createGoogleKMS(rbacUser, rbacPassword, googleName);
    assertThat(addResponse.getStatusCode()).isEqualTo(400);
  }

  @Test
  // delete Google KMS- User not authorized
  public void deleteGoogleKMSTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("Google- DO NOT DELETE");
    JsonPath deleteResponse = secretManagerHelper.deleteSecretManagerById(
        requestSpecificationObject, secretManagerId, SecretManagerType.GCP_KMS);
    assertThat(
        deleteResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }
}
