package io.harness.test.rest.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.secrets.encryptedFile.EncryptedFileHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = {"PL", "RBAC", "QA"})
public class EncryptedFilesTest extends AbstractTest {
  EncryptedFileHelper encryptedFileHelper = new EncryptedFileHelper();
  String rbacUser = configPropertis.getConfig("RBAC_ACCOUNT_LEVEL_USER");
  String rbacPassword = secretsProperties.getSecret("RBAC_TEST_USER_PASSWORD_QA");

  @Test
  // add an Encrypted File- User not authorized
  public void addSecretFile() {
    String secretName = commonHelper.createRandomName("Secret-Text-");
    JsonPath addResponse =
        encryptedFileHelper.createEncryptedFile(rbacUser, rbacPassword, secretName, "U04qhhIeSYuSPfuPl1VvIQ");
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // edit an Encrypted File- User not authorized
  public void editSecretFile() {
    JsonPath editResponse = encryptedFileHelper.editSecretFile(
        rbacUser, rbacPassword, "RBAC Secret File- DO NOT DELETE", "update", "U04qhhIeSYuSPfuPl1VvIQ");
    assertThat(
        editResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  // delete an Encrypted File- User not authorized
  public void deleteSecretFile() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    Response deleteResponse =
        encryptedFileHelper.deleteEncryptedFile(requestSpecificationObject, "OazeD6kMTy-9imuMApLSUg");
    assertThat(deleteResponse.getStatusCode()).isEqualTo(400);
  }
}
