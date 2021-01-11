package io.harness.rest.helper.cloudproviders.k8s;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.cpcrud.CPCrud;
import io.harness.rest.helper.cloudproviders.gcp.GCPConstants;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class K8SHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();

  String masterUrl = configPropertis.getConfig("K8S_MASTER_URL");

  public JsonPath createK8SDelegateCp(String k8sName, String type, String appId) throws IOException {
    String delegateName = "qa-target-automation-1";
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(K8SConstants.REQUEST_JSON_K8S_DELEGATE_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject k8sCloudProvider = cloudProvider.getAsJsonObject("k8sCloudProvider");
    k8sCloudProvider.addProperty("name", k8sName);

    // Add delegate name
    JsonObject inheritClusterDetails = k8sCloudProvider.getAsJsonObject("inheritClusterDetails");
    inheritClusterDetails.addProperty("delegateName", delegateName);

    secretsGraphQLHelper.directUsageScopeInCp(type, appId, inheritClusterDetails);

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

  public JsonPath editK8SDelegateCp(String id, String k8sName, String type, String appId) throws IOException {
    String delegateName = "qa-target-automation-1";
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(K8SConstants.REQUEST_JSON_K8S_DELEGATE_UPDATE);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);

    JsonObject k8sCloudProvider = cloudProvider.getAsJsonObject("k8sCloudProvider");
    k8sCloudProvider.addProperty("name", k8sName);

    // Add delegate name
    JsonObject inheritClusterDetails = k8sCloudProvider.getAsJsonObject("inheritClusterDetails");
    inheritClusterDetails.addProperty("delegateName", delegateName);

    secretsGraphQLHelper.directUsageScopeInCp(type, appId, inheritClusterDetails);

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

  public JsonPath createK8SUserNamePasswordCp(String k8sName, String keySecretId) throws IOException {
    String userName = "admin";

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(K8SConstants.REQUEST_JSON_K8S_MANUAL_USERNAME_PASSWORD_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject k8sCloudProvider = cloudProvider.getAsJsonObject("k8sCloudProvider");
    k8sCloudProvider.addProperty("name", k8sName);

    // Add delegate name
    JsonObject manualClusterDetails = k8sCloudProvider.getAsJsonObject("manualClusterDetails");
    manualClusterDetails.addProperty("masterUrl", masterUrl);

    JsonObject userObject = manualClusterDetails.getAsJsonObject("usernameAndPassword");
    userObject.addProperty("userName", userName);
    userObject.addProperty("passwordSecretId", keySecretId);

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

  public JsonPath editK8SUserNamePasswordCp(String id, String k8sName, String keySecretId) throws IOException {
    String userName = "admin";

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(K8SConstants.REQUEST_JSON_K8S_MANUAL_USERNAME_PASSWORD_UPDATE);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);
    JsonObject k8sCloudProvider = cloudProvider.getAsJsonObject("k8sCloudProvider");
    k8sCloudProvider.addProperty("name", k8sName);

    // Add delegate name
    JsonObject manualClusterDetails = k8sCloudProvider.getAsJsonObject("manualClusterDetails");
    manualClusterDetails.addProperty("masterUrl", masterUrl);

    JsonObject userObject = manualClusterDetails.getAsJsonObject("usernameAndPassword");
    userObject.addProperty("userName", userName);
    userObject.addProperty("passwordSecretId", keySecretId);

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

  public JsonPath createK8SServiceIdCp(String k8sName, String keySecretId) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(K8SConstants.REQUEST_JSON_K8S_MANUAL_SERVICE_ID_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject k8sCloudProvider = cloudProvider.getAsJsonObject("k8sCloudProvider");
    k8sCloudProvider.addProperty("name", k8sName);

    // Add delegate name
    JsonObject manualClusterDetails = k8sCloudProvider.getAsJsonObject("manualClusterDetails");
    manualClusterDetails.addProperty("masterUrl", masterUrl);

    JsonObject serviceAccountToken = manualClusterDetails.getAsJsonObject("serviceAccountToken");
    serviceAccountToken.addProperty("serviceAccountTokenSecretId", keySecretId);

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

  public JsonPath editK8SServiceIdCp(String id, String k8sName, String keySecretId) throws IOException {
    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_UPDATE);
    JsonObject appReqData = jReader.readJSONFiles(K8SConstants.REQUEST_JSON_K8S_MANUAL_SERVICE_ID_UPDATE);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);
    JsonObject k8sCloudProvider = cloudProvider.getAsJsonObject("k8sCloudProvider");
    k8sCloudProvider.addProperty("name", k8sName);

    // Add delegate name
    JsonObject manualClusterDetails = k8sCloudProvider.getAsJsonObject("manualClusterDetails");
    manualClusterDetails.addProperty("masterUrl", masterUrl);

    JsonObject serviceAccountToken = manualClusterDetails.getAsJsonObject("serviceAccountToken");
    serviceAccountToken.addProperty("serviceAccountTokenSecretId", keySecretId);

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

  public JsonPath createK8sOidcCp(String k8sName, String passwordSecretId, String clientIdSecretId,
      String clientSecretSecretId) throws IOException {
    
    String identityProviderUrl = configPropertis.getConfig("K8S_OIDC_IDENTITIY_PROVIDER_URL");
    String userName = configPropertis.getConfig("K8S_OIDC_USERNAME");

    System.out.println("passwordSecretId " + passwordSecretId);
    System.out.println("clientIdSecretId " + clientIdSecretId);
    System.out.println("clientSecretSecretId " + clientSecretSecretId);

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(K8SConstants.REQUEST_JSON_K8S_MANUAL_OIDC_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    JsonObject k8sCloudProvider = cloudProvider.getAsJsonObject("k8sCloudProvider");
    k8sCloudProvider.addProperty("name", k8sName);

    // Add delegate name
    JsonObject manualClusterDetails = k8sCloudProvider.getAsJsonObject("manualClusterDetails");
    manualClusterDetails.addProperty("masterUrl", masterUrl);

    JsonObject serviceAccountToken = manualClusterDetails.getAsJsonObject("oidcToken");
    serviceAccountToken.addProperty("identityProviderUrl", identityProviderUrl);
    serviceAccountToken.addProperty("userName", userName);
    serviceAccountToken.addProperty("passwordSecretId", passwordSecretId);
    serviceAccountToken.addProperty("clientIdSecretId", clientIdSecretId);
    serviceAccountToken.addProperty("clientSecretSecretId", clientSecretSecretId);

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

  public JsonPath editK8sOidcCp(String id, String k8sName) throws IOException {
    String identityProviderUrl = configPropertis.getConfig("K8S_OIDC_IDENTITIY_PROVIDER_URL");
    String userName = configPropertis.getConfig("K8S_OIDC_USERNAME");
    String passwordSecretId = secretsProperties.getSecret("K8S_OIDC_PASSWORD_SECRET");
    String clientIdSecretId = secretsProperties.getSecret("K8S_OIDC_CLIENT_SECRET");
    String clientSecretSecretId = secretsProperties.getSecret("K8S_OIDC_CLIENT_SECRET_SECRET");

    File file = new File(System.getProperty("user.dir") + CPCrud.REQUEST_JSON_CP_CREATION);
    JsonObject appReqData = jReader.readJSONFiles(K8SConstants.REQUEST_JSON_K8S_MANUAL_OIDC_CREATION);

    // Update the name dynamically
    JsonObject cloudProvider = appReqData.getAsJsonObject("cloudProvider");
    cloudProvider.addProperty("cloudProviderId", id);
    JsonObject k8sCloudProvider = cloudProvider.getAsJsonObject("k8sCloudProvider");
    k8sCloudProvider.addProperty("name", k8sName);

    // Add delegate name
    JsonObject manualClusterDetails = k8sCloudProvider.getAsJsonObject("manualClusterDetails");
    manualClusterDetails.addProperty("masterUrl", masterUrl);

    JsonObject serviceAccountToken = manualClusterDetails.getAsJsonObject("oidcToken");
    serviceAccountToken.addProperty("identityProviderUrl", identityProviderUrl);
    serviceAccountToken.addProperty("userName", userName);
    serviceAccountToken.addProperty("passwordSecretId", passwordSecretId);
    serviceAccountToken.addProperty("clientIdSecretId", clientIdSecretId);
    serviceAccountToken.addProperty("clientSecretSecretId", clientSecretSecretId);

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

  public JsonPath deleteK8SCp(String id) throws IOException {
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

  public JsonPath createKubernetesCloudProvider(RequestSpecification requestSpecification, String k8sName) {
    try {
      JsonObject cloudProviderData = jReader.readJSONFiles(K8SConstants.REQUEST_KUBERNETES_CP_CREATION_JSON);
      JsonObject k8sCredentials = new JsonObject();
      JsonObject ccmConfig = new JsonObject();
      ccmConfig.addProperty("cloudCostEnabled", true);
      ccmConfig.addProperty("skipK8sEventCollection", false);
      k8sCredentials.addProperty("authType", "SERVICE_ACCOUNT");
      k8sCredentials.add("ccmConfig", ccmConfig);
      k8sCredentials.addProperty("masterUrl", "https://34.67.13.218/");
      k8sCredentials.addProperty("serviceAccountToken", "ondf5CKGSU2ln05VvYnO5Q");
      k8sCredentials.addProperty("type", "KUBERNETES_CLUSTER");
      k8sCredentials.addProperty("useKubernetesDelegate", false);
      cloudProviderData.addProperty("name", k8sName);
      cloudProviderData.addProperty("accountId", defaultAccountId);
      cloudProviderData.addProperty("category", "CLOUD_PROVIDER");
      cloudProviderData.add("usageRestrictions", null);
      cloudProviderData.add("value", k8sCredentials);
      requestSpecification.body(cloudProviderData.toString());
      Response cloudProviderResponse =
          genericRequestBuilder.postCall(requestSpecification, K8SConstants.URI_K8S_CLOUDPROVIDER_CREATION);
      return cloudProviderResponse.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }
}
