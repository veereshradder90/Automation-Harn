package io.harness.test.rest.devx;

import io.harness.rest.helper.devxusergroups.UserGroupsHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class UserGroups extends AbstractTest {

    UserGroupsHelper userGroupsHelper = new UserGroupsHelper();

    @Test
    public void testIfUserGroupListingAPIIsWorking() throws IOException {
        JsonPath appResponse = userGroupsHelper.getUserGroupsList();
        Assert.assertTrue(!appResponse.getString("data.userGroups.nodes[0].name").isEmpty());
    }

    @Test
    public void testIfUserGroupByNameIsWorking() throws IOException{
        String userGroupName = "Account Administrator";
        JsonPath appResponse = userGroupsHelper.getUserGroupByNameTest();
        Assert.assertTrue(appResponse.getString("data.userGroupByName.name").equalsIgnoreCase(userGroupName));
    }

    @Test
    public void testIfUserGroupByIdIsWorking() throws IOException{
        String rbacUserId = "IXAeJL1YR6W9ybsUVrm_fg";
        String rbacUserGroupName = commonHelper.createRandomName("DevX-UserGroup-");
        JsonPath userGroupCreate = userGroupsHelper.createUserGroupSanity(rbacUserGroupName,rbacUserId,"PROD_ALL","");
        Assert.assertTrue(!userGroupCreate.getString("data.createUserGroup.userGroup.id").isEmpty());
        String userGroupId = userGroupCreate.getString("data.createUserGroup.userGroup.id");
        JsonPath appResponse = userGroupsHelper.getUserGroupByIdApi(userGroupId);
        Assert.assertTrue(appResponse.getString("data.userGroup.id").equalsIgnoreCase(userGroupId));
    }

    @Test
    public void testIfUserListingAPIIsWorking() throws IOException {
        JsonPath appResponse = userGroupsHelper.getUsersList();
        Assert.assertTrue(!appResponse.getString("data.users.nodes[0].name").isEmpty());
    }

    @Test
    public void testIfUserByNameIsWorking() throws IOException{
        String userName = "Abhinav Singh";
        JsonPath appResponse = userGroupsHelper.getUserByNameTest();
        Assert.assertTrue(appResponse.getString("data.userByName.name").equalsIgnoreCase(userName));
    }

    @Test
    public void testIfUserByIdIsWorking() throws IOException{
        String rbacUserId = "IXAeJL1YR6W9ybsUVrm_fg";
        JsonPath appResponse = userGroupsHelper.getUserByIdApi(rbacUserId);
        Assert.assertTrue(appResponse.getString("data.user.id").equalsIgnoreCase(rbacUserId));
    }

    @Test(enabled = true)
    public void testRbac() throws IOException {
        JsonPath appResponse = userGroupsHelper.getUserGroupsList();
        Assert.assertTrue(!appResponse.getString("data.userGroups.nodes[0].name").isEmpty());
        System.out.println("User at the top :"+appResponse.getString("data.userGroups.nodes[2].name"));

        JsonPath appResponse2 = userGroupsHelper.getUserGroupsListRbac2();
        Assert.assertTrue(!appResponse2.getString("data.userGroups.nodes[0].name").isEmpty());
        System.out.println("User2 at the top :"+appResponse2.getString("data.userGroups.nodes[2].name"));
    }
}
