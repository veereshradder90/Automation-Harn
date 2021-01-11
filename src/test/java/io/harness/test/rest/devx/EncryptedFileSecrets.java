package io.harness.test.rest.devx;

import io.harness.rest.helper.dxsecrets.encryptedfile.EncryptedFileHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class EncryptedFileSecrets extends AbstractTest {

    EncryptedFileHelper encryptedFileHelper = new EncryptedFileHelper();

    @Test
    public void testEncryptedFileGetByNameApi() throws IOException {
        String etName = "Test";
        JsonPath secretsByNameTest = encryptedFileHelper.getEncryptedFileSecretsByNameSanity(etName);
        Assert.assertTrue(secretsByNameTest.getString("data.secretByName.name").equalsIgnoreCase(etName));
        Assert.assertTrue(!secretsByNameTest.getString("data.secretByName.id").isEmpty());
    }

    @Test
    public void testEncryptedFileGetByIdApi() throws IOException {
        String secretName = "Test";
        String etId = "YjwvtpEwSLyuE4wYH4ceMA";
        JsonPath secretsByIdTest = encryptedFileHelper.getEncryptedFileSecretsByIdSanity(etId);
        Assert.assertTrue(secretsByIdTest.getString("data.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!secretsByIdTest.getString("data.secret.id").isEmpty());
    }

}
