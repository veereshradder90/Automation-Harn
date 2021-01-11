package io.harness.rest.utils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.core.RestAssuredClient;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

import static io.harness.rest.core.CoreUtils.defaultAccountId;

@Slf4j
public class GlobalDataProvider {
  HashMap<String, String> cloudProviders = new HashMap<String, String>();
  HashMap<String, String> connectors = new HashMap<String, String>();

  public static RestAssuredClient restClient = new RestAssuredClient();
  public static RequestSpecification defaultClient = GenericRequestBuilder.getRequestSpecificationObject();

  public GlobalDataProvider() {
    if (cloudProviders.isEmpty()) {
      cloudProviders = getCloudProviders(defaultAccountId);
    }
    if (connectors.isEmpty()) {
      connectors = getConnectors(defaultAccountId);
    }
  }

  public static HashMap<String, String> getCloudProviders(String accountId) {
    defaultClient.queryParam("accountId", accountId);
    defaultClient.queryParam("search[0][field]", "category");
    defaultClient.queryParam("search[0][op]", "IN");
    defaultClient.queryParam("search[0][value]", "CLOUD_PROVIDER");
    Response response = defaultClient.get("/settings");
    HashMap<String, String> cloudProvider = new HashMap<String, String>();

    if (response.getStatusCode() != 200) {
      log.info("Settings CloudProvider API call failed. response:" + response.getBody().asString());
      return cloudProvider;
    }
    log.info("status code:", response.getStatusCode());
    log.info("Settings CloudProvider API response:" + response.asString());
    List<HashMap<String, String>> responseData = response.jsonPath().get("resource.response");
    for (HashMap cloudProviderData : responseData) {
      HashMap<String, String> cloudProviderType = (HashMap<String, String>) cloudProviderData.get("value");
      String cpNameAndType = cloudProviderData.get("name").toString() + "_" + cloudProviderType.get("type");
      cloudProvider.put(cpNameAndType, cloudProviderData.get("uuid").toString());
    }
    return cloudProvider;
  }

  public static HashMap<String, String> getConnectors(String accountId) {
    defaultClient.queryParam("accountId", accountId);
    defaultClient.queryParam("search[0][field]", "category");
    defaultClient.queryParam("search[0][op]", "IN");
    defaultClient.queryParam("search[0][value]", "CONNECTOR");
    defaultClient.queryParam("search[0][value]", "HELM_REPO");
    defaultClient.queryParam("search[0][value]", "AZURE_ARTIFACTS");
    Response response = defaultClient.get("/settings");

    HashMap<String, String> connectors = new HashMap<String, String>();

    if (response.getStatusCode() != 200) {
      log.info("Settings Connector API call failed. response:" + response.getBody().asString());
      return connectors;
    }
    log.info("status code:", response.getStatusCode());
    log.info("Settings Connector API response:" + response.asString());
    List<HashMap<String, String>> responseData = response.jsonPath().get("resource.response");
    for (HashMap connectorData : responseData) {
      HashMap<String, String> connectorType = (HashMap<String, String>) connectorData.get("value");
      String connectorNameAndType = connectorData.get("name").toString() + "_" + connectorType.get("type");
      connectors.put(connectorNameAndType, connectorData.get("uuid").toString());
    }
    return connectors;
  }

  public String getConnectorUUID(String name, String type) {
    String connectorNameAndType = name + "_" + type;
    log.info("connector name :" + name + " type:" + type + " UUID is:" + connectors.get(connectorNameAndType));
    return connectors.get(connectorNameAndType);
  }

  public String getCloudProviderUUID(String name, String type) {
    String cpNameAndType = name + "_" + type;
    log.info("cloud provider name :" + name + " type:" + type + " UUID is:" + connectors.get(cpNameAndType));
    return cloudProviders.get(cpNameAndType);
  }
}
