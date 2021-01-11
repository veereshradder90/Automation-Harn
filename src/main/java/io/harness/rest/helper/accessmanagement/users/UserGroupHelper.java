package io.harness.rest.helper.accessmanagement.users;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.applications.ApplicationConstants;
import io.harness.rest.utils.RestAssuredResponseParser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class UserGroupHelper extends CoreUtils {

  GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
  public Map<String, String> getUserGroupList() {
    Response response = GenericRequestBuilder.getCall(UserConstants.USER_GROUPS);
    if (response.getStatusCode() == 200) {
      Map<String, String> userGroupList =
          responseParser.getResponseMapForGivenKeys(response, "resource.response", "name", "uuid");
      return userGroupList;
    }
    return new HashMap<String, String>();
  }

  public Map<String, String> getUserGroupList(RequestSpecification requestSpecification) {
    Response response = GenericRequestBuilder.getCall(requestSpecification, UserConstants.USER_GROUPS);
    if (response.getStatusCode() == 200) {
      Map<String, String> userGroupList =
          responseParser.getResponseMapForGivenKeys(response, "resource.response", "name", "uuid");
      return userGroupList;
    }
    return new HashMap<String, String>();
  }
  // create a User Group
  public JsonPath createUserGroup(String userGroupName) {
    JsonObject groupData = new JsonObject();
    groupData.addProperty("name", userGroupName);
    groupData.addProperty("accountId", defaultAccountId);
    Response userGroupResponse = genericRequestBuilder.postCall(groupData, UserConstants.USER_GROUPS);
    return userGroupResponse.jsonPath();
  }

  public JsonPath createUserGroup(RequestSpecification requestSpecification, String userGroupName) {
    JsonObject groupData = new JsonObject();
    groupData.addProperty("name", userGroupName);
    groupData.addProperty("accountId", defaultAccountId);
    requestSpecification.body(groupData.toString());
    Response userGroupResponse = genericRequestBuilder.postCall(requestSpecification, UserConstants.USER_GROUPS);
    return userGroupResponse.jsonPath();
  }

  public Response getUserGroups(RequestSpecification requestSpecification) {
    Response Response = genericRequestBuilder.getCall(requestSpecification, UserConstants.USER_GROUPS);
    return Response;
  }

  public Response deleteUserGroup(String userGroupId) {
    RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
    requestSpecification.pathParam("userGroupId", userGroupId);
    Response deleteResponse = genericRequestBuilder.deleteCall(requestSpecification, UserConstants.USER_GROUPS_DELETE);
    return deleteResponse;
  }
  
  public Response getUserGroupsResponse(RequestSpecification requestSpecification) {
    Response response = GenericRequestBuilder.getCall(requestSpecification, UserConstants.USER_GROUPS);
    return response;
  }
}
