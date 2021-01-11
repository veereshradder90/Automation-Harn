package io.harness.rest.helper.cloudproviders.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.cpcrud.CPCrud;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AwsHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();


  public JsonPath createAWSManualCp(String awsCpName, String secretKeySecretId) throws IOException {

    String accessKey =secretsProperties.getSecret("AWS_ACCESS_KEY");

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(AwsConstants.REQUEST_JSON_AWS_MANUAL_CREATION);

    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("awsCloudProvider");
    pcfCloudProvider.addProperty("name", awsCpName);

    JsonObject manualCreds = pcfCloudProvider.getAsJsonObject("manualCredentials");
    manualCreds.addProperty("accessKey", accessKey);
    manualCreds.addProperty("secretKeySecretId", secretKeySecretId);

    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath createAWSDelegateCp(String awsCpName, String delegateTag, String type, String appId)
      throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(AwsConstants.REQUEST_JSON_AWS_DELEGATE_CREATION);

    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("awsCloudProvider");
    pcfCloudProvider.addProperty("name", awsCpName);

    JsonObject ec2IamCredentials = pcfCloudProvider.getAsJsonObject("ec2IamCredentials");
    ec2IamCredentials.addProperty("delegateSelector", delegateTag);

    secretsGraphQLHelper.directUsageScopeInCp(type, appId, ec2IamCredentials);

    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath editAWSDelegateCp(String id, String awsCpName, String delegateTag) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(AwsConstants.REQUEST_JSON_AWS_DELEGATE_UPDATE);

    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);

    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("awsCloudProvider");
    pcfCloudProvider.addProperty("name", awsCpName);

    JsonObject ec2IamCredentials = pcfCloudProvider.getAsJsonObject("ec2IamCredentials");
    ec2IamCredentials.addProperty("delegateSelector", delegateTag);

    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath editAWSManualCp(String id, String awsCpName, String secretKeySecretId) throws IOException {
    String accessKey =secretsProperties.getSecret("AWS_ACCESS_KEY");
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(AwsConstants.REQUEST_JSON_AWS_MANUAL_UPDATE);

    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);

    JsonObject pcfCloudProvider = cloudProvider.getAsJsonObject("awsCloudProvider");
    pcfCloudProvider.addProperty("name", awsCpName);

    JsonObject manualCreds = pcfCloudProvider.getAsJsonObject("manualCredentials");
    manualCreds.addProperty("accessKey", accessKey);
    manualCreds.addProperty("secretKeySecretId", secretKeySecretId);

    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
    System.out.println(mapper.writeValueAsString(jsonNode));

    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath deleteAWSDelegateCP(String id) throws IOException {
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

  public JsonPath createAWSCloudProvider(RequestSpecification requestSpecification, String awsName) {
    try {
      JsonObject cloudProviderData = jReader.readJSONFiles(AwsConstants.REQUEST_AWS_CLOUDPROVIDER_CREATION_JSON);
      JsonObject awsValue = new JsonObject();
      awsValue.addProperty("accessKey", "AKIA4GYQC5QT4KKRT5WO");
      awsValue.addProperty("secretKey", "1IU5xoCxTBSQs7Bswv4NRQ");
      awsValue.addProperty("type", "AWS");
      awsValue.addProperty("useEc2IamCredentials", false);
      cloudProviderData.addProperty("name", awsName);
      cloudProviderData.addProperty("accountId", defaultAccountId);
      cloudProviderData.addProperty("category", "CLOUD_PROVIDER");
      cloudProviderData.add("usageRestrictions", null);
      cloudProviderData.add("value", awsValue);
      requestSpecification.body(cloudProviderData.toString());
      Response cloudProviderResponse =
          genericRequestBuilder.postCall(requestSpecification, AwsConstants.URI_AWS_CLOUDPROVIDER_CREATION);
      return cloudProviderResponse.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }
}
