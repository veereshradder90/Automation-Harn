package io.harness.rest.helper.rbac;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.accessmanagement.users.UserGroupHelper;
import io.harness.rest.helper.accessmanagement.users.UserHelper;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;

public class AccountRBACImpl implements AccountRBACInterface {
  CommonHelper commonHelper = new CommonHelper();
  ApplicationHelper applicationHelper = new ApplicationHelper();
  PermissionDataHelper permissionDataHelper = new PermissionDataHelper();
  UserGroupHelper userGroupHelper = new UserGroupHelper();
  @Override
  public boolean createApplicationPermission(String bearerToken, int expectedStatusCode) {
    HashMap<Object, Object> createApplicationPermissionData =
        permissionDataHelper.getACreateApplicationPermissionData();
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecificationObject.auth().oauth2(bearerToken);
    Response response = applicationHelper.createApplication(
        requestSpecificationObject, createApplicationPermissionData.get("appName").toString());
    return response.getStatusCode() == expectedStatusCode;
  }

  @Override
  public boolean deleteApplicationPermission(String bearerToken, int expectedStatusCode) {
    HashMap<Object, Object> deleteApplicationPermissionData =
        permissionDataHelper.getADeleteApplicationPermissionData();
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecificationObject.auth().oauth2(bearerToken);
    Response response = applicationHelper.deleteApp(
        requestSpecificationObject, deleteApplicationPermissionData.get("appName").toString(), "name");
    return response.getStatusCode() == expectedStatusCode;
  }

  @Override
  public boolean readUserGroupPermission(String bearerToken, int expectedStatusCode) {
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecificationObject.auth().oauth2(bearerToken);
    Response userGroupsResponse = userGroupHelper.getUserGroupsResponse(requestSpecificationObject);
    return userGroupsResponse.getStatusCode() == expectedStatusCode;
  }

  @Override
  public boolean manageUserGroupPermission(String bearerToken, int expectedStatusCode) {
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecificationObject.auth().oauth2(bearerToken);
    UserHelper userHelper = new UserHelper();
    String[] users = new String[] {commonHelper.createRandomName("rbac-test-pl") + "@mailinator.com"};
    String[] userGroup = new String[] {};
    Response response = userHelper.addUser(requestSpecificationObject, users, userGroup);
    return response.getStatusCode() == expectedStatusCode;
  }

  @Override
  public boolean manageAdminAndOtherAccountPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean manageTagsPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean manageTemplateLibraryPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean manageConnectorsPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean manageCloudProvidersPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean manageSecretsPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean manageSecretManagerPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean viewAuditTrialPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean ceViewPermission(String bearerToken, int expectedStatusCode) {
    return false;
  }

  @Override
  public boolean ceOwnerePermission(String bearerToken, int expectedStatusCode) {
    return false;
  }
}
