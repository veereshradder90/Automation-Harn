package io.harness.rest.helper.cloudproviders.gcp;

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

public class GCPHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

  public JsonPath createGCPCp(String gcpName, String serviceAccountKeySecretId) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(GCPConstants.REQUEST_JSON_GCP_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("gcpCloudProvider");
    pcfCloudProvider.addProperty("name", gcpName);
    pcfCloudProvider.addProperty("serviceAccountKeySecretId", serviceAccountKeySecretId.toString());

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GCPConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath editGCPCP(String id, String pcfCPName, String serviceAccountKeySecretId) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(GCPConstants.REQUEST_JSON_GCP_UPDATE);

    // Update the name dynamically
    System.out.println("Created ID is :" + id);
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("gcpCloudProvider");
    pcfCloudProvider.addProperty("name", pcfCPName);
    pcfCloudProvider.addProperty("serviceAccountKeySecretId", serviceAccountKeySecretId);
    System.out.println("Json :" + cloudProvider);

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GCPConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath deleteGCPCp(String id) throws IOException {
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

  public JsonPath createGoogleCloudProvider(RequestSpecification requestSpecification, String gcpName) {
    try {
      JsonObject cloudProviderData = jReader.readJSONFiles(GCPConstants.REQUEST_GOOGLE_CLOUDPROVIDER_CREATION_JSON);
      JsonObject gcpValue = new JsonObject();
      gcpValue.addProperty("type", "GCP");
      gcpValue.addProperty("serviceAccountKeyFileContent", "rM4k05SqR3SPftBILqwHDg");
      cloudProviderData.addProperty("name", gcpName);
      cloudProviderData.addProperty("accountId", defaultAccountId);
      cloudProviderData.addProperty("category", "CLOUD_PROVIDER");
      cloudProviderData.add("usageRestrictions", null);
      cloudProviderData.add("value", gcpValue);
      requestSpecification.body(cloudProviderData.toString());
      Response cloudProviderResponse =
          genericRequestBuilder.postCall(requestSpecification, GCPConstants.URI_GOOGLE_CLOUDPROVIDER_CREATION);
      return cloudProviderResponse.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }
}
