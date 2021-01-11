package io.harness.rest.helper.ng.organizations;

/**
 * author: shaswat.deep
 */
public class OrgConstants {
  public static final String REQUEST_JSON_CV_ORG_DETAILS = "/src/main/resources/project/ng/org/orgDetails.json";

  // Account Organization
  public static final String URI_ACCOUNT_ORG = "/ng/api/organizations";

  // Organizations
  public static final String URI_ORG_LIST = "/ng/api/accounts/{accountIdentifier}/organizations";
  public static final String URI_ORG_IDENTIFIERS = "/ng/api/accounts/{accountIdentifier}/organizations/{orgIdentifier}";
}
