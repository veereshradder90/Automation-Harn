package io.harness.rest.helper.accessmanagement.users;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.tagsmanagement.TagsConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHelper extends CoreUtils {
  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  public Map<String, String> getUsersList() {
    RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
    Map<String, String> userList = new HashMap<String, String>();
    Map<String, String> queryParams = new HashMap<String, String>();
    queryParams.put("offset", "0");
    queryParams.put("limit", "1000");
    requestSpecification.queryParams(queryParams);
    Response response = GenericRequestBuilder.getCall(requestSpecification, UserConstants.USERS);
    ArrayList<HashMap> responseMap = response.jsonPath().getJsonObject("resource.response");
    for (HashMap map : responseMap) {
      HashMap userDetailsMap = (HashMap) map.get("user");
      userList.put(userDetailsMap.get("email").toString(), userDetailsMap.get("uuid").toString());
    }
    return userList;
  }

  public boolean updateUserGroups(String uuid, List<String> userGroupList) {
    JsonObject jsonRequest = new JsonObject();
    JsonArray userGroupArray = new JsonArray();
    if (userGroupList != null) {
      for (String userGroupId : userGroupList) {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", userGroupId);
        userGroupArray.add(object);
      }
    }
    jsonRequest.add("userGroups", userGroupArray);
    Response response = GenericRequestBuilder.putCall(jsonRequest, UserConstants.UPDATE_USER + uuid);
    return response.getStatusCode() == 200;
  }

  public JsonPath inviteUser(RequestSpecification requestSpecification, String email, String userGroups) {
    JsonObject userData = new JsonObject();
    userData.addProperty("email", email);
    userData.addProperty("userGroups", userGroups);
    requestSpecification.body(userData.toString());
    Response inviteUserResponse = genericRequestBuilder.postCall(requestSpecification, UserConstants.INVITE_USER);
    return inviteUserResponse.jsonPath();
  }

  public Response getUsers(RequestSpecification requestSpecification) {
    Response Response = genericRequestBuilder.getCall(requestSpecification, UserConstants.USERS);
    return Response;
  }
  
  public Response addUser(String[] emailIds, String[] usergroups) {
    JsonObject jsonObject = new JsonObject();
    JsonArray emailList = new JsonArray();
    for (String email : emailIds) {
      emailList.add(email);
    }
    jsonObject.add("emails", emailList);

    JsonArray userGroupList = new JsonArray();
    for (String userGroup : usergroups) {
      userGroupList.add(userGroup);
    }
    jsonObject.add("userGroups", userGroupList);

    Response response = GenericRequestBuilder.postCall(jsonObject, UserConstants.USER_INVITE);
    return response;
  }

  public Response addUser(RequestSpecification requestSpecification, String[] emailIds, String[] usergroups) {
    JsonObject jsonObject = new JsonObject();
    JsonArray emailList = new JsonArray();
    for (String email : emailIds) {
      emailList.add(email);
    }
    jsonObject.add("emails", emailList);

    JsonArray userGroupList = new JsonArray();
    for (String userGroup : usergroups) {
      userGroupList.add(userGroup);
    }
    jsonObject.add("userGroups", userGroupList);
    requestSpecification.body(jsonObject.toString());
    Response response = GenericRequestBuilder.postCall(requestSpecification, UserConstants.USER_INVITE);
    return response;
  }
}
