package io.harness.test.rest.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.accessmanagement.users.UserGroupHelper;
import io.harness.rest.helper.accessmanagement.users.UserHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ManageUserGroupsTest extends AbstractTest {
  UserGroupHelper userGroupHelper = new UserGroupHelper();

  UserHelper userHelper = new UserHelper();

  @Test
  public void readUsersTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_manageusergroups@mailinator.com", "Harness@123");
    Response readUsers = userHelper.getUsers(requestSpecificationObject);
    assertThat(readUsers.getStatusCode()).isEqualTo(200);
  }

  @Test
  public void readUserGroupsTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_manageusergroups@mailinator.com", "Harness@123");
    Response readUserGroups = userGroupHelper.getUserGroups(requestSpecificationObject);
    assertThat(readUserGroups.getStatusCode()).isEqualTo(200);
  }

  @Test
  public void inviteUserTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_manageusergroups@mailinator.com", "Harness@123");
    JsonPath inviteUserResponse = userHelper.inviteUser(requestSpecificationObject, "platform@mailinator.com", "");
    assertThat(inviteUserResponse.getString("responseMessages.message")
                   .equalsIgnoreCase("Invalid request: User not authorized"));
  }

  @Test
  public void addUserGroupTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject("rbac_manageusergroups@mailinator.com", "Harness@123");
    JsonPath addUserGroupResponse = userGroupHelper.createUserGroup(requestSpecificationObject, "Platform");
    assertThat(addUserGroupResponse.getString("responseMessages.message")
                   .equalsIgnoreCase("Invalid request: User not authorized"));
  }
}
