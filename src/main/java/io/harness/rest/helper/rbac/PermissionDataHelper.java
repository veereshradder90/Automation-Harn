package io.harness.rest.helper.rbac;

import io.harness.rest.helper.CommonHelper;

import java.util.HashMap;

public class PermissionDataHelper {
  CommonHelper commonHelper = new CommonHelper();
  public HashMap<Object, Object> getACreateApplicationPermissionData() {
    HashMap<Object, Object> properties = new HashMap<>();
    properties.put("appName", commonHelper.createRandomName("rbacapp"));
    return properties;
  }

  public HashMap<Object, Object> getADeleteApplicationPermissionData() {
    HashMap<Object, Object> properties = new HashMap<>();
    properties.put("appName", "TestimDemoApp");
    return properties;
  }

  public HashMap<Object, Object> getReadUserGroupPermissionData() {
    HashMap<Object, Object> properties = new HashMap<>();
    return properties;
  }

  public HashMap<Object, Object> getManagerUserGroupPermissionData(
      HashMap<AccountPermissions, HashMap<Object, Object>> permission) {
    HashMap<Object, Object> properties = new HashMap<>();
    properties.put("userGroupName", commonHelper.createRandomName("rbacug"));
    return properties;
  }
}
