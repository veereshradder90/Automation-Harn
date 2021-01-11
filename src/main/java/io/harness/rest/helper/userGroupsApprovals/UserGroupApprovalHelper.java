package io.harness.rest.helper.userGroupsApprovals;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserGroupApprovalHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

    public JsonPath getUserGroups() {
        Response post = genericRequestBuilder.getCall( UserGroupApprovalConstant.GET_USER_GROUP_URI);
        return post.jsonPath();
    }

    public String getUserGroupIdByName(String userGroupName) {
        String userGroupId="";
        JsonPath userGroupData=getUserGroups();
        for(int i=0,size=userGroupData.getList("resource.response").size();i<size;i++){
            if(userGroupData.getString("resource.response["+i+"].name").equals(userGroupName))
                userGroupId= userGroupData.getString("resource.response["+i+"].uuid");
        }
        return userGroupId;
    }
}
