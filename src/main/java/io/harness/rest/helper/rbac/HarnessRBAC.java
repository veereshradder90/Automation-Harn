package io.harness.rest.helper.rbac;

import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HarnessRBAC {
  AccountRBACInterface accountRBACInterface = new AccountRBACImpl();

  public void testAccountRBAC(String bearerToken, ArrayList<AccountPermissions> includePermission) {
    List<AccountPermissions> tempAccountPermissions = Arrays.asList(AccountPermissions.values());
    ArrayList<AccountPermissions> accountPermissionsList = new ArrayList<>(tempAccountPermissions);
    System.out.println(accountPermissionsList);
    accountPermissionsList.removeAll(includePermission);
    System.out.println(accountPermissionsList);

    for (AccountPermissions accountPermission : includePermission) {
      boolean result = accountLevelRBAC(accountPermission, bearerToken, 200);
      Assert.assertTrue(result, accountPermission + " is looking good");
    }

    for (AccountPermissions accountPermission : accountPermissionsList) {
      boolean result = accountLevelRBAC(accountPermission, bearerToken, 400);
      Assert.assertTrue(result, accountPermission + " is looking good");
    }
  }

  public boolean accountLevelRBAC(AccountPermissions accountPermissions, String bearerToken, int expectedResponseCode) {
    switch (accountPermissions) {
      case CREATE_APPLICATION:
        return accountRBACInterface.createApplicationPermission(bearerToken, expectedResponseCode);

      case DELETE_APPLICATION:
        return accountRBACInterface.deleteApplicationPermission(bearerToken, expectedResponseCode);

      case READ_USER_GROUPS:
        return accountRBACInterface.readUserGroupPermission(bearerToken, expectedResponseCode);

      case MANAGE_USERS_GROUP:
        return accountRBACInterface.manageUserGroupPermission(bearerToken, expectedResponseCode);

      case ADMIN_OTHER_ACCOUNT_FUNCTIONS:

      case MANAGE_TAGS:

      case MANAGE_CLOUD_PROVIDERS:

      case MANAGE_CONNECTORS:

      case MANAGE_SECRET_MANAGERS:

      case MANAGE_TEMPLATE_LIBRARY:

      case MANAGE_SECRETS:

      case VIEW_AUDIT_TRIAL:

      case CE_ADMIN:
        return true;

      case CE_OWNER:
        return true;
      default:
        return true;
    }
  }
}
