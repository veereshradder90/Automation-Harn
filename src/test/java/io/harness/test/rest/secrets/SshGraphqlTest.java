package io.harness.test.rest.secrets;
import io.harness.rest.helper.secrets.SecretsHelper;
import io.harness.rest.helper.secrets.sshGraphql.SshHelperGraphql;
import io.harness.rest.helper.secrets.sshGraphql.SshType;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Map;

/**
 * @author Boopesh.Shanmugam
 * @apiNote Contains the test cases for SSH secrets in Secret manager
 */

@Test(groups = {"PL","QA"})
public class SshGraphqlTest extends AbstractTest {
    SshHelperGraphql sshHelper = new SshHelperGraphql();
    // Dynamic refernce can be done with updated package present in master
    SecretsHelper secrets = new SecretsHelper();
    Map InlineKeyResponse= secrets.getSecretFile("ssh_file");
    Map PasswordResponse= secrets.getSecretProperties("qesetupaccesskey");
    String InlineKey=InlineKeyResponse.get("uuid").toString();
    String Password=PasswordResponse.get("uuid").toString();
//    String InlineKey="ePuBMQvPQ0Sl3FGYkd9NQg";
//    String Password="mq-PSjt0Sjmq43dEE5V1og";
//      String appid="K5ekE570SoCb0JN7bfJclw";


