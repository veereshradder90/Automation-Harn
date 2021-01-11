package io.harness.rest.helper.cloudproviders.physicaldata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.azure.AzureConstants;
import io.harness.rest.helper.cloudproviders.cpcrud.CPCrud;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.devxusergroups.UserGroupsConstants;
import io.harness.rest.helper.devxusergroups.UserGroupsHelper;
import io.harness.rest.helper.dxapplicationsgraphql.GraphQLApplicationConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PhysicalDataCPHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();

  public JsonPath createPDCCp(String pcfCPName, String type, String appId) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(PhysicalDataCPConstants.REQUEST_JSON_PHYSICAL_DATA_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("physicalDataCenterCloudProvider");
    pcfCloudProvider.addProperty("name", pcfCPName);

    secretsGraphQLHelper.directUsageScopeInCp(type, appId, pcfCloudProvider);

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

  public JsonPath editPDCCP(String id, String pcfCPName) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(PhysicalDataCPConstants.REQUEST_JSON_PHYSICAL_DATA_UPDATE);

    // Update the name dynamically
    System.out.println("Created ID is :" + id);
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("physicalDataCenterCloudProvider");
    pcfCloudProvider.addProperty("name", pcfCPName);
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

  public JsonPath deletePDCCp(String id) throws IOException {
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

  public JsonPath getCPByNameTest(String cpName) throws IOException {
    File file = new File(System.getProperty("user.dir") + PhysicalDataCPConstants.REQUEST_GET_BY_NAME_CP);
    JsonObject appReqData = jReader.readJSONFiles(PhysicalDataCPConstants.REQUEST_GET_BY_NAME_CP_JSON);

    appReqData.addProperty("name", cpName);
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

  public JsonPath listCloudProviders() throws IOException {
    File file = new File(System.getProperty("user.dir") + PhysicalDataCPConstants.REQUEST_CP_LIST_INST_CREATION);
    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath getCPByIdTest(String awsId) throws IOException {
    File file = new File(System.getProperty("user.dir") + PhysicalDataCPConstants.REQUEST_GET_BY_ID_CP);
    JsonObject appReqData = new JsonObject();

    appReqData.addProperty("name", awsId);
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

  public JsonPath createPhysicalDataCenterCloudProvider(RequestSpecification requestSpecification, String pdcName) {
    try {
      JsonObject cloudProviderData =
          jReader.readJSONFiles(PhysicalDataCPConstants.REQUEST_PHYSICALDATACENTER_CP_CREATION_JSON);
      JsonObject pdcValue = new JsonObject();

      pdcValue.addProperty("type", "PHYSICAL_DATA_CENTER");

      cloudProviderData.addProperty("name", pdcName);
      cloudProviderData.addProperty("accountId", defaultAccountId);
      cloudProviderData.addProperty("category", "CLOUD_PROVIDER");
      cloudProviderData.add("usageRestrictions", null);
      cloudProviderData.add("value", pdcValue);

      requestSpecification.body(cloudProviderData.toString());
      Response cloudProviderResponse = genericRequestBuilder.postCall(
          requestSpecification, PhysicalDataCPConstants.URI_PHYSICALDATACENTERS_CLOUDPROVIDER_CREATION);
      return cloudProviderResponse.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }
}
