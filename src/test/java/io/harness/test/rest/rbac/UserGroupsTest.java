package io.harness.test.rest.rbac;

import io.harness.rest.helper.accessmanagement.users.UserGroupHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserGroupsTest extends AbstractTest {
  UserGroupHelper userGroupHelper = new UserGroupHelper();

  @Test
  public void createUserGroupTest() {
    String userGroupName = commonHelper.createRandomName("UserGroup- ");
    JsonPath userGroupResponse = userGroupHelper.createUserGroup(userGroupName);
    Assert.assertTrue(userGroupResponse.getString("resource.name").equalsIgnoreCase(userGroupName));

    // assertion through AssertJ:
    assertThat(userGroupResponse.getString("resource.name")).isEqualTo(userGroupName);
  }

  @Test
  public void deleteUserGroupTest() {
    String userGroupName = commonHelper.createRandomName("User Group-");

    // create app
    JsonPath createUserGroupResponse = userGroupHelper.createUserGroup(userGroupName);

    String userGroupId = createUserGroupResponse.getString("resource.uuid");

    // delete app
    Response deleteResponse = userGroupHelper.deleteUserGroup(userGroupId);
    assertThat(deleteResponse.getStatusCode()).isEqualTo(200);
  }
}
