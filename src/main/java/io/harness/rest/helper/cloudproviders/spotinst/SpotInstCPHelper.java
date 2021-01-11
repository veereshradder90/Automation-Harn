package io.harness.rest.helper.cloudproviders.spotinst;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.cpcrud.CPCrud;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SpotInstCPHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath createSpotInstCp(String spotInstCPName, String keySecretId) throws IOException {
    String accountId = configPropertis.getConfig("SPOTINST_ACCOUNT_ID");

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(SpotInstCPConstants.REQUEST_JSON_SPOT_INST_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("spotInstCloudProvider");
    pcfCloudProvider.addProperty("name", spotInstCPName);
    pcfCloudProvider.addProperty("accountId", accountId);

    pcfCloudProvider.addProperty("tokenSecretId", keySecretId);

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

  public JsonPath editSpotInstCP(String id, String spotInstCPName, String keySecretId) throws IOException {
    String accountId = configPropertis.getConfig("SPOTINST_ACCOUNT_ID");

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(SpotInstCPConstants.REQUEST_JSON_SPOT_INST_UPDATE);

    // Update the name dynamically
    System.out.println("Created ID is :" + id);
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("spotInstCloudProvider");
    pcfCloudProvider.addProperty("name", spotInstCPName);
    pcfCloudProvider.addProperty("accountId", accountId);
    pcfCloudProvider.addProperty("tokenSecretId", keySecretId);

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

  public JsonPath deleteSpotInstCp(String id) throws IOException {
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

  public JsonPath createSpotInstCloudProvider(RequestSpecification requestSpecification, String gcpName) {
    try {
      JsonObject cloudProviderData =
          jReader.readJSONFiles(SpotInstCPConstants.REQUEST_SPOTINST_CLOUDPROVIDER_CREATION_JSON);
      JsonObject spotinstValue = new JsonObject();
      spotinstValue.addProperty("spotInstAccountId", "act-d48a11cf");
      spotinstValue.addProperty("spotInstToken", "C20bP8yVT-SSd8MJroBGJQ");
      spotinstValue.addProperty("type", "SPOT_INST");

      cloudProviderData.addProperty("name", gcpName);
      cloudProviderData.addProperty("accountId", defaultAccountId);
      cloudProviderData.addProperty("category", "CLOUD_PROVIDER");
      cloudProviderData.add("usageRestrictions", null);
      cloudProviderData.add("value", spotinstValue);

      requestSpecification.body(cloudProviderData.toString());
      Response cloudProviderResponse =
          genericRequestBuilder.postCall(requestSpecification, SpotInstCPConstants.URI_SPOTINST_CLOUDPROVIDER_CREATION);
      return cloudProviderResponse.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }
}
