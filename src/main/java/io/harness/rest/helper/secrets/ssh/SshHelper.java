package io.harness.rest.helper.secrets.ssh;
import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class SshHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

     // Create SSH key which call internal functions based on the type of key fetch type;
    // credentials denote Inline key or password or path
    public  JsonPath createSshKey(String sshName, String username,  String credentials, SshType keytype){
        JsonPath response=null;
        if(keytype==SshType.INLINE){
            response= createSshUsingInlineKey(sshName,username,credentials);
            return response;
        }
        else if (keytype==SshType.PATH){
            response= createSshUsingKeyPath(sshName,username,credentials);
            return response;
        }
        else if (keytype==SshType.PASSWORD){
            response= createSshUsingPassword(sshName, username,credentials);
            return response;
        }
        else {
            return response;
        }
    }

    public  JsonPath editSshKey(String sshName, String username,  String credentials, String uuid, SshType keytype) {
        JsonPath response = null;
        if (keytype == SshType.INLINE) {
            response = editSshUsingInlineKey(sshName, username, credentials, uuid);
            return response;
        } else if (keytype == SshType.PATH) {
            response = editSshUsingKeyPath(sshName, username, credentials, uuid);
            return response;
        } else if (keytype == SshType.PASSWORD) {
            response = editSshUsingPassword(sshName, username, credentials, uuid);
            return response;
        } else {
            return response;
        }
    }


        // create SSH with SSH inline key
    public JsonPath createSshUsingInlineKey(String sshName, String username, String InlineKey) {
        JsonObject sshData = new JsonObject();
        JsonObject sshRest = new JsonObject();
        JsonObject sshDataValue = new JsonObject();
        sshDataValue.addProperty("type", "HOST_CONNECTION_ATTRIBUTES");
        sshDataValue.addProperty("connectionType", "SSH");
        sshDataValue.addProperty("authenticationScheme", "SSH_KEY");
        sshDataValue.addProperty("sshPort", "22");
        sshDataValue.addProperty("userName", username);
        sshDataValue.addProperty("key",InlineKey);
        sshDataValue.addProperty("accessType", "KEY");
        sshData.addProperty("name", sshName);
        sshData.addProperty("category", "SETTING");
        sshData.add("usageRestrictions", sshRest);
        sshData.addProperty("accountId", defaultAccountId);
        sshData.add("value", sshDataValue);
        Response sshResponse = genericRequestBuilder.postCall(sshData, SshConstants.URI_SSH_CREATION);
        return sshResponse.jsonPath();
    }
    // edit SSH with SSH inline key
    public JsonPath editSshUsingInlineKey(String newSshName, String newUsername, String InlineKey, String uuid) { // Assigning the id of the key which needs to be modified

        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();// Building the request for the updated key
        JsonObject sshData = new JsonObject();
        JsonObject sshDataValue = new JsonObject();
        JsonObject sshRest = new JsonObject();
        sshDataValue.addProperty("connectionType", "SSH");
        sshDataValue.addProperty("type", "HOST_CONNECTION_ATTRIBUTES");
        sshDataValue.addProperty("authenticationScheme", "SSH_KEY");
        sshDataValue.addProperty("sshPort", "22");
        sshDataValue.addProperty("userName", newUsername);
        sshDataValue.addProperty("key",InlineKey);
        sshDataValue.addProperty("accessType", "KEY");
        sshData.addProperty("name", newSshName);
        sshData.addProperty("category", "SETTING");
        sshData.add("usageRestrictions", sshRest);
        sshData.addProperty("accountId", defaultAccountId);
        sshData.add("value", sshDataValue);
        requestSpecification.body(sshData.toString());
        requestSpecification.pathParam("sshId", uuid);
        Response editedSshResponse =
                genericRequestBuilder.putCall(requestSpecification, SshConstants
                        .URI_SSH_EDIT_DELETE);
        return editedSshResponse.jsonPath();


    }

    // delete SSH with SSH inline key
    public Response deleteSshKey(String uuid) {  //Assigning the id of the key which needs to be deleted

        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("sshId", uuid);
        Response deleteResponse =
                genericRequestBuilder.deleteCall(requestSpecification, SshConstants
                        .URI_SSH_EDIT_DELETE);
        return deleteResponse;
    }

    // create SSH with key in Delegate Path
    public JsonPath createSshUsingKeyPath(String sshName, String username, String path) {
        JsonObject sshData = new JsonObject();
        JsonObject sshRest = new JsonObject();
        JsonArray restr = new JsonArray();;
        JsonObject sshDataValue = new JsonObject();// Need to add values of the Values field of Request
        sshRest.add("appEnvRestrictions", restr);
        sshDataValue.addProperty("connectionType", "SSH");
        sshDataValue.addProperty("type", "HOST_CONNECTION_ATTRIBUTES");
        sshDataValue.addProperty("authenticationScheme", "SSH_KEY");
        sshDataValue.addProperty("sshPort", "22");
        sshDataValue.addProperty("userName", username);
        sshDataValue.addProperty("keyPath", path);
        sshDataValue.addProperty("passphrase", "");
        sshDataValue.addProperty("keyless", true);
        sshDataValue.addProperty("accessType", "KEY");
        sshData.addProperty("name", sshName);
        sshData.addProperty("category", "SETTING");
        sshData.addProperty("accountId", defaultAccountId);
        sshData.add("usageRestrictions", sshRest);
        sshData.add("value", sshDataValue);
        Response sshResponse = genericRequestBuilder.postCall(sshData, SshConstants
                .URI_SSH_CREATION);
        return sshResponse.jsonPath();
    }
    // edit SSH with SSH key in path
    public JsonPath editSshUsingKeyPath(String sshName, String username, String path, String uuid) {  // Assigning the id of the key which needs to be modified

        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();// Building the request for the updated key
        JsonObject sshData = new JsonObject();
        JsonObject sshRest = new JsonObject();
        JsonObject sshDataValue = new JsonObject();// Need to add values of the Values field of Request
        JsonArray restr = new JsonArray();;
        sshRest.add("appEnvRestrictions", restr);
        sshDataValue.addProperty("connectionType", "SSH");
        sshDataValue.addProperty("type", "HOST_CONNECTION_ATTRIBUTES");
        sshDataValue.addProperty("authenticationScheme", "SSH_KEY");
        sshDataValue.addProperty("sshPort", "22");
        sshDataValue.addProperty("userName", username);
        sshDataValue.addProperty("keyPath", path);
        sshDataValue.addProperty("settingType", "HOST_CONNECTION_ATTRIBUTES");
        sshDataValue.addProperty("keyless", true);
        sshDataValue.addProperty("accessType", "KEY");
        sshData.addProperty("name", sshName);
        sshData.addProperty("category", "SETTING");
        sshData.addProperty("accountId", defaultAccountId);
        sshData.add("usageRestrictions", sshRest);
        sshData.add("value", sshDataValue);
        requestSpecification.body(sshData.toString());
        requestSpecification.pathParam("sshId", uuid);
        Response editedSshResponse =
                genericRequestBuilder.putCall(requestSpecification, SshConstants
                        .URI_SSH_EDIT_DELETE);
        return editedSshResponse.jsonPath();


    }

    // create SSH with password
    public JsonPath createSshUsingPassword(String sshName, String username, String password) {
        JsonObject sshData = new JsonObject();
        JsonObject sshRest = new JsonObject();
        JsonObject sshDataValue = new JsonObject();// Need to add values of the Values field of Request
        sshDataValue.addProperty("connectionType", "SSH");
        sshDataValue.addProperty("type", "HOST_CONNECTION_ATTRIBUTES");
        sshDataValue.addProperty("authenticationScheme", "SSH_KEY");
        sshDataValue.addProperty("sshPort", "22");
        sshDataValue.addProperty("userName", username);
        sshDataValue.addProperty("sshPassword",password);
        sshDataValue.addProperty("accessType", "USER_PASSWORD");
        sshData.addProperty("name", sshName);
        sshData.addProperty("category", "SETTING");
        sshData.add("usageRestrictions", sshRest);
        sshData.addProperty("accountId", defaultAccountId);
        sshData.add("value", sshDataValue);
        Response sshResponse = genericRequestBuilder.postCall(sshData, SshConstants
                .URI_SSH_CREATION);
        return sshResponse.jsonPath();
    }

    // edit SSH with SSH password
    public JsonPath editSshUsingPassword(String newSshName, String newUsername, String password, String uuid) {  // Assigning the id of the key which needs to be modified
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();// Building the request for the updated key
        JsonObject sshData = new JsonObject();
        JsonObject sshDataValue = new JsonObject();
        JsonObject sshRest = new JsonObject();
        sshDataValue.addProperty("connectionType", "SSH");
        sshDataValue.addProperty("type", "HOST_CONNECTION_ATTRIBUTES");
        sshDataValue.addProperty("authenticationScheme", "SSH_KEY");
        sshDataValue.addProperty("sshPort", "22");
        sshDataValue.addProperty("userName", newUsername);
        sshDataValue.addProperty("sshPassword", password);
        sshDataValue.addProperty("accessType", "USER_PASSWORD");
        sshData.addProperty("name", newSshName);
        sshData.addProperty("category", "SETTING");
        sshData.add("usageRestrictions", sshRest);
        sshData.addProperty("accountId", defaultAccountId);
        sshData.add("value", sshDataValue);
        requestSpecification.body(sshData.toString());
        requestSpecification.pathParam("sshId", uuid);
        Response editedSshResponse =
                genericRequestBuilder.putCall(requestSpecification, SshConstants
                        .URI_SSH_EDIT_DELETE);
        return editedSshResponse.jsonPath();


    }
}
