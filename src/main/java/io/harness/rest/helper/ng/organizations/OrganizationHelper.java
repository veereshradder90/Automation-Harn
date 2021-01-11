package io.harness.rest.helper.ng.organizations;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * author: shaswat.deep
 */
@Slf4j
public class OrganizationHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  // Search Organization
  public Response getOrgListSearch(Map<String, Object> queryParams) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    log.info("List of Orgs for the given Account Id: " + queryParams.get("accountIdentifier"));
    Response response = genericRequestBuilder.getCall(requestSpecification, OrgConstants.URI_ACCOUNT_ORG);

    return response;
  }

  // Get Org List
  public Response getOrgList(String accountIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("accountIdentifier", accountIdentifier);
    log.info("List of Org List for the account: " + accountIdentifier);
    Response response = genericRequestBuilder.getCall(requestSpecification, OrgConstants.URI_ORG_LIST);

    return response;
  }

  // Create an org
  public Response createOrg(String accountIdentifier, String body) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("accountIdentifier", accountIdentifier);
    requestSpecification.body(body);
    log.info("Add an org in the account: " + accountIdentifier);
    Response response = genericRequestBuilder.postCall(requestSpecification, OrgConstants.URI_ORG_LIST);

    return response;
  }

  // Delete Org by Identifier
  public Response deleteOrgByIdentifiers(String accountIdentifier, String orgIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("accountIdentifier", accountIdentifier);
    requestSpecification.pathParam("orgIdentifier", orgIdentifier);
    log.info("Delete the Org for the account: " + accountIdentifier);
    Response response = genericRequestBuilder.deleteCall(requestSpecification, OrgConstants.URI_ORG_IDENTIFIERS);

    return response;
  }

  // Get an Org details by Identifier
  public Response getOrgByIdentifiers(String accountIdentifier, String orgIdentifier) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("accountIdentifier", accountIdentifier);
    requestSpecification.pathParam("orgIdentifier", orgIdentifier);
    log.info("Get the Org details for the account: " + accountIdentifier);
    Response response = genericRequestBuilder.getCall(requestSpecification, OrgConstants.URI_ORG_IDENTIFIERS);

    return response;
  }

  // Update the org details
  public Response updateOrgByIdentifiers(String accountIdentifier, String orgIdentifier, String body) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObjectNG();
    requestSpecification.pathParam("accountIdentifier", accountIdentifier);
    requestSpecification.pathParam("orgIdentifier", orgIdentifier);
    requestSpecification.body(body);
    log.info("Update the Org details for the account: " + accountIdentifier);
    Response response = genericRequestBuilder.putCall(requestSpecification, OrgConstants.URI_ORG_IDENTIFIERS);

    return response;
  }
}
