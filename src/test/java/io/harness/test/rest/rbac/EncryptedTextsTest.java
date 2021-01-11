package io.harness.test.rest.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.cloudprovider.CloudProviderHelper;
import io.harness.rest.helper.secrets.encryptedText.EncryptedTextHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = {"PL", "RBAC", "QA"})
public class EncryptedTextsTest extends AbstractTest {
  EncryptedTextHelper encryptedTextHelper = new EncryptedTextHelper();
  CloudProviderHelper cpHelper = new CloudProviderHelper();
  String rbacUser = configPropertis.getConfig("RBAC_ACCOUNT_LEVEL_USER");
  String rbacPassword = secretsProperties.getSecret("RBAC_TEST_USER_PASSWORD_QA");

  @Test
  // add an Encrypted Text- User not authorized
  public void addSecretTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String secretName = commonHelper.createRandomName("Secret-Text-");
    JsonPath addResponse = encryptedTextHelper.createEncryptedText(
        requestSpecificationObject, secretName, "value", "U04qhhIeSYuSPfuPl1VvIQ");
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // edit an Encrypted Text- User not authorized
  public void editSecretTest() {
    JsonPath editResponse = encryptedTextHelper.editEncryptedText(
        rbacUser, rbacPassword, "RBAC Secret Text- DO NOT DELETE", "update", "value", "U04qhhIeSYuSPfuPl1VvIQ");
    assertThat(
        editResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // delete Azure Vault- User not authorized
  public void deleteSecretTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    Response deleteResponse =
        encryptedTextHelper.deleteEncryptedText(requestSpecificationObject, "XqdP9uaLQwGNb2UoJvt58g");
    assertThat(deleteResponse.getStatusCode()).isEqualTo(400);
  }
}
