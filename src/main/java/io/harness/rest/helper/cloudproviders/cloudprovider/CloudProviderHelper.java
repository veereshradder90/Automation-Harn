package io.harness.rest.helper.cloudproviders.cloudprovider;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.secrets.encryptedFile.EncryptedFileConstants;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CloudProviderHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  public Map<String, String> getCloudProvidersList() {
    Response response =
        GenericRequestBuilder.getRequestSpecificationObject().get(CloudProviderConstants.URI_GET_CLOUDPROVIDERS);
    Map<String, String> cloudProviders = new HashMap<String, String>();
    if (response.getStatusCode() == 200) {
      cloudProviders = responseParser.getResponseMapForGivenKeys(response, "resource", "name", "uuid");
    } else {
      log.info("Error while fetching Cloud Providers List");
    }
    return cloudProviders;
  }

  public Response deleteCloudProviderByName(String cloudProviderName) {
    return deleteCloudProviderById(getCloudProvidersList().get(cloudProviderName));
  }

  public String getCloudProviderId(String cloudProviderName) {
    String uuid = "";
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("search[0][field]", "category");
    requestSpecification.queryParam("search[0][op]", "IN");
    requestSpecification.queryParam("search[0][value]", "CLOUD_PROVIDER");
    Response secretResponse =
        genericRequestBuilder.getCall(requestSpecification, CloudProviderConstants.URI_GET_CLOUDPROVIDERS);
    HashMap<String, String> map = new HashMap<>();
    for (int i = 0; i < ((ArrayList) secretResponse.path("resource.response.name")).size(); i++) {
      map.put(((ArrayList) secretResponse.path("resource.response.name")).get(i).toString(),
          ((ArrayList) secretResponse.path("resource.response.uuid")).get(i).toString());
    }
    log.info("uuid is " + map.get(cloudProviderName));
    return uuid;
  }

  public Response deleteCloudProviderById(String cloudProviderId) {
    RequestSpecification requestObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestObject.pathParam("cloudproviderId", cloudProviderId);
    Response response =
        GenericRequestBuilder.deleteCall(requestObject, CloudProviderConstants.URI_CLOUDPROVIDER_DELETE);
    return response;
  }
}