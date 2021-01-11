package io.harness.test.rest.devx;

import io.harness.rest.helper.dxsecrets.ssh.SshHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class SshSecrets extends AbstractTest {
    SshHelper sshHelper  = new SshHelper();

    @Test
    public void testSshSecretsGetByNameApi() throws IOException {
        String etName = "asdasdasd";
        JsonPath secretsByNameTest = sshHelper.getSSHSecretsByNameSanity(etName);
        Assert.assertTrue(secretsByNameTest.getString("data.secretByName.name").equalsIgnoreCase(etName));
        Assert.assertTrue(!secretsByNameTest.getString("data.secretByName.id").isEmpty());
    }

    @Test
    public void testSshSecretsGetByIdApi() throws IOException {
        String secretName = "asdasdasd";
        String etId = "CCK-XqtwTe2wS7xVifmuvQ";
        JsonPath secretsByIdTest = sshHelper.getSSHSecretsByIdSanity(etId);
        Assert.assertTrue(secretsByIdTest.getString("data.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!secretsByIdTest.getString("data.secret.id").isEmpty());
    }
}
