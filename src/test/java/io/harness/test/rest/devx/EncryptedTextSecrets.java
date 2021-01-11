package io.harness.test.rest.devx;

import io.harness.rest.helper.dxsecrets.encryptedtext.EncryptedTextHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class EncryptedTextSecrets extends AbstractTest {
    EncryptedTextHelper encryptedTextHelper = new EncryptedTextHelper();
    String secretManagerId = "mtGpFlPNSFaUJNUztLoIaA";
    String secretReference = "000-azure-b22";

    @Test
    public void testCreateEncryptedTextAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
    }

    @Test
    public void testUpdateEncryptedTextAPI() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etId = appResponse.getString("data.createSecret.secret.id");

        JsonPath updatedResponse = encryptedTextHelper.updateEncryptedText(secretName,etId,secretManagerId,secretReference,"NON_PROD_ALL","");
        Assert.assertTrue(!updatedResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(updatedResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(secretName));
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
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));

        JsonPath secretByNameResponse = encryptedTextHelper.getSecretsByNameTest(secretName);
        Assert.assertTrue(secretByNameResponse.getString("data.secretByName.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!secretByNameResponse.getString("data.secretByName.id").isEmpty());
    }

    @Test
    public void testEncryptedTextGetByNameApi() throws IOException {
        String secretName = commonHelper.createRandomName("EText-DevX-");
        JsonPath appResponse = encryptedTextHelper.createEncryptedText(secretName,secretManagerId,secretReference,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        String etName = appResponse.getString("data.createSecret.secret.name");


        JsonPath secretsByNameTest = encryptedTextHelper.getEncryptedTextSecretsByNameSanity(etName);
        Assert.assertTrue(secretsByNameTest.getString("data.secretByName.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!secretsByNameTest.getString("data.secretByName.id").isEmpty());
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
    }

}