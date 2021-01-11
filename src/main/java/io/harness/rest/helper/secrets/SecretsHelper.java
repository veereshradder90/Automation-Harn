package io.harness.rest.helper.secrets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.UsageScope.UsageScopeHelper;
import io.harness.rest.helper.secretmanager.SecretManagerHelper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SecretsHelper extends CoreUtils {
  SecretManagerHelper secretManagerHelper = new SecretManagerHelper();
  UsageScopeHelper usageScopeHelper = new UsageScopeHelper();
  public String bearerToken = null;

  public Response addSecretText(JsonObject jsonObject, String uri) {
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    if (bearerToken == null) {
      bearerToken = restClient.getBearerToken();
    }
    requestSpecificationObject.auth().oauth2(bearerToken);
    requestSpecificationObject.body(jsonObject.toString());
    Response response = GenericRequestBuilder.postCall(requestSpecificationObject, uri);
    return response;
  }

  public Response addSecretText(String secretName, String secretValue) {
    String defaultSecretManagerId =
        secretManagerHelper.getSecretManagerList().get(SecretConstants.DEFAULT_SECRET_MANAGER_NAME);
    return addSecretText(secretName, secretValue, defaultSecretManagerId);
  }

  public Response addSecretText(String secretName, String secretValue, String secretManagerId) {
    return addSecretText(secretName, secretValue, secretManagerId, false);
  }

  public Response addSecretText(String secretName, String secretValue, String secretManagerId, boolean scopeToAccount) {
    try {
      JsonObject jsonObject = jReader.readJSONFiles(SecretConstants.REQUEST_JSON_SECRET_CREATION);
      jsonObject.addProperty("name", secretName);
      jsonObject.addProperty("value", secretValue);
      jsonObject.addProperty("kmsId", secretManagerId);
      jsonObject.addProperty("scopedToAccount", scopeToAccount);
      if (!scopeToAccount) {
        jsonObject.add("usageRestrictions", usageScopeHelper.getDefaultUsageScope());
      } else {
        jsonObject.add("usageRestrictions", null);
      }
      Response response = addSecretText(jsonObject, SecretConstants.ADD_SECRETS);
      return response;
    } catch (FileNotFoundException e) {
      log.info("file not found {}", SecretConstants.REQUEST_JSON_SECRET_CREATION);
    }
    return null;
  }

  public Response addScopeToAccountSecret(String secretName, String secretValue) {
    String defaultSecretManagerId =
        secretManagerHelper.getSecretManagerList().get(SecretConstants.DEFAULT_SECRET_MANAGER_NAME);
    return addScopeToAccountSecret(secretName, secretValue, defaultSecretManagerId);
  }

  public Response addScopeToAccountSecret(String secretName, String secretValue, String secretManagerId) {
    return addSecretText(secretName, secretValue, secretManagerId, true);
  }

  public Response addSecretWithUsageScope(Map<String, String> secretMap, JsonElement usageScope) {
    try {
      JsonObject jsonObject = jReader.readJSONFiles(SecretConstants.REQUEST_JSON_SECRET_CREATION);
      jsonObject.addProperty("name", secretMap.get("name"));
      jsonObject.addProperty("value", secretMap.get("value"));
      jsonObject.addProperty("kmsId", secretMap.get("kmsId"));
      jsonObject.addProperty("scopedToAccount", false);
      jsonObject.add("usageRestrictions", usageScope);
      Response response = addSecretText(jsonObject, SecretConstants.ADD_SECRETS);
      return response;
    } catch (FileNotFoundException e) {
      log.info("file not found {}", SecretConstants.REQUEST_JSON_SECRET_CREATION);
    }
    return null;
  }

  public Response addCustomSecret(String name, String secretManagerId, Map<String, Object> properties) {
    JsonObject jsonObject = null;
    try {
      jsonObject = jReader.readJSONFiles(SecretConstants.REQUEST_JSON_SECRET_CREATION);
      jsonObject.addProperty("name", name);
      jsonObject.addProperty("value", "");
      jsonObject.addProperty("kmsId", secretManagerId);
      if (properties != null && properties.get("parameters") != null) {
        jsonObject.add("parameters", (JsonArray) properties.get("parameters"));
      } else {
        jsonObject.add("parameters", new JsonArray());
      }
      jsonObject.addProperty("scopedToAccount", false);
      if (properties != null && properties.get("usageRestrictions") != null) {
        jsonObject.add("usageRestrictions", (JsonElement) properties.get("usageRestrictions"));
      } else {
        jsonObject.add("usageRestrictions", usageScopeHelper.getDefaultUsageScope());
      }

      Response response = addSecretText(jsonObject, SecretConstants.ADD_SECRETS);
      return response;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Response getSecretList(String type) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    Map<String, String> queryParams = new HashMap<String, String>();
    queryParams.put("type", type);
    queryParams.put("details", "true");
    queryParams.put("offset", "0");
    queryParams.put("limit", "1000");
    requestSpecification.queryParams(queryParams);
    Response response = GenericRequestBuilder.getCall(requestSpecification, SecretConstants.GET_SECRET_LIST);
    if (response.getStatusCode() != 200) {
      log.info("list-secrets-page is failed");
    }
    return response;
  }
  public Map<String, String> getSecrets() {
    Map<String, String> secretList = new HashMap<String, String>();
    Response response = getSecretList(SecretConstants.SECRET_TEXT);
    if (response.getStatusCode() == 200) {
      secretList = responseParser.getResponseMapForGivenKeys(response, "resource.response", "name", "uuid");
    }
    return secretList;
  }

  public Response getSecretsListForApplication(String type, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    Map<String, String> queryParams = new HashMap<String, String>();
    queryParams.put("type", type);
    queryParams.put("details", "false");
    queryParams.put("currentAppId", appId);
    queryParams.put("offset", "0");
    queryParams.put("limit", "1000");
    requestSpecification.queryParams(queryParams);
    Response response = GenericRequestBuilder.getCall(requestSpecification, SecretConstants.GET_SECRET_LIST);
    if (response.getStatusCode() != 200) {
      log.info("list-secrets-page is failed");
    }
    return response;
  }

  public Response deleteSecret(String uuid) {
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecificationObject.queryParam("uuid", uuid);
    Response response = GenericRequestBuilder.deleteCall(requestSpecificationObject, SecretConstants.DELETE_SECRET);
    return response;
  }

  public boolean deleteFile(String uuid) {
    RequestSpecification requestSpecificationObject = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecificationObject.queryParam("uuid", uuid);
    Response response =
        GenericRequestBuilder.deleteCall(requestSpecificationObject, SecretConstants.DELETE_SECRET_FILE);
    return response.getStatusCode() == 200;
  }

  public Map<String, String> getSecretProperties(String secretName) {
    Map<String, String> secretProperties = searchSecret(secretName, SecretConstants.SECRET_TEXT);
    return secretProperties;
  }

  public Response editSecret(String secretNameToEdit, Map<String, String> editedValues) {
    Map<String, String> secretProperties = getSecretProperties(secretNameToEdit);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", secretProperties.get("name"));
    jsonObject.add("usageRestrictions", usageScopeHelper.getDefaultUsageScope());
    Object scopeToAccount = secretProperties.get("scopedToAccount");
    jsonObject.addProperty("scopedToAccount", Boolean.valueOf(scopeToAccount.toString()));
    jsonObject.addProperty("kmsId", secretProperties.get("kmsId"));
    jsonObject.addProperty("value", secretProperties.get("value"));
    for (String key : editedValues.keySet()) {
      jsonObject.addProperty(key, editedValues.get(key));
    }
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(jsonObject.toString());
    requestSpecification.queryParam("uuid", secretProperties.get("uuid"));
    Response response = GenericRequestBuilder.postCall(requestSpecification, SecretConstants.UPDATE_SECRET);
    return response;
  }

  public boolean isSecretExist(String secret) {
    Map<String, String> secretProperties = getSecretProperties(secret);
    return secret.equals(secretProperties.get("name"));
  }

  public boolean isSecretExist(String secret, String secretManagerId) {
    Map<String, String> secretProperties = getSecretProperties(secret);
    if (secretProperties == null || secretProperties.size() == 0) {
      return false;
    }
    return secretProperties.get("kmsId").equals(secretManagerId);
  }

  public Map<String, String> searchSecret(String secretName, String type) {
    Map<String, String> secretProperties = new HashMap<String, String>();
    ArrayList<HashMap> resource = searchRegexSecret(secretName, type);
    if (resource.size() == 1 && secretName.equals(resource.get(0).get("name").toString())) {
      secretProperties = resource.get(0);
    }
    return secretProperties;
  }

  public ArrayList<HashMap> searchRegexSecret(String secretName, String type) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    Map<String, String> queryParams = new HashMap<String, String>();
    queryParams.put("type", type);
    queryParams.put("details", "true");
    queryParams.put("offset", "0");
    queryParams.put("limit", "1000");
    queryParams.put("search[0][field]", "name");
    queryParams.put("search[0][op]", "CONTAINS");
    queryParams.put("search[0][value]", secretName);
    requestSpecification.queryParams(queryParams);
    Response response = GenericRequestBuilder.getCall(requestSpecification, SecretConstants.GET_SECRET_LIST);
    if (response.getStatusCode() != 200) {
      log.info("list-secrets-page is failed");
    }
    ArrayList<HashMap> secretsList = response.jsonPath().getJsonObject("resource.response");
    return secretsList;
  }

  public ArrayList<HashMap> searchRegexSecretForApplication(String secretName, String type, String appId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    Map<String, String> queryParams = new HashMap<String, String>();
    queryParams.put("type", type);
    queryParams.put("details", "false");
    queryParams.put("currentAppId", appId);
    queryParams.put("offset", "0");
    queryParams.put("limit", "1000");
    queryParams.put("search[0][field]", "name");
    queryParams.put("search[0][op]", "CONTAINS");
    queryParams.put("search[0][value]", secretName);
    requestSpecification.queryParams(queryParams);
    Response response = GenericRequestBuilder.getCall(requestSpecification, SecretConstants.GET_SECRET_LIST);
    if (response.getStatusCode() != 200) {
      log.info("list-secrets-page is failed");
    }
    ArrayList<HashMap> secretsList = response.jsonPath().getJsonObject("resource.response");
    return secretsList;
  }

  public Response addSecretFile(String secretName, String fileContent, String secretManagerId) {
    return addSecretFile(secretName, fileContent, secretManagerId, false);
  }

  public Response addSecretFile(String secretName, String fileContent, String secretManagerId, boolean scopeToAccount) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getMultipartRequestSpecification();
    requestSpecification.formParams("name", secretName);
    requestSpecification.formParams("kmsId", secretManagerId);
    requestSpecification.formParams("scopedToAccount", scopeToAccount);
    if (!scopeToAccount) {
      requestSpecification.formParams("usageRestrictions", usageScopeHelper.getDefaultUsageScope().toString());
    }
    requestSpecification.formParams("filename", "sample.txt");
    requestSpecification.multiPart("file", fileContent);
    Response response = GenericRequestBuilder.postCall(requestSpecification, SecretConstants.ADD_SECRET_FILE);
    return response;
  }

  public Response editSecretFile(String secretName, String editedName, String fileContent) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getMultipartRequestSpecification();
    Map<String, String> secretFile = getSecretFile(secretName);
    requestSpecification.formParams("name", editedName);
    requestSpecification.formParams("kmsId", secretFile.get("kmsId"));
    Object scopeToAccount = secretFile.get("scopedToAccount");
    requestSpecification.formParams("scopedToAccount", Boolean.valueOf(scopeToAccount.toString()));
    requestSpecification.formParams("usageRestrictions", secretFile.get("usageRestrictions"));
    requestSpecification.formParams("filename", "sample.txt");
    requestSpecification.formParams("uuid", secretFile.get("uuid"));
    requestSpecification.multiPart("file", fileContent);
    Response response = GenericRequestBuilder.postCall(requestSpecification, SecretConstants.UPDATE_SECRET_FILE);
    return response;
  }

  public Response getSecretFiles() {
    Response secretList = getSecretList(SecretConstants.SECRET_FILE);
    return secretList;
  }

  public Map<String, String> getSecretFile(String secretName) {
    Map<String, String> secretProperties = searchSecret(secretName, SecretConstants.SECRET_FILE);
    return secretProperties;
  }

  public boolean isSecretFileExist(String secretName, String secretManagerId) {
    Map<String, String> secretFiles = getSecretFile(secretName);
    if (secretFiles == null) {
      return false;
    }
    return secretManagerId.equals(secretFiles.get("kmsId"));
  }

  public ArrayList<HashMap> getChangeLogForSecrets(String secretName) {
    Map<String, String> secretProperties = searchSecret(secretName, "SECRET_TEXT");
    String secretId = secretProperties.get("uuid");
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("entityId", secretId);
    requestSpecification.queryParam("type", "SECRET_TEXT");
    Response changeLogs = GenericRequestBuilder.getCall(requestSpecification, SecretConstants.CHANGE_LOGS);
    ArrayList<HashMap> resource = changeLogs.jsonPath().getJsonObject("resource");
    return resource;
  }

  public ArrayList<HashMap> getChangeLog(String entityName, String type) {
    switch (type) {
      case "SECRET_TEXT":
        return getChangeLogForSecrets(entityName);
      default:
        return null;
    }
  }

  public ArrayList<HashMap> getSetupUsageForSecrets(String secretName) {
    Map<String, String> secretProperties = searchSecret(secretName, "SECRET_TEXT");
    String secretId = secretProperties.get("uuid");
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("uuid", secretId);
    Response changeLogs = GenericRequestBuilder.getCall(requestSpecification, SecretConstants.LIST_SETUP_USAGE);
    ArrayList<HashMap> resource = changeLogs.jsonPath().getJsonObject("resource");
    return resource;
  }

  public ArrayList<HashMap> getSetupUsage(String entityName, String type) {
    switch (type) {
      case "SECRET_TEXT":
        return getSetupUsageForSecrets(entityName);
      default:
        return null;
    }
  }
}