    @Test
    public void testCreateSshUsingInlineAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, InlineKey,type,"PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(sshName));
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.authenticationType.userName").equalsIgnoreCase(userName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshWithInlineAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, InlineKey,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newUserName=userName+"new";
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,InlineKey,sshId,type,"PROD_ALL","");
        Assert.assertTrue(!appResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.authenticationType.userName").equalsIgnoreCase(newUserName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testDeleteSshWithInlineAPI() throws IOException{
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, InlineKey,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        JsonPath deletedResponse=sshHelper.deleteSsh(sshId);
        Assert.assertTrue(!deletedResponse.getString("data.deleteSecret.clientMutationId").isEmpty());

    }
    @Test
    public void testCreateSshUsingKeyPathAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String path = "/home/user/path";
        SshType type=SshType.PATH;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, path,type,"PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(sshName));
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.authenticationType.userName").equalsIgnoreCase(userName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshWithKeyPathAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String path = "/home/user/path";
        SshType type=SshType.PATH;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, path,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newUserName=userName+"new";
        String newPath=path+"new";
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,newPath,sshId,type,"PROD_ALL","");
        Assert.assertTrue(!appResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.authenticationType.userName").equalsIgnoreCase(newUserName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testDeleteSshWithKeyPathAPI() throws IOException{
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String path ="/home/user/path";
        SshType type=SshType.PATH;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, path,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        JsonPath deletedResponse=sshHelper.deleteSsh(sshId);
        Assert.assertTrue(!deletedResponse.getString("data.deleteSecret.clientMutationId").isEmpty());

    }
    @Test
    public void testCreateSshUsingPasswordAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(sshName));
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.authenticationType.userName").equalsIgnoreCase(userName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshWithPasswordAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newUserName=userName+"new";
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,Password,sshId,type,"PROD_ALL","");
        Assert.assertTrue(!appResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.authenticationType.userName").equalsIgnoreCase(newUserName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testDeleteSshWithPasswordAPI() throws IOException{
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        JsonPath deletedResponse=sshHelper.deleteSsh(sshId);
        Assert.assertTrue(!deletedResponse.getString("data.deleteSecret.clientMutationId").isEmpty());

    }
    @Test
    public void testGetSshByNameAPI() throws IOException{
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        JsonPath secretByNameResponse = sshHelper.getSecretsByName(sshName);
        Assert.assertTrue(secretByNameResponse.getString("data.secretByName.name").equalsIgnoreCase(sshName));
        Assert.assertTrue(!secretByNameResponse.getString("data.secretByName.id").isEmpty());
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testGetSshByIdAPI() throws IOException{
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        JsonPath secretByIdResponse = sshHelper.getSecretsById(sshId);
        Assert.assertTrue(secretByIdResponse.getString("data.secret.name").equalsIgnoreCase(sshName));
        Assert.assertTrue(!secretByIdResponse.getString("data.secret.id").isEmpty());
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshWithEmptyPasswordAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newUserName=userName+"new";
        String errorMessage="Exception while fetching data (/updateSecret) : Invalid request: No SSH password secret id provided for the SSH credential type PASSWORD";
        String Password="";
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,Password,sshId,type,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshWithInlineToKeyPathAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, InlineKey,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newUserName=userName+"new";
        String path="/home/user/path";
        SshType typeNew =SshType.PATH;
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,path,sshId,typeNew,"PROD_ALL","");
        Assert.assertTrue(!appResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.authenticationType.userName").equalsIgnoreCase(newUserName));
        sshHelper.deleteSsh(sshId);

    }
    public void testUpdateSshWithEmptyUsernameAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName =commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newUserName="";
        String errorMessage="Exception while fetching data (/updateSecret) : Invalid request: The user name provided with the request cannot be blank";
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,Password,sshId,type,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshCredentialsAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, InlineKey,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newUserName=userName+"new";
        Map InlineKeyResponse= secrets.getSecretFile("sshkeycdp");
        String InlineKeyUpdated=InlineKeyResponse.get("uuid").toString();
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,InlineKeyUpdated,sshId,type,"PROD_ALL","");
        Assert.assertTrue(!appResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.authenticationType.userName").equalsIgnoreCase(newUserName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshEmptyNameAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName="";
        String newUserName=userName+"new";
        String errorMessage="Exception while fetching data (/updateSecret) : Invalid request: Cannot set the ssh credential name as null";
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,Password,sshId,type,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshEmptyCredentialsAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newUserName=userName+"new";
        String Password="";
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,newUserName,Password,sshId,type,"PROD_ALL","");
        String errorMessage="Exception while fetching data (/updateSecret) : Invalid request: No SSH password secret id provided for the SSH credential type PASSWORD";
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));
        sshHelper.deleteSsh(sshId);

    }

    @Test
    public void testCreateUpdateSshDuplicateNamesAPI() throws IOException {
        String sshName = ("test_sample_dupCheck");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.PASSWORD;
        JsonPath appResponse1 = sshHelper.createSshKey(sshName, userName, Password,type,"PROD_ALL","");
        String sshId1=appResponse1.getString("data.createSecret.secret.id");
        String sshName2 = ("test_sample_dupCheck");
        String userName2 = commonHelper.createRandomName("test");
        JsonPath appResponse2 = sshHelper.createSshKey(sshName2, userName2, Password,type,"PROD_ALL","");
        String errorMessageCreate="Exception while fetching data (/createSecret) : Invalid argument(s): The name test_sample_dupCheck already exists in SETTING.";
        Assert.assertTrue(appResponse2.getString("errors.message").contains(errorMessageCreate));
        sshName2 = commonHelper.createRandomName("test_sample_dupCheck");
        userName2 = commonHelper.createRandomName("test");
        appResponse2 = sshHelper.createSshKey(sshName2, userName2, Password,type,"PROD_ALL","");
        String sshId2=appResponse2.getString("data.createSecret.secret.id");
        String sshName3="test_sample_dupCheck";
        String errorMessageUpdate="Exception while fetching data (/updateSecret) : Invalid argument(s): The name test_sample_dupCheck already exists in SETTING.";
        JsonPath finalResponse=sshHelper.updateSshKey(sshName3,userName2,Password,sshId2,type,"PROD_ALL","");
        Assert.assertTrue(finalResponse.getString("errors.message").contains(errorMessageUpdate));
        sshHelper.deleteSsh(sshId1);

    }
    @Test
    public void testCreateSshUsingKeyPathAndPasswordWithEmptyCredentialsAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        String path = "";
        String errorMessagePath="Exception while fetching data (/createSecret) : Invalid request: No SSH key file path provided for the SSH credential type SSH_KEY_FILE_PATH";
        SshType typePath=SshType.PATH;
        JsonPath appResponsePath = sshHelper.createSshKey(sshName, userName, path,typePath,"PROD_ALL","");
        Assert.assertTrue(appResponsePath.getString("errors.message").contains(errorMessagePath));
        SshType typePass=SshType.PASSWORD;
        String Password="";
        String errorMessagePassword="Exception while fetching data (/createSecret) : Invalid request: No SSH password secret id provided for the SSH credential type PASSWORD";
        JsonPath appResponsePassword = sshHelper.createSshKey(sshName, userName,Password,typePass,"PROD_ALL","");
        Assert.assertTrue(appResponsePassword.getString("errors.message").contains(errorMessagePassword));
        SshType typeInline=SshType.INLINE;
        String InlineEmpty="";
        String errorMessageInline="Exception while fetching data (/createSecret) : Invalid request: No SSH key secret file id provided for the SSH credential type SSH_KEY";
        JsonPath appResponseInline = sshHelper.createSshKey(sshName, userName,InlineEmpty,typeInline,"PROD_ALL","");
        Assert.assertTrue(appResponseInline.getString("errors.message").contains(errorMessageInline));

    }
    @Test
    public void testSshAllApplicationProductionEnvironemtUsageScopeApi() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, InlineKey,type,"PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = sshHelper.getSecretsById(sshId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.filterType").equalsIgnoreCase("[ALL]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[PRODUCTION_ENVIRONMENTS]"));
        sshHelper.deleteSsh(sshId);
    }

    @Test
    public void testSshAllApplicationNonProductionEnvironemtUsageScopeApi() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, InlineKey,type,"NON_PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");



        JsonPath secretsByIdUsageScopeTest = sshHelper.getSecretsById(sshId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.filterType").equalsIgnoreCase("[ALL]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[NON_PRODUCTION_ENVIRONMENTS]"));
        sshHelper.deleteSsh(sshId);
    }
    @Test
    public void testSshOneApplicationProductionEnvironemtUsageScopeApi() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, InlineKey,type,"PROD_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        String sshId=appResponse.getString("data.createSecret.secret.id");



        JsonPath secretsByIdUsageScopeTest = sshHelper.getSecretsById(sshId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[PRODUCTION_ENVIRONMENTS]"));
        sshHelper.deleteSsh(sshId);
    }
    @Test
    public void testSshOneApplicationNonProductionEnvironemtUsageScopeApi() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, InlineKey,type,"NON_PROD_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        String sshId=appResponse.getString("data.createSecret.secret.id");



        JsonPath secretsByIdUsageScopeTest = sshHelper.getSecretsById(sshId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.filterType").equalsIgnoreCase("[NON_PRODUCTION_ENVIRONMENTS]"));
        sshHelper.deleteSsh(sshId);
    }
    @Test
    public void testSshOneApplicationLocalEnvironemtUsageScopeApi() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, InlineKey,type,"LOCAL_ENV_SINGLE_APP","6_Ql-WqYQsG7o6_0dx4k3Q");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        JsonPath secretsByIdUsageScopeTest = sshHelper.getSecretsById(sshId);
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.application.appId").equalsIgnoreCase("[6_Ql-WqYQsG7o6_0dx4k3Q]"));
        Assert.assertTrue(secretsByIdUsageScopeTest.getString("data.secret.usageScope.appEnvScopes.environment.envId").equalsIgnoreCase("[lNnSwhWlQO2D4RcrRDr3LQ]"));
        sshHelper.deleteSsh(sshId);
    }
    @Test
    public void testSshAllApplicationLocalEnvironemtUsageScopeApi() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponse = sshHelper.createSshKey(sshName, userName, InlineKey,type,"LOCAL_ENV_ALL","");
        String errorMessage="Exception while fetching data (/createSecret) : Invalid request: EnvId cannot be supplied with app filterType ALL";
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));

    }
    @Test
    public void testCreateSshUsingKerberosKeyTabAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String principal = commonHelper.createRandomName("test");
        String realm = commonHelper.createRandomName("test_realm");
        String path = "/home/user/path";
        JsonPath appResponse = sshHelper.createSshUsingKerberosKeytab(sshName, principal, realm, path, "PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(sshName));

        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshUsingKerberosKeyTabAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String principal = commonHelper.createRandomName("test");
        String realm = commonHelper.createRandomName("test_realm");
        String path = "/home/user/path";
        JsonPath appResponse = sshHelper.createSshUsingKerberosKeytab(sshName, principal, realm, path, "PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newPrincipal=principal+"new";
        String newRealm=realm+"new";
        String newPath=path+"new";
        JsonPath appResponseUpdate = sshHelper.updateSshUsingKerberosKeytab(newSshName, newPrincipal, newRealm, newPath, sshId, "PROD_ALL","");
        Assert.assertTrue(!appResponseUpdate.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponseUpdate.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testCreateSshUsingKerberosPasswordAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String principal = commonHelper.createRandomName("test");
        String realm = commonHelper.createRandomName("test_realm");
        JsonPath appResponse = sshHelper.createSshUsingKerberosPassword(sshName, principal, realm, Password, "PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        Assert.assertTrue(!appResponse.getString("data.createSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createSecret.secret.name").equalsIgnoreCase(sshName));

        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshUsingKerberosPassword() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String principal = commonHelper.createRandomName("test");
        String realm = commonHelper.createRandomName("test_realm");
        JsonPath appResponse = sshHelper.createSshUsingKerberosPassword(sshName, principal, realm, Password, "PROD_ALL","");
        String sshId=appResponse.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String newPrincipal=principal+"new";
        String newRealm=realm+"new";
        JsonPath appResponseUpdate = sshHelper.updateSshUsingKerberosPassword(newSshName, newPrincipal, newRealm, Password, sshId, "PROD_ALL","");
        Assert.assertTrue(!appResponseUpdate.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponseUpdate.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshWithInlineToKerberosAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponseCreate = sshHelper.createSshKey(sshName, userName, InlineKey,type,"PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String principal = commonHelper.createRandomName("test");
        String realm = commonHelper.createRandomName("test_realm");
        JsonPath appResponse = sshHelper.updateSshUsingKerberosPassword(newSshName,principal,realm,Password,sshId,"PROD_ALL","");
        Assert.assertTrue(!appResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testUpdateSshKerberosToSSHWithPassword() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String principal = commonHelper.createRandomName("test");
        String realm = commonHelper.createRandomName("test_realm");
        JsonPath appResponseCreate = sshHelper.createSshUsingKerberosPassword(sshName, principal, realm, Password, "PROD_ALL","");
        String sshId=appResponseCreate.getString("data.createSecret.secret.id");
        String newSshName=sshName+"new";
        String userName = commonHelper.createRandomName("test");
        SshType type=SshType.INLINE;
        JsonPath appResponse = sshHelper.updateSshKey(newSshName,userName,InlineKey,sshId,type,"PROD_ALL","");
        Assert.assertTrue(!appResponse.getString("data.updateSecret.secret.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.name").equalsIgnoreCase(newSshName));
        Assert.assertTrue(appResponse.getString("data.updateSecret.secret.authenticationType.userName").equalsIgnoreCase(userName));
        sshHelper.deleteSsh(sshId);

    }
    @Test
    public void testCreateSshUsingKerberosEmptyPasswordAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String principal = commonHelper.createRandomName("test");
        String realm = commonHelper.createRandomName("test_realm");
        String EmptyPassword="";
        JsonPath appResponse = sshHelper.createSshUsingKerberosPassword(sshName, principal, realm, EmptyPassword, "PROD_ALL","");
        String errorMessage="Exception while fetching data (/createSecret) : Invalid request: No password secret id provided for TGT generation using password";
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));


    }
    @Test
    public void testCreateSshUsingKerberosEmptyKeyTabAPI() throws IOException {
        String sshName = commonHelper.createRandomName("test_sample");
        String principal = commonHelper.createRandomName("test");
        String realm = commonHelper.createRandomName("test_realm");
        String path = "";
        JsonPath appResponse = sshHelper.createSshUsingKerberosKeytab(sshName, principal, realm, path, "PROD_ALL","");
        String errorMessage="Exception while fetching data (/createSecret) : Invalid request: No key file path provided for the kerberos credential with TGT Generation using key file path";
        Assert.assertTrue(appResponse.getString("errors.message").contains(errorMessage));


    }





}