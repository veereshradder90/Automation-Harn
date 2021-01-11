package io.harness.rest.helper.secretmanager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SecretManagerHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  public Map<String, String> getSecretManagerList() {
    Response response =
        GenericRequestBuilder.getRequestSpecificationObject().get(SecretManagerConstants.LIST_SECRET_MANAGERS);
    Map<String, String> secretManagers = new HashMap<String, String>();
    if (response.getStatusCode() == 200) {
      secretManagers = responseParser.getResponseMapForGivenKeys(response, "resource", "name", "uuid");
    } else {
      log.info("Error while fetching Secret manager List");
    }
    return secretManagers;
  }

  public Response createVaultSecretManagerWithAppRole(String managerName) {
    HashMap<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("appRoleId", secretsProperties.getSecret("HASHICORP_VAULT_APPROLE_ID"));
    propertiesMap.put("secretId", secretsProperties.getSecret("HASHICORP_VAULT_SECRET_ID"));
    propertiesMap.put("secretEngineName", "secret");
    return createVaultSecretManager(managerName, propertiesMap);
  }

  public Response createVaultSecretManager(String managerName) {
    HashMap<String, String> propertiesMap = new HashMap<>();
    return createVaultSecretManager(managerName, propertiesMap);
  }

  public Response createVaultSecretManager(String managerName, HashMap<String, String> managerProperties) {
    try {
      JsonObject jsonObject = jReader.readJSONFiles(SecretManagerConstants.REQUEST_JSON_VAULT_SM_TOKEN);
      jsonObject.addProperty("name", managerName);
      Set<String> keySet = managerProperties.keySet();
      for (String key : keySet) {
        jsonObject.addProperty(key, managerProperties.get(key));
      }
      if (managerProperties.get("authToken") == null && managerProperties.get("appRoleId") == null) {
        String authToken = secretsProperties.getSecret("HASHICORP_VAULT_TOKEN");
        jsonObject.addProperty("authToken", authToken);
      }
      Response response = GenericRequestBuilder.postCall(jsonObject, SecretManagerConstants.ADD_VAULT_SM);
      return response;

    } catch (FileNotFoundException e) {
      log.info("JSON file not found");
    }
    return null;
  }

  public Response createHashiCorpVault(RequestSpecification requestSpecification, String hashiName) {
    try {
      JsonObject hashiCorpVaultData = jReader.readJSONFiles(SecretManagerConstants.REQUEST_JSON_VAULT_SM_TOKEN);
      hashiCorpVaultData.addProperty("name", hashiName);
      hashiCorpVaultData.addProperty("basepath", "/harness");
      hashiCorpVaultData.addProperty("vaultUrl", configPropertis.getConfig("HASHICORP_VAULT_URL"));
      hashiCorpVaultData.addProperty("appRoleId", secretsProperties.getSecret("HASHICORP_VAULT_APPROLE_ID"));
      hashiCorpVaultData.addProperty("secretId", secretsProperties.getSecret("HASHICORP_VAULT_SECRET_ID"));
      hashiCorpVaultData.addProperty("secretEngineName", "secret");
      hashiCorpVaultData.addProperty("default", false);
      hashiCorpVaultData.addProperty("renewalInterval", 15);
      requestSpecification.body(hashiCorpVaultData.toString());
      Response hashiCorpVaultResponse =
          genericRequestBuilder.postCall(requestSpecification, SecretManagerConstants.ADD_VAULT_SM);
      return hashiCorpVaultResponse;
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }

  public Response deleteSecretManagerByName(String secretManagerName, SecretManagerType smType) {
    return deleteSecretManagerById(getSecretManagerList().get(secretManagerName), smType);
  }

  public Response deleteSecretManagerById(String secretManagerId, SecretManagerType smType) {
    RequestSpecification requestObject = GenericRequestBuilder.getRequestSpecificationObject();
    Response response = null;
    switch (smType) {
      case AWS_SM:
        requestObject.queryParam("configId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestObject, SecretManagerConstants.ADD_AWS_SM);
        return response;
      case HASHICORP_VAULT:
        requestObject.queryParam("vaultConfigId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestObject, SecretManagerConstants.ADD_VAULT_SM);
        return response;
      case GCP_KMS:
        requestObject.queryParam("configId", secretManagerId);
        requestObject.contentType("multipart/form-data");
        response = GenericRequestBuilder.deleteCall(requestObject, SecretManagerConstants.ADD_GCP_KMS);
        return response;
      case AZURE_VAULT:
        requestObject.queryParam("configId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestObject, SecretManagerConstants.ADD_AZURE_VAULT);
        return response;
      case AWS_KMS:
        requestObject.queryParam("kmsConfigId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestObject, SecretManagerConstants.KMS);
        return response;
      case CUSTOM_SM:
        requestObject.pathParam("secretManagerId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestObject, SecretManagerConstants.UPDATE_CUSTOM_SM);
        return response;
      default:
        log.info("no secret manager specified");
        return null;
    }
  }

  public Response createAwsKmsSecretManager(String managerName, SecretManagerType smType) {
    String endPoint = null;
    HashMap credentials = new HashMap();
    credentials.put("accessKey", secretsProperties.getSecret("AWS_KMS_ACCESS_KEY"));
    credentials.put("secretKey", secretsProperties.getSecret("AWS_KMS_SECRET_KEY"));
    if (smType == SecretManagerType.AWS_KMS) {
      credentials.put("kmsArn", secretsProperties.getSecret("AWS_KMS_ARN"));
      endPoint = SecretManagerConstants.ADD_AWS_KMS;
    } else if (smType == SecretManagerType.AWS_SM) {
      endPoint = SecretManagerConstants.ADD_AWS_SM;
    }
    return createAwsKmsSecretManager(managerName, credentials, endPoint);
  }

  public Response createAwsKmsSecretManager(
      String managerName, HashMap<String, String> managerProperties, String endPoint) {
    try {
      JsonObject jsonObject = jReader.readJSONFiles(SecretManagerConstants.REQUEST_JSON_AWS_KMS);
      jsonObject.addProperty("name", managerName);
      Set<String> keySet = managerProperties.keySet();
      for (String key : keySet) {
        jsonObject.addProperty(key, managerProperties.get(key));
      }
      Response response = GenericRequestBuilder.postCall(jsonObject, endPoint);
      return response;

    } catch (FileNotFoundException e) {
      log.info("JSON file not found");
    }
    return null;
  }

  public Response createAWSKMS(RequestSpecification requestSpecification, String awsKMSName) {
    try {
      JsonObject awsKMSData = jReader.readJSONFiles(SecretManagerConstants.REQUEST_JSON_AWS_KMS);
      awsKMSData.addProperty("name", awsKMSName);
      awsKMSData.addProperty("accessKey", secretsProperties.getSecret("AWS_KMS_ACCESS_KEY"));
      awsKMSData.addProperty("secretKey", secretsProperties.getSecret("AWS_KMS_SECRET_KEY"));
      awsKMSData.addProperty("kmsArn", secretsProperties.getSecret("AWS_KMS_ARN"));
      awsKMSData.addProperty("region", "us-east-1");
      awsKMSData.addProperty("default", false);
      requestSpecification.body(awsKMSData.toString());
      Response awsKMSResponse =
          genericRequestBuilder.postCall(requestSpecification, SecretManagerConstants.ADD_AWS_KMS);
      return awsKMSResponse;
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }

  public Response createAWSSecretManager(RequestSpecification requestSpecification, String awsSMName) {
    try {
      JsonObject awsSecretManagerData = jReader.readJSONFiles(SecretManagerConstants.REQUEST_JSON_AWS_KMS);
      awsSecretManagerData.addProperty("name", awsSMName);
      awsSecretManagerData.addProperty("accessKey", secretsProperties.getSecret("AWS_KMS_ACCESS_KEY"));
      awsSecretManagerData.addProperty("secretKey", secretsProperties.getSecret("AWS_KMS_SECRET_KEY"));
      awsSecretManagerData.addProperty("kmsArn", "");
      awsSecretManagerData.addProperty("region", "us-east-1");
      awsSecretManagerData.addProperty("default", false);
      requestSpecification.body(awsSecretManagerData.toString());
      Response awsManagerResponse =
          genericRequestBuilder.postCall(requestSpecification, SecretManagerConstants.ADD_AWS_KMS);
      return awsManagerResponse;
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }

  public Response createAzureVaultSecretManager(String managerName) {
    HashMap<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("secretKey", secretsProperties.getSecret("AZURE_VAULT_SECRET_KEY"));
    propertiesMap.put("tenantId", secretsProperties.getSecret("AZURE_VAULT_TENANT_ID"));
    propertiesMap.put("subscription", secretsProperties.getSecret("AZURE_VAULT_SUBSCRIPTION"));
    propertiesMap.put("clientId", secretsProperties.getSecret("AZURE_VAULT_CLIENT_ID"));
    propertiesMap.put("vaultName", "Aman-test");
    propertiesMap.put("accountId", defaultAccountId);
    return createAzureVaultSecretManager(managerName, propertiesMap);
  }

  public Response createAzureVaultSecretManager(String managerName, HashMap<String, String> managerProperties) {
    try {
      JsonObject jsonObject = jReader.readJSONFiles(SecretManagerConstants.REQUEST_JSON_AZURE_VAULT);
      jsonObject.addProperty("name", managerName);
      Set<String> keySet = managerProperties.keySet();
      for (String key : keySet) {
        jsonObject.addProperty(key, managerProperties.get(key));
      }
      Response response = GenericRequestBuilder.postCall(jsonObject, SecretManagerConstants.ADD_AZURE_VAULT);
      return response;

    } catch (FileNotFoundException e) {
      log.info("JSON file not found");
    }
    return null;
  }

  public Response createAzureVaultSecretManager(RequestSpecification requestSpecification, String azureName) {
    try {
      JsonObject azureManagerData = jReader.readJSONFiles(SecretManagerConstants.REQUEST_JSON_AZURE_VAULT);
      azureManagerData.addProperty("name", azureName);
      azureManagerData.addProperty("accountId", defaultAccountId);
      azureManagerData.addProperty("clientId", secretsProperties.getSecret("AZURE_VAULT_CLIENT_ID"));
      azureManagerData.addProperty("tenantId", secretsProperties.getSecret("AZURE_VAULT_TENANT_ID"));
      azureManagerData.addProperty("subscription", secretsProperties.getSecret("AZURE_VAULT_SUBSCRIPTION"));
      azureManagerData.addProperty("secretKey", secretsProperties.getSecret("AZURE_VAULT_SECRET_KEY"));
      requestSpecification.body(azureManagerData.toString());
      Response azureManagerResponse =
          genericRequestBuilder.postCall(requestSpecification, SecretManagerConstants.ADD_AZURE_VAULT);
      return azureManagerResponse;
    } catch (FileNotFoundException e) {
      System.out.println("Error" + e.getMessage());
    }
    return null;
  }

  public Response createGoogleKMS(String managerName) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getMultipartRequestSpecification();
    requestSpecification.formParams("name", managerName);
    requestSpecification.formParams("encryptionType", "GCP_KMS");
    requestSpecification.formParams("projectId", "playground-243019");
    requestSpecification.formParams("region", "us");
    requestSpecification.formParams("keyName", "harness");
    requestSpecification.formParams("keyRing", "kms-dev-test");
    requestSpecification.formParams("isDefault", "undefined");
    requestSpecification.multiPart("credentials", secretsProperties.getSecret("GOOGLE_KMS_KEY_FILE"));
    Response response = requestSpecification.post(SecretManagerConstants.ADD_GCP_KMS);
    return response;
  }

  public Response createGoogleKMS(String email, String password, String googleName) {
    RequestSpecification googleKMSData = GenericRequestBuilder.getMultipartRequestSpecification();
    String bearerToken = restClient.loginUser(email, password);
    googleKMSData.auth().oauth2(bearerToken);
    googleKMSData.formParams("name", googleName);
    googleKMSData.formParams("encryptionType", "GCP_KMS");
    googleKMSData.formParams("projectId", "playground-243019");
    googleKMSData.formParams("region", "us");
    googleKMSData.formParams("keyName", "harness");
    googleKMSData.formParams("keyRing", "kms-dev-test");
    googleKMSData.formParams("isDefault", "undefined");
    googleKMSData.multiPart("credentials", secretsProperties.getSecret("GOOGLE_KMS_KEY_FILE"));
    Response response = GenericRequestBuilder.postCall(googleKMSData, SecretManagerConstants.ADD_GCP_KMS);
    return response;
  }

  public Response createCustomSM(String name, Map<String, Object> inputMap) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("executeOnDelegate", true);
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("templateId", inputMap.get("templateId").toString());
    jsonObject.add("delegateSelectors", new JsonArray());
    if (inputMap.get("testVariables") != null) {
      jsonObject.add("testVariables", (JsonArray) inputMap.get("testVariables"));
    } else {
      jsonObject.add("testVariables", new JsonArray());
    }
    Response response = GenericRequestBuilder.putCall(jsonObject, SecretManagerConstants.ADD_CUSTOM_SM);
    return response;
  }

  public Response createCustomSecretManager(
      RequestSpecification requestSpecification, String customName, Map<String, Object> inputMap) {
    JsonObject customData = new JsonObject();
    customData.addProperty("executeOnDelegate", true);
    customData.addProperty("name", customName);
    customData.addProperty("templateId", inputMap.get("templateId").toString());
    customData.add("delegateSelectors", new JsonArray());
    requestSpecification.body(customData.toString());
    Response customManagerResponse =
        genericRequestBuilder.putCall(requestSpecification, SecretManagerConstants.ADD_CUSTOM_SM);
    return customManagerResponse;
  }

  public Response editCustomSecretManager(String name, String secretManagerId, Map<String, Object> inputMap) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("executeOnDelegate", true);
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("templateId", inputMap.get("templateId").toString());
    jsonObject.add("delegateSelectors", new JsonArray());
    if (inputMap.get("testVariables") != null) {
      jsonObject.add("testVariables", (JsonArray) inputMap.get("testVariables"));
    } else {
      jsonObject.add("testVariables", new JsonArray());
    }
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.body(jsonObject.toString());
    requestSpecification.pathParam("secretManagerId", secretManagerId);

    Response response = GenericRequestBuilder.postCall(requestSpecification, SecretManagerConstants.UPDATE_CUSTOM_SM);
    return response;
  }

  public Response editCustomSecretManager(
      RequestSpecification requestSpecification, String name, String secretManagerId, Map<String, Object> inputMap) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("executeOnDelegate", true);
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("templateId", inputMap.get("templateId").toString());
    jsonObject.add("delegateSelectors", new JsonArray());
    if (inputMap.get("testVariables") != null) {
      jsonObject.add("testVariables", (JsonArray) inputMap.get("testVariables"));
    } else {
      jsonObject.add("testVariables", new JsonArray());
    }
    requestSpecification.body(jsonObject.toString());
    requestSpecification.pathParam("secretManagerId", secretManagerId);

    Response response = GenericRequestBuilder.postCall(requestSpecification, SecretManagerConstants.UPDATE_CUSTOM_SM);
    return response;
  }

  public Response migrateSecretManagers(HashMap<String, String> migrationProperties) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("fromEncryptionType", migrationProperties.get("fromEncryptionType"));
    requestSpecification.queryParam("fromKmsId", migrationProperties.get("fromKmsId"));
    requestSpecification.queryParam("toEncryptionType", migrationProperties.get("toEncryptionType"));
    requestSpecification.queryParam("toKmsId", migrationProperties.get("toKmsId"));
    requestSpecification.body(new JsonObject().toString());
    Response response = GenericRequestBuilder.putCall(requestSpecification, SecretManagerConstants.MIGRATE_SM);
    return response;
  }

  public JsonPath deleteSecretManagerById(
      RequestSpecification requestSpecification, String secretManagerId, SecretManagerType smType) {
    Response response = null;
    switch (smType) {
      case AWS_SM:
        requestSpecification.queryParam("configId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestSpecification, SecretManagerConstants.ADD_AWS_SM);
        return response.jsonPath();
      case HASHICORP_VAULT:
        requestSpecification.queryParam("vaultConfigId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestSpecification, SecretManagerConstants.ADD_VAULT_SM);
        return response.jsonPath();
      case GCP_KMS:
        requestSpecification.queryParam("configId", secretManagerId);
        requestSpecification.contentType("multipart/form-data");
        response = GenericRequestBuilder.deleteCall(requestSpecification, SecretManagerConstants.ADD_GCP_KMS);
        return response.jsonPath();
      case AZURE_VAULT:
        requestSpecification.queryParam("configId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestSpecification, SecretManagerConstants.ADD_AZURE_VAULT);
        return response.jsonPath();
      case AWS_KMS:
        requestSpecification.queryParam("kmsConfigId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestSpecification, SecretManagerConstants.KMS);
        return response.jsonPath();
      case CUSTOM_SM:
        requestSpecification.pathParam("secretManagerId", secretManagerId);
        response = GenericRequestBuilder.deleteCall(requestSpecification, SecretManagerConstants.UPDATE_CUSTOM_SM);
        return response.jsonPath();
      default:
        log.info("no secret manager specified");
        return null;
    }
  }
}
