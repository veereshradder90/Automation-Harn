package io.harness.test.rest.secrets;
import com.bettercloud.vault.*;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.Vault;
import com.bettercloud.vault.response.LogicalResponse;
import com.bettercloud.vault.response.AuthResponse;
import io.harness.rest.helper.secrets.vault.VaultHelper;
import io.harness.rest.utils.SecretsProperties;
import io.harness.rest.helper.CommonHelper;
import io.harness.test.rest.base.AbstractTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Boopesh.Shanmugam
 * @apiNote Contains the test cases for WinRM secrets in Secret managers
 */


@Test(groups = {"PL","QA"})
public class VaultAPITest extends AbstractTest {

    VaultHelper vaultHelper = new VaultHelper();
    CommonHelper commonHelper =new CommonHelper();

    public VaultAPITest() throws VaultException {
    }

    @Test
    public void createTokenVaultTest() throws VaultException {
        final AuthResponse response = vaultHelper.createToken();
        final String token = response.getAuthClientToken();
        System.out.println(token);
        Assert.assertEquals(response.getRestResponse().getStatus(),200);

    }
    @Test
    public void createTokenVaultWithPeriodTest() throws VaultException {
        final AuthResponse response = vaultHelper.createTokenWithLease("1h");
        final String token = response.getAuthClientToken();
        System.out.println(token);
        long time=response.getAuthLeaseDuration();
        Assert.assertEquals(response.getRestResponse().getStatus(),200);
        System.out.println(time);

    }
    @Test
    public void writeSecret() throws VaultException {
        String Key = commonHelper.createRandomName("testKey");
        String Value = commonHelper.createRandomName("testValue");
        String Path="hello";
        final LogicalResponse writeResponse = vaultHelper.createSecret(Key,Value,Path);
        Assert.assertEquals(writeResponse.getRestResponse().getStatus(),200);


    }
    @Test
    public void readSecret() throws VaultException {
        String Key = commonHelper.createRandomName("testKey");
        String Value = commonHelper.createRandomName("testValue");
        String Path="hello";
        final LogicalResponse writeResponse = vaultHelper.createSecret(Key,Value,Path);
        Assert.assertEquals(writeResponse.getRestResponse().getStatus(),200);

        final String value = vaultHelper.readToken(Path,Key);
        Assert.assertEquals(value,Value);

    }






}
