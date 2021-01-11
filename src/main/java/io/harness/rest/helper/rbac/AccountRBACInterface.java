package io.harness.rest.helper.rbac;

public interface AccountRBACInterface {
  public boolean createApplicationPermission(String bearerToken, int expectedStatusCode);
  public boolean deleteApplicationPermission(String bearerToken, int expectedStatusCode);
  public boolean readUserGroupPermission(String bearerToken, int expectedStatusCode);
  public boolean manageUserGroupPermission(String bearerToken, int expectedStatusCode);
  public boolean manageAdminAndOtherAccountPermission(String bearerToken, int expectedStatusCode);
  public boolean manageTagsPermission(String bearerToken, int expectedStatusCode);
  public boolean manageTemplateLibraryPermission(String bearerToken, int expectedStatusCode);
  public boolean manageConnectorsPermission(String bearerToken, int expectedStatusCode);
  public boolean manageCloudProvidersPermission(String bearerToken, int expectedStatusCode);
  public boolean manageSecretsPermission(String bearerToken, int expectedStatusCode);
  public boolean manageSecretManagerPermission(String bearerToken, int expectedStatusCode);
  public boolean viewAuditTrialPermission(String bearerToken, int expectedStatusCode);
  public boolean ceViewPermission(String bearerToken, int expectedStatusCode);
  public boolean ceOwnerePermission(String bearerToken, int expectedStatusCode);
}
