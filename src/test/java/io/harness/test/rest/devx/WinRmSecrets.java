package io.harness.test.rest.devx;

import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.dxsecrets.winRM.WinrmHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class WinRmSecrets extends AbstractTest {
    WinrmHelper winrmHelper = new WinrmHelper();
    ApplicationHelper applicationHelper = new ApplicationHelper();
    @Test
    public void testWinRmSecretsGetByNameApi() throws IOException {
        String etName = "WinRM- DO NOT DELETE";
        JsonPath secretsByNameTest = winrmHelper.getWinrmSecretsByNameSanity(etName);
        Assert.assertTrue(secretsByNameTest.getString("data.secretByName.name").equalsIgnoreCase(etName));
        Assert.assertTrue(!secretsByNameTest.getString("data.secretByName.id").isEmpty());
    }

    @Test
    public void testWinRmSecretsGetByIdApi() throws IOException {
        String secretName = "WinRM- DO NOT DELETE";
        String etId = "Hwh9VrKbRi-78BLpeyHLPQ";
        JsonPath secretsByIdTest = winrmHelper.getWinrmSecretsByIdSanity(etId);
        Assert.assertTrue(secretsByIdTest.getString("data.secret.name").equalsIgnoreCase(secretName));
        Assert.assertTrue(!secretsByIdTest.getString("data.secret.id").isEmpty());
    }

}

