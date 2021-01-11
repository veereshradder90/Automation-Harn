package io.harness.rest.helper.secrets.encryptedFile;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class EncryptedFileHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  public JsonPath createEncryptedFile(String email, String password, String secretFileName, String kmsId) {
    RequestSpecification requestSpecification = GenericRequestBuilder.getMultipartRequestSpecification();
    String bearerToken = restClient.loginUser(email, password);
    requestSpecification.auth().oauth2(bearerToken);
    requestSpecification.formParams("name", secretFileName);
    requestSpecification.formParams("kmsId", kmsId);
    requestSpecification.formParams("scopedToAccount", false);
    requestSpecification.multiPart("file", secretsProperties.getSecret("GOOGLE_KMS_KEY_FILE"));
    Response response =
        GenericRequestBuilder.postCall(requestSpecification, EncryptedFileConstants.URI_ENCRYPTED_FILE_CREATION);
    return response.jsonPath();
  }

  public String getSecretFileId(String secretFileName) {
    String uuid = "";
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("type", "CONFIG_FILE");
    requestSpecification.queryParam("details", true);
    requestSpecification.queryParam("offset", 0);
    requestSpecification.queryParam("limit", 40);
    Response secretResponse =
        genericRequestBuilder.getCall(requestSpecification, EncryptedFileConstants.URI_LIST_ENCRYPTED_FILES);
    HashMap<String, String> map = new HashMap<>();
    for (int i = 0; i < ((ArrayList) secretResponse.path("resource.response.name")).size(); i++) {
      map.put(((ArrayList) secretResponse.path("resource.response.name")).get(i).toString(),
          ((ArrayList) secretResponse.path("resource.response.uuid")).get(i).toString());
    }
    log.info("uuid is " + map.get(secretFileName));
    return uuid;
  }

  public JsonPath editSecretFile(String email, String password, String secretFileName, String newName, String kmsId) {
    RequestSpecification fileData = GenericRequestBuilder.getMultipartRequestSpecification();
    String bearerToken = restClient.loginUser(email, password);
    fileData.auth().oauth2(bearerToken);
    fileData.formParams("name", newName);
    fileData.formParams("kmsId", kmsId);
    fileData.formParams("scopedToAccount", false);
    fileData.formParams("uuid", getSecretFileId(secretFileName));
    fileData.multiPart("file", secretsProperties.getSecret("GOOGLE_KMS_KEY_FILE"));
    Response editResponse = genericRequestBuilder.postCall(fileData, EncryptedFileConstants.URI_ENCRYPTED_FILE_UPDATE);
    return editResponse.jsonPath();
  }

  public Response deleteEncryptedFile(RequestSpecification requestSpecification, String uuid) {
    requestSpecification.queryParam("accountId", defaultAccountId);
    requestSpecification.queryParam("uuid", uuid);
    Response deleteResponse =
        GenericRequestBuilder.postCall(requestSpecification, EncryptedFileConstants.URI_ENCRYPTED_FILE_DELETE);
    return deleteResponse;
  }
}
