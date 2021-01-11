package io.harness.rest.helper.cloudproviders.pcf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.cpcrud.CPCrud;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PcfHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath createPCFCp(String pcfCPName, String keySecretId) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(PcfConstants.REQUEST_JSON_PCF_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("pcfCloudProvider");
    pcfCloudProvider.addProperty("name", pcfCPName);
    pcfCloudProvider.addProperty("userName", configPropertis.getConfig("PCF_DX_USER"));
    pcfCloudProvider.addProperty("passwordSecretId", keySecretId);

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

  public JsonPath editPCFCP(String id, String pcfCPName) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(PcfConstants.REQUEST_JSON_PCF_UPDATE);

    // Update the name dynamically
    System.out.println("Created ID is :" + id);
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("pcfCloudProvider");
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

  public JsonPath deletePCFCp(String id) throws IOException {
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

  public JsonPath createPCFCloudProvider(RequestSpecification requestSpecification, String gcpName) {
    try {
      JsonObject cloudProviderData = jReader.readJSONFiles(PcfConstants.REQUEST_PCF_CLOUDPROVIDER_CREATION_JSON);
      JsonObject pcfValue = new JsonObject();
      pcfValue.addProperty("endpointUrl", "api.run.pivotal.io");
      pcfValue.addProperty("password", "yBRUwxotRhmLINbNUphxaA");
      pcfValue.addProperty("type", "PCF");
      pcfValue.addProperty("username", configPropertis.getConfig("PCF_DX_USER"));

      cloudProviderData.addProperty("name", gcpName);
      cloudProviderData.addProperty("accountId", defaultAccountId);
      cloudProviderData.addProperty("category", "CLOUD_PROVIDER");
      cloudProviderData.add("usageRestrictions", null);
      cloudProviderData.add("value", pcfValue);

      requestSpecification.body(cloudProviderData.toString());
      Response cloudProviderResponse =
          genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_PCF_CLOUDPROVIDER_CREATION);
      return cloudProviderResponse.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }
}
