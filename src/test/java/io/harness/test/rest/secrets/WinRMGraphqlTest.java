package io.harness.test.rest.secrets;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.harness.rest.helper.secrets.winRM.WinRmHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Map;

/**
 * @author Boopesh.Shanmugam
 * @apiNote Contains the test cases for WinRM secrets in Secret managers
 */

@Test(groups = {"PL","QA"})
public class WinRMGraphqlTest extends AbstractTest {
    WinRmHelper winRmHelper = new WinRmHelper();
    // Dynamic refernce can be done with updated package present in master
    SecretsHelper secrets = new SecretsHelper();
    Map PasswordResponse= secrets.getSecretProperties("qesetupaccesskey");
    String Password=PasswordResponse.get("uuid").toString();
//    String Password="mq-PSjt0Sjmq43dEE5V1og";
//    String envId="lNnSwhWlQO2D4RcrRDr3LQ"

    @Test
    public void testCreateWinRMUsingNTLMAPI() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "PROD_ALL","");
        String winId=appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(WinRmName));
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.userName").equalsIgnoreCase(userName));
        winRmHelper.deleteWinRm(winId);
    }

    @Test
    public void testUpdateWinRMUsingNTLMPI() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "PROD_ALL","");
        String winId=appResponse.getString("data.createSecret.secret.id");
        String newWinRmName = WinRmName+"new";
        String newuserName = userName+"new";
        String newdomain = domain+"new";
        JsonPath appResponseUpdate = winRmHelper.updateWinRMUsingNTLM(newWinRmName, newuserName, newdomain, Password, winId, "PROD_ALL","");
        Assert.assertTrue(!appResponseUpdate.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponseUpdate.getString("data.updateSecret.secret.name").equalsIgnoreCase(newWinRmName));
        Assert.assertTrue(appResponseUpdate.getString("data.updateSecret.secret.userName").equalsIgnoreCase(newuserName));
        winRmHelper.deleteWinRm(winId);

    }

    @Test
    public void testCreateWinRMUsingNTLMAdvancedAPI() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLMAdvanced(WinRmName, userName, domain, Password, "PROD_ALL","");
        String winId=appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(WinRmName));
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.userName").equalsIgnoreCase(userName));
        winRmHelper.deleteWinRm(winId);

    }
    @Test
    public void testUpdateWinRMUsingNTLMAdvancedPI() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "PROD_ALL","");
        String winId=appResponse.getString("data.createSecret.secret.id");
        String newWinRmName = WinRmName+"new";
        String newuserName = userName+"new";
        String newdomain = domain+"new";
        JsonPath appResponseUpdate = winRmHelper.updateWinRMUsingNTLMAdvanced(newWinRmName, newuserName, newdomain, Password, winId, "PROD_ALL","");
        Assert.assertTrue(!appResponseUpdate.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponseUpdate.getString("data.updateSecret.secret.name").equalsIgnoreCase(newWinRmName));
        Assert.assertTrue(appResponseUpdate.getString("data.updateSecret.secret.userName").equalsIgnoreCase(newuserName));
        winRmHelper.deleteWinRm(winId);

    }

    @Test
    public void testDeleteWinRMWithNTLMAPI() throws IOException{
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "PROD_ALL","");
        String winId=appResponse.getString("data.createSecret.secret.id");
        JsonPath deletedResponse=winRmHelper.deleteWinRm(winId);
        Assert.assertTrue(!deletedResponse.getString("data.deleteSecret.clientMutationId").isEmpty());

    }



    @Test
    public void testCreateWinRMUsingDuplicateName() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "PROD_ALL","");
        String winId=appResponse.getString("data.createSecret.secret.id");
        String newUsername=userName+"new";
        String newDomain=domain+"new";
        String errorMessage="Exception while fetching data (/createSecret) : Invalid argument(s): The name "+ WinRmName + " already exists in SETTING.";
        JsonPath appResponseDuplicate = winRmHelper.createWinRMUsingNTLM(WinRmName, newUsername, newDomain, Password, "PROD_ALL","");
        Assert.assertTrue(appResponseDuplicate.getString("errors.message").contains(errorMessage));
        winRmHelper.deleteWinRm(winId);

    }
    @Test
    public void testWinRMOneApplicationNonProductionEnvironemtUsageScopeApi() throws IOException {
        String WinRmName = commonHelper.createRandomName("aaaanatraj");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "NON_PROD_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        String winId=appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = winRmHelper.getSecretsById(winId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[NON_PRODUCTION_ENVIRONMENTS]"));

    }

    @Test
    public void testWinRMOneApplicationProductionEnvironemtUsageScopeApi() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "PROD_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        String winId=appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = winRmHelper.getSecretsById(winId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[PRODUCTION_ENVIRONMENTS]"));
        winRmHelper.deleteWinRm(winId);
    }

    @Test
    public void testWinRMOneApplicationOneEnvironemtUsageScopeApi() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "LOCAL_ENV_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        String winId=appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = winRmHelper.getSecretsById(winId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.envId").equalsIgnoreCase("[lNnSwhWlQO2D4RcrRDr3LQ]"));
        winRmHelper.deleteWinRm(winId);
    }
    @Test
    public void testWinRMAllApplicationProductionEnvironemtUsageScopeApi() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "PROD_ALL","");
        String winId=appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = winRmHelper.getSecretsById(winId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.filterType").equalsIgnoreCase("[ALL]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[PRODUCTION_ENVIRONMENTS]"));
        winRmHelper.deleteWinRm(winId);
    }
    @Test
    public void testWinRMAllApplicationNonProductionEnvironemtUsageScopeApi() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "NON_PROD_ALL","");
        String winId=appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = winRmHelper.getSecretsById(winId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.filterType").equalsIgnoreCase("[ALL]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[NON_PRODUCTION_ENVIRONMENTS]"));
        winRmHelper.deleteWinRm(winId);
    }

    @Test
    public void testWinRMAllApplicationLocalEnvironemtUsageScopeApi() throws IOException {
        String WinRmName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String domain = commonHelper.createRandomName("test.org");
        JsonPath appResponse = winRmHelper.createWinRMUsingNTLM(WinRmName, userName, domain, Password, "LOCAL_ENV_ALL","");
        String errorMessage="Exception while fetching data (/createSecret) : Invalid request: EnvId cannot be supplied with app filterType ALL";
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));
    }




}
