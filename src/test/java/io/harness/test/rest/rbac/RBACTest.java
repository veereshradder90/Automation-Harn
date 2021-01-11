package io.harness.test.rest.rbac;

import io.harness.rest.helper.rbac.AccountPermissions;
import io.harness.rest.helper.rbac.HarnessRBAC;
import io.harness.rest.utils.SecretsProperties;
import io.harness.test.rest.base.AbstractTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class RBACTest extends AbstractTest {
  SecretsProperties secretsProperties = new SecretsProperties();

  @Test
  public void testApplicationPermission() {
    String bearerToken =
        loginHelper.loginUser("rbac_pl@mailinator.com", secretsProperties.getSecret("RBAC_PASSWORD_QA"));
    HarnessRBAC rbac = new HarnessRBAC();
    ArrayList<AccountPermissions> accountPermissions = new ArrayList<>();
    accountPermissions.add(AccountPermissions.CREATE_APPLICATION);
    accountPermissions.add(AccountPermissions.DELETE_APPLICATION);
    rbac.testAccountRBAC(bearerToken, accountPermissions);
  }

  @Test
  public void testManageUserPermission() {
    String bearerToken =
        loginHelper.loginUser("rbac_pl1@mailinator.com", secretsProperties.getSecret("RBAC_PASSWORD_QA"));
    HarnessRBAC rbac = new HarnessRBAC();
    ArrayList<AccountPermissions> accountPermissions = new ArrayList<>();
    accountPermissions.add(AccountPermissions.READ_USER_GROUPS);
    accountPermissions.add(AccountPermissions.MANAGE_USERS_GROUP);
    rbac.testAccountRBAC(bearerToken, accountPermissions);
  }
}
