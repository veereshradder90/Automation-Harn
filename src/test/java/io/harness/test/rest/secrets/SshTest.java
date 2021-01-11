package io.harness.test.rest.secrets;
import io.harness.test.rest.base.AbstractTest;
import io.harness.rest.helper.secrets.ssh.SshHelper;
import io.restassured.path.json.JsonPath;
import io.harness.rest.helper.secrets.ssh.SshType;
import io.harness.rest.helper.CommonHelper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Boopesh.Shanmugam
 * @apiNote Contains the test cases for WinRM secrets in Secret managers
 */

@Test(groups = {"PL","QA"})
public class SshTest extends AbstractTest {
    SshHelper sshHelper = new SshHelper();

    @Test
    public void createSshKeyUsingInlineTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String key = "HCPfmNBZT3axyf5j7vtsBg";
        SshType type=SshType.INLINE;
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username, key, type);
        String uuid = sshResponse.getString("resource.uuid");  // fetching the ssh id once it is created
        Assert.assertTrue(sshResponse.getString("resource.name").equalsIgnoreCase(sshName));
        Assert.assertTrue(sshResponse.getString("resource.value.userName").equalsIgnoreCase(username));

        // assertion through AssertJ:
        assertThat(sshResponse.getString("resource.name")).isEqualTo(sshName);
        sshHelper.deleteSshKey(uuid);       // deleting after the test is done

    }

    @Test
    public void editSshKeyUsingInlineTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String key = "HCPfmNBZT3axyf5j7vtsBg";
        SshType type=SshType.INLINE;
        // create ssh key
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username,key, type);
        String uuid = sshResponse.getString("resource.uuid");

        // edit
        String newsshName = sshName + "new";
        String newusername = username + "new";
        JsonPath editResponse = sshHelper.editSshKey(newsshName, newusername, key, uuid, type);
        assertThat(editResponse.getString("resource.name")).isEqualTo(newsshName);
        assertThat(editResponse.getString("resource.value.userName")).isEqualTo(newusername);
        sshHelper.deleteSshKey(uuid);       // deleting the key after the test is done
    }

    @Test
    public void deleteSshCreatedUsingInlineKeyTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String key = "HCPfmNBZT3axyf5j7vtsBg";
        SshType type=SshType.INLINE;
        // create ssh key
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username, key, type);
        String uuid = sshResponse.getString("resource.uuid");

        // delete app
        Response deleteResponse = sshHelper.deleteSshKey(uuid);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(200);
    }
    @Test
    public void createSshKeyUsingKeyPathTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String path = "test/key";
        SshType type=SshType.PATH;
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username, path, type);
        String uuid = sshResponse.getString("resource.uuid");  // fetching the ssh id once it is created
        Assert.assertTrue(sshResponse.getString("resource.name").equalsIgnoreCase(sshName));
        Assert.assertTrue(sshResponse.getString("resource.value.userName").equalsIgnoreCase(username));

        // assertion through AssertJ:
        assertThat(sshResponse.getString("resource.name")).isEqualTo(sshName);
        sshHelper.deleteSshKey(uuid);       // deleting after the test is done

    }
    @Test
    public void editSshKeyUsingKeyPathTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String path = "test/key";
        SshType type=SshType.PATH;
        // create ssh key
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username, path, type);
        String uuid = sshResponse.getString("resource.uuid");

        // edit
        String newsshName = sshName + "new";
        String newusername = username + "new";
        String newpath = path + "new";
        JsonPath editResponse = sshHelper.editSshKey(newsshName, newusername, newpath, uuid, type);
        assertThat(editResponse.getString("resource.name")).isEqualTo(newsshName);
        assertThat(editResponse.getString("resource.value.userName")).isEqualTo(newusername);
        assertThat(editResponse.getString("resource.value.keyPath")).isEqualTo(newpath);
        sshHelper.deleteSshKey(uuid);       // deleting the key after the test is done
    }
    @Test
    public void deleteSshCreatedUsingKeyPathTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String path = "test/key";
        SshType type=SshType.PATH;
        // create ssh key
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username, path, type);
        String uuid = sshResponse.getString("resource.uuid");

        // delete app
        Response deleteResponse = sshHelper.deleteSshKey(uuid);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(200);
    }
    @Test
    public void createSshKeyUsingPasswordTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String password = "znzmwlJVR5i9VERDhL3J2Q";
        SshType type=SshType.PASSWORD;
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username, password, type);
        String uuid = sshResponse.getString("resource.uuid");  // fetching the ssh id once it is created
        Assert.assertTrue(sshResponse.getString("resource.name").equalsIgnoreCase(sshName));
        Assert.assertTrue(sshResponse.getString("resource.value.userName").equalsIgnoreCase(username));

        // assertion through AssertJ:
        assertThat(sshResponse.getString("resource.name")).isEqualTo(sshName);
        assertThat(sshResponse.getString("resource.value.userName")).isEqualTo(username);
        sshHelper.deleteSshKey(uuid);       // deleting after the test is done

    }
    @Test
    public void editSshKeyUsingPasswordTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String password = "znzmwlJVR5i9VERDhL3J2Q";
        SshType type=SshType.PATH;
        // create ssh key
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username, password, type);
        String uuid = sshResponse.getString("resource.uuid");

        // edit
        String newsshName = sshName + "new";
        String newusername = username + "new";
        JsonPath editResponse = sshHelper.editSshKey(newsshName, newusername, password, uuid, type);
        assertThat(editResponse.getString("resource.name")).isEqualTo(newsshName);
        assertThat(editResponse.getString("resource.value.userName")).isEqualTo(newusername);
        sshHelper.deleteSshKey(uuid);    // deleting the key after the test is done
    }
    @Test
    public void deleteSshCreatedUsingKeyPasswordTest() {
        String sshName = commonHelper.createRandomName("test-ssh");
        String username = commonHelper.createRandomName("test");
        String password = "znzmwlJVR5i9VERDhL3J2Q";
        SshType type=SshType.PATH;
        // create ssh key
        JsonPath sshResponse = sshHelper.createSshKey(sshName, username, password, type);
        String uuid = sshResponse.getString("resource.uuid");

        // delete app
        Response deleteResponse = sshHelper.deleteSshKey(uuid);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(200);
    }

}