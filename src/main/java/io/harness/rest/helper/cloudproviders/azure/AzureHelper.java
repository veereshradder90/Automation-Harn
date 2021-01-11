package io.harness.rest.helper.cloudproviders.azure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.UsageScope.UsageScopeHelper;
import io.harness.rest.helper.cloudproviders.cpcrud.CPCrud;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AzureHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  public CommonHelper commonHelper = new CommonHelper();
  SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();
  UsageScopeHelper usageScopeHelper = new UsageScopeHelper();

  String clientId = secretsProperties.getSecret("AZURE_CP_CLIENT_ID");
  String tenantId = secretsProperties.getSecret("AZURE_CP_TENANT_ID");


  public JsonPath createAzureCp(String azureName, String keySecretId) throws IOException {

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(AzureConstants.REQUEST_JSON_AZURE_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("azureCloudProvider");
    pcfCloudProvider.addProperty("name", azureName);
    pcfCloudProvider.addProperty("clientId", clientId);
    pcfCloudProvider.addProperty("tenantId", tenantId);
    pcfCloudProvider.addProperty("keySecretId", keySecretId);

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath editAzureCP(String id, String azureName, String keySecretId) throws IOException {

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(AzureConstants.REQUEST_JSON_AZURE_UPDATE);

    // Update the name dynamically
    System.out.println("Created ID is :" + id);
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("azureCloudProvider");
    pcfCloudProvider.addProperty("name", azureName);
    pcfCloudProvider.addProperty("clientId", clientId);
    pcfCloudProvider.addProperty("tenantId", tenantId);
    pcfCloudProvider.addProperty("keySecretId", keySecretId);

    System.out.println("Json :" + cloudProvider);

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath deleteAzureCp(String id) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_DELETE);
    JsonObject appReqData = jReader.readJSONFiles(CPCrud.QUERY_CP_DELETE);

    System.out.println("Created ID is :" + id);
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);

    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath createAzureCloudProvider(RequestSpecification requestSpecification, String azureName) {
    try {
      JsonObject cloudProviderData = jReader.readJSONFiles(AzureConstants.REQUEST_AZURE_CP_CREATION_JSON);
      JsonObject azureCredentials = new JsonObject();
      azureCredentials.addProperty("clientId", clientId);
      azureCredentials.addProperty("tenantId", tenantId);
      azureCredentials.addProperty("key", "zVRSeyz8TvKJupLZG32KnQ");
      azureCredentials.addProperty("type", "AZURE");
      cloudProviderData.addProperty("name", azureName);
      cloudProviderData.addProperty("accountId", defaultAccountId);
      cloudProviderData.addProperty("category", "CLOUD_PROVIDER");
      cloudProviderData.add("usageRestrictions", null);
      cloudProviderData.add("value", azureCredentials);
      requestSpecification.body(cloudProviderData.toString());
      Response cloudProviderResponse =
          genericRequestBuilder.postCall(requestSpecification, AzureConstants.URI_AZURE_CLOUDPROVIDER_CREATION);
      return cloudProviderResponse.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }
}
