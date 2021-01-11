package io.harness.test.rest.secrets;
import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.harness.rest.helper.secrets.encryptedText.EncryptedTextHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author Boopesh.Shanmugam
 * @apiNote Contains test cases for Encrypted Text Secrets in Secret Manager
 */

@Test(groups = {"PL","QA"})
public class EncryptedTextSecretsTest extends AbstractTest {
    SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
    EncryptedTextHelper encryptedTextHelper = new EncryptedTextHelper();
    String secretManagerId = secretManagerHelper.getSecretManagerList().get("000-matt-azure");
    String HashicorpId= secretManagerHelper.getSecretManagerList().get("VaultSM");
    String GooglemanagerId= secretManagerHelper.getSecretManagerList().get("GoogleKMS");
    String AwsManagerId= secretManagerHelper.getSecretManagerList().get("aws_secretManager65npp_edit");
    String secretReference = "000-azure-b22";
    String secretReferenceHashi="/guna/SECRET_TEXT/newtest#value";

    @Test
    public void testCreateEncryptedTextAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        String etId = appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testUpdateEncryptedTextAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        String newSecretName=secretName+"new";
        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedText(newSecretName,etId,secretReference,"NON_PROD_ALL","");
        Assert.assertTrue(!updatedResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSecretName));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testDeleteEncryptedTextAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath deletedResponse = encryptedTextHelper.deleteEncryptedText(etId);
        Assert.assertTrue(!deletedResponse.getString("data.deleteSecret.clientMutationId").isEmpty());
    }

    @Test
    public void testGetByNameSecret() throws IOException{
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        String etId = appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        JsonPath secretByNameResponse = encryptedTextHelper.getSecretsByNameTest(secretName);
        Assert.assertTrue(secretByNameResponse.getString("data.secretByName.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!secretByNameResponse.getString("data.secretByName.id").isEmpty());
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testEncryptedTextGetByNameApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etName = appResponse.getString("data.createSecret.secret.name");
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByNameTest = encryptedTextHelper.getEncryptedTextSecretsByNameSanity(etName);
        Assert.assertTrue(secretsByNameTest.getString("data.secretByName.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!secretsByNameTest.getString("data.secretByName.id").isEmpty());
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testEncryptedTextGetByIdApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdTest = encryptedTextHelper.getEncryptedTextSecretsByIdSanity(etId);
        Assert.assertTrue(secretsByIdTest.getString("data.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!secretsByIdTest.getString("data.secret.id").isEmpty());
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testCreateEncryptedTextEmptyNameAPI() throws IOException {
        String secretName ="";
        String errorMessage="Exception while fetching data (/createSecret) : Invalid request: The name of the secret can not be blank";
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));
    }

    @Test
    public void testCreateEncryptedTextEmptyValueAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        String secretReference="";
        String errorMessage="Exception while fetching data (/createSecret) : Invalid request: Supply either the secret path or the secret value";
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));

    }

    @Test
    public void testEncryptedTextAllApplicationProductionEnvironemtUsageScopeApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-Devx-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = encryptedTextHelper.getEncryptedTextSecretsByIdUsageScope(etId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.filterType").equalsIgnoreCase("[ALL]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[PRODUCTION_ENVIRONMENTS]"));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testEncryptedTextAllApplicationNonProductionEnvironemtUsageScopeApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-Devx-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"NON_PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = encryptedTextHelper.getEncryptedTextSecretsByIdUsageScope(etId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.filterType").equalsIgnoreCase("[ALL]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[NON_PRODUCTION_ENVIRONMENTS]"));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testEncryptedTextOneApplicationProductionEnvironemtUsageScopeApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-Devx-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = encryptedTextHelper.getEncryptedTextSecretsByIdUsageScope(etId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[PRODUCTION_ENVIRONMENTS]"));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testEncryptedTextOneApplicationNonProductionEnvironemtUsageScopeApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-Devx-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"NON_PROD_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = encryptedTextHelper.getEncryptedTextSecretsByIdUsageScope(etId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[NON_PRODUCTION_ENVIRONMENTS]"));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testEncryptedTextOneApplicationLocalEnvironemtUsageScopeApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-Devx-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"LOCAL_ENV_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = encryptedTextHelper.getEncryptedTextSecretsByIdUsageScope(etId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.envId").equalsIgnoreCase("[lNnSwhWlQO2D4RcrRDr3LQ]"));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testUpdateEncryptedTextNameAndScopeAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        String newSecretName=secretName+"new";
        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedText(newSecretName,etId,secretReference,"PROD_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        Assert.assertTrue(!updatedResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSecretName));
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[PRODUCTION_ENVIRONMENTS]"));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testCreateEncryptedTexDuplicateNametAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        JsonPath appResponseDuplicate = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        String errorMessage="Exception while fetching data (/createSecret) : Duplicate name "+ secretName;
        Assert.assertTrue(appResponseDuplicate.getString("errors.message").contains(errorMessage));
    }

    @Test
    public void testUpdateEncryptedTextDuplicateNameAPI() throws IOException {
        String secretName1 = commonHelper.createRandomName("EText-DevX-");
        String secretName2 = commonHelper.createRandomName("EText-DevXDup-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName1,secretManagerId,secretReference,"PROD_ALL","");
        encryptedTextHelper.createEncryptedText(secretName2,secretManagerId,secretReference,"PROD_ALL","");
        String etId = appResponse.getString("data.createSecret.secret.id");
        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedText(secretName2,etId,secretReference,"NON_PROD_ALL","");
        String errorMessage="Exception while fetching data (/updateSecret) : Duplicate name "+ secretName2;
        Assert.assertTrue(updatedResponse.getString("errors.message").contains(errorMessage));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testUpdateEncryptedTextWithValueGoogleKMSAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        String value= commonHelper.createRandomName("Password123");
        JsonPath appResponse = encryptedTextHelper.createEncryptedTextWithValue(secretName,GooglemanagerId,value,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        String newSecretName=secretName+"new";
        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedTextWithValue(newSecretName,etId,value,"NON_PROD_ALL","");
        Assert.assertTrue(!updatedResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSecretName));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testUpdateEncryptedTextWithValueAWSKMSAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        String value= commonHelper.createRandomName("Password123");
        JsonPath appResponse = encryptedTextHelper.createEncryptedTextWithValue(secretName,AwsManagerId,value,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        String newSecretName=secretName+"new";
        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedTextWithValue(newSecretName,etId,value,"NON_PROD_ALL","");
        Assert.assertTrue(!updatedResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSecretName));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testEncryptedTextAllApplicationLocalEnvironemtUsageScopeApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-Devx-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"LOCAL_ENV_ALL","");
        String errorMessage="Exception while fetching data (/createSecret) : Invalid request: EnvId cannot be supplied with app filterType ALL";
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));
    }

    @Test
    public void testUpdateEncryptedTextWithReferenceHashiAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,HashicorpId,secretReferenceHashi,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        String newSecretName=secretName+"new";
        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedText(newSecretName,etId,secretReferenceHashi,"NON_PROD_ALL","");
        Assert.assertTrue(!updatedResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSecretName));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testUpdateEncryptedTextWithValueHashiCorpAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        String value= commonHelper.createRandomName("Password123");
        JsonPath appResponse = encryptedTextHelper.createEncryptedTextWithValue(secretName,HashicorpId,value,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        String newSecretName=secretName+"new";
        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedTextWithValue(newSecretName,etId,value,"NON_PROD_ALL","");
        Assert.assertTrue(!updatedResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSecretName));
        encryptedTextHelper.deleteEncryptedText(etId);
    }

    @Test
    public void testCreateEncryptedTextWithValueAWSManagerAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        String value= commonHelper.createRandomName("Password123");
        JsonPath appResponse = encryptedTextHelper.createEncryptedTextWithValue(secretName,AwsManagerId,value,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        encryptedTextHelper.deleteEncryptedText(etId);
    }
    @Test
    public void testCreateEncryptedTextWithValueWrongManagerCredentialsAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        String value= commonHelper.createRandomName("Password123");
        JsonPath appResponse = encryptedTextHelper.createEncryptedTextWithValue(secretName,"dummyCredential",value,"PROD_ALL","");
        String errorMessage="Exception while fetching data (/createSecret) : An error has occurred. Please contact the Harness support team.";
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));

    }

    @Test
    public void testUpdateEncryptedTextWithValueAzureManagerAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        String value= commonHelper.createRandomName("Password123");
        JsonPath appResponse = encryptedTextHelper.createEncryptedTextWithValue(secretName,secretManagerId,value,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");
        String newSecretName=secretName+"new";
        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedTextWithValue(newSecretName,etId,value,"NON_PROD_ALL","");
        Assert.assertTrue(!updatedResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSecretName));
        encryptedTextHelper.deleteEncryptedText(etId);
    }




}