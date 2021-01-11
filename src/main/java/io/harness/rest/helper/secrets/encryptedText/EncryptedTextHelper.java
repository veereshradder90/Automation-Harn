package io.harness.rest.helper.secrets.encryptedText;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Boopesh.Shanmugam
 * @apiNote Serves as the helper class for EncryptedTextSecrets
 */

@Slf4j
public class EncryptedTextHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();

  public JsonPath createEncryptedText(
      String secretName, String secretManagerId, String secretReference, String type, String appId) throws IOException {
    File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_CREATE_QUERY);
    JsonObject appReqData = jReader.readJSONFiles(EncryptedTextConstants.ENCRYPTED_TEXT_CREATE_REFERENCE_JSON);

    // Update the name dynamically
    JsonObject secret = appReqData.getAsJsonObject("secret");
    JsonObject encryptedText = secret.getAsJsonObject("encryptedText");
    encryptedText.addProperty("name", secretName);
    encryptedText.addProperty("secretReference", secretReference);
    encryptedText.addProperty("secretManagerId", secretManagerId);

    secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, encryptedText);

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath updateEncryptedText(
      String secretName, String secretId, String secretReference, String type, String appId) throws IOException {
    File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_UPDATE_QUERY);
    JsonObject appReqData = jReader.readJSONFiles(EncryptedTextConstants.ENCRYPTED_TEXT_UPDATE_REFERENCE_JSON);

    // Update the name dynamically
    JsonObject secret = appReqData.getAsJsonObject("secret");
    secret.addProperty("secretId", secretId);
    JsonObject encryptedText = secret.getAsJsonObject("encryptedText");
    encryptedText.addProperty("name", secretName);
    encryptedText.addProperty("secretReference", secretReference);

    secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, encryptedText);

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath createEncryptedTextWithValue(
      String secretName, String secretManagerId, String value, String type, String appId) throws IOException {
    File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_CREATE_QUERY);
    JsonObject appReqData = jReader.readJSONFiles(EncryptedTextConstants.ENCRYPTED_TEXT_CREATE_VALUE_JSON);

    // Update the name dynamically
    JsonObject secret = appReqData.getAsJsonObject("secret");
    JsonObject encryptedText = secret.getAsJsonObject("encryptedText");
    encryptedText.addProperty("name", secretName);
    encryptedText.addProperty("value", value);
    encryptedText.addProperty("secretManagerId", secretManagerId);

    secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, encryptedText);

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }
  public JsonPath updateEncryptedTextWithValue(
      String secretName, String secretId, String value, String type, String appId) throws IOException {
    File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_UPDATE_QUERY);
    JsonObject appReqData = jReader.readJSONFiles(EncryptedTextConstants.ENCRYPTED_TEXT_UPDATE_VALUE_JSON);

    // Update the name dynamically
    JsonObject secret = appReqData.getAsJsonObject("secret");
    secret.addProperty("secretId", secretId);
    JsonObject encryptedText = secret.getAsJsonObject("encryptedText");
    encryptedText.addProperty("name", secretName);
    encryptedText.addProperty("value", value);

    secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, encryptedText);

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath deleteEncryptedText(String secretId) throws IOException {
    File file = new File(System.getProperty("user.dir") + SecretsGraphQLConstants.REQUEST_JSON_SECRETS_DELETION_QUERY);
    JsonObject appReqData = jReader.readJSONFiles(SecretsGraphQLConstants.REQUEST_JSON_SECRETS_DELETION_PARAM);

    // Update the name dynamically
    JsonObject secret = appReqData.getAsJsonObject("secret");
    secret.addProperty("secretId", secretId);
    secret.addProperty("secretType", "ENCRYPTED_TEXT");

    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);
    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath getSecretsByNameTest(String secretName) throws IOException {
    File file = new File(System.getProperty("user.dir") + SecretsGraphQLConstants.REQUEST_GET_BY_NAME_SECRET);
    JsonObject appReqData = jReader.readJSONFiles(SecretsGraphQLConstants.REQUEST_GET_BY_NAME_SECRET_JSON);

    appReqData.addProperty("name", secretName);
    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);

    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath getEncryptedTextSecretsByNameSanity(String secretName) throws IOException {
    File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_GET_BY_NAME);
    JsonObject appReqData = new JsonObject();

    appReqData.addProperty("name", secretName);
    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);

    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath getEncryptedTextSecretsByIdSanity(String secretName) throws IOException {
    File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_GET_BY_ID);
    JsonObject appReqData = new JsonObject();

    appReqData.addProperty("name", secretName);
    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);

    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }
  public JsonPath getEncryptedTextSecretsByIdUsageScope(String secretName) throws IOException {
    File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_GET_BY_ID_UsageScope);
    JsonObject appReqData = new JsonObject();

    appReqData.addProperty("name", secretName);
    // Convert the json to ObjectNode which will be passed to create the graphQL payload
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

    // Convert the Query to graphQL accepted format.
    String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(graphqlPayload);

    Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
    return applicationResponse.jsonPath();
  }

  public JsonPath createEncryptedText(
      RequestSpecification requestSpecification, String secretName, String value, String kmsId) {
    try {
      JsonObject secretData = jReader.readJSONFiles(EncryptedTextConstants.REQUEST_ENCRYPTED_TEXT_CREATION_JSON);
      secretData.addProperty("name", secretName);
      secretData.addProperty("kmsId", kmsId);
      secretData.addProperty("scopedToAccount", false);
      secretData.add("usageRestrictions", null);
      secretData.addProperty("value", value);
      requestSpecification.body(secretData.toString());
      Response secretResponse =
          genericRequestBuilder.postCall(requestSpecification, EncryptedTextConstants.URI_ENCRYPTED_TEXT_CREATION);
      return secretResponse.jsonPath();
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }

  public JsonPath getSecretId(String secretName) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("type", "SECRET_TEXT");
    requestSpecification.queryParam("sort[0][field]", "name");
    requestSpecification.queryParam("search[0][op]", "CONTAINS");
    requestSpecification.queryParam("search[0][value]", secretName);
    requestSpecification.queryParam("details", true);
    requestSpecification.queryParam("offset", 0);
    requestSpecification.queryParam("limit", 40);
    Response secretResponse =
        genericRequestBuilder.getCall(requestSpecification, EncryptedTextConstants.URI_LIST_ENCRYPTED_TEXTS);
    log.info("uuid is " + secretResponse.path("resource.response[0].uuid"));
    return secretResponse.jsonPath();
  }

  public JsonPath editEncryptedText(
      String email, String password, String secretName, String newName, String value, String kmsId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    String bearerToken = restClient.loginUser(email, password);
    requestSpecification.auth().oauth2(bearerToken);
    JsonObject textData = new JsonObject();

    textData.addProperty("name", newName);
    textData.addProperty("kmsId", kmsId);
    textData.addProperty("scopedToAccount", false);
    textData.add("usageRestrictions", null);
    textData.addProperty("value", value);
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("uuid", getSecretId(secretName));
    Response editResponse =
        genericRequestBuilder.postCall(requestSpecification, EncryptedTextConstants.URI_ENCRYPTED_TEXT_UPDATE);
    return editResponse.jsonPath();
  }

  public Response deleteEncryptedText(RequestSpecification requestSpecification, String uuid) {
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("uuid", uuid);
    Response deleteresponse =
        GenericRequestBuilder.deleteCall(requestSpecification, EncryptedTextConstants.URI_ENCRYPTED_TEXT_DELETE);
    return deleteresponse;
  }
}
