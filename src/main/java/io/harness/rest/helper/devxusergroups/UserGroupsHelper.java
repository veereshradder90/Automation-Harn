package io.harness.rest.helper.devxusergroups;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;

public class UserGroupsHelper  extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();


    public JsonPath createUserGroupSanity(String userGroupName,String userId , String type , String appId ) throws IOException {

        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERGROUP_CREATION_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(UserGroupsConstants.REQUEST_USERGROUP_CREATION_JSON);

        //Update the name dynamically
        JsonObject userGroup= appReqData.getAsJsonObject("userGroup");
        userGroup.addProperty("name",userGroupName);
        userGroup.addProperty("userIds",userId);

        JsonObject permissions= userGroup.getAsJsonObject("permissions");
        JsonArray appPermissions= permissions.getAsJsonArray("appPermissions");
        //Set env
        appPermissions.get(0).getAsJsonObject().addProperty("permissionType","ENV");
        JsonObject applications= appPermissions.get(0).getAsJsonObject().getAsJsonObject("applications");
        JsonObject environments= appPermissions.get(0).getAsJsonObject().getAsJsonObject("environments");
        JsonArray filterTypes= environments.getAsJsonArray("filterTypes");
        switch (type){
            case "PROD_ALL":
                applications.addProperty("filterType","ALL");
                filterTypes.add("PRODUCTION_ENVIRONMENTS");
                break;
            case "NON_PROD_ALL":
                applications.addProperty("filterType","ALL");
                filterTypes.add("NON_PRODUCTION_ENVIRONMENTS");
                break;
            case "PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appIds",appId);
                filterTypes.add("PRODUCTION_ENVIRONMENTS");
                break;
            case "NON_PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appIds",appId);
                filterTypes.add("NON_PRODUCTION_ENVIRONMENTS");
                break;
            case "NONE":
//                secretType.remove("usageScope");
                break;
        }

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification,PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    private void getUserGroupByNameAndDeleteIt(String rbacUserGroupName) throws IOException {
        //Delete if the usergroup already exists
        JsonPath userGroupByName = getUserGroupByName();
        Assert.assertTrue(userGroupByName.getString("data.userGroupByName.name").equalsIgnoreCase(rbacUserGroupName));
        String userGroupId = userGroupByName.getString("data.userGroupByName.id");

        JsonPath deleteUserGroup = deleteUserGroup(userGroupId);
        Assert.assertTrue(!deleteUserGroup.getString("data.deleteUserGroup.clientMutationId").isEmpty());

        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public JsonPath createUserGroup(String userGroupName,String userId , String type , String appId ) throws IOException {
        //Search and delete already existing usergroup
        getUserGroupByNameAndDeleteIt(userGroupName);

        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERGROUP_CREATION_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(UserGroupsConstants.REQUEST_USERGROUP_CREATION_JSON);

        //Update the name dynamically
        JsonObject userGroup= appReqData.getAsJsonObject("userGroup");
        userGroup.addProperty("name",userGroupName);
        userGroup.addProperty("userIds",userId);

        JsonObject permissions= userGroup.getAsJsonObject("permissions");
        JsonArray appPermissions= permissions.getAsJsonArray("appPermissions");
        //Set env
        appPermissions.get(0).getAsJsonObject().addProperty("permissionType","ENV");
        JsonObject applications= appPermissions.get(0).getAsJsonObject().getAsJsonObject("applications");
        JsonObject environments= appPermissions.get(0).getAsJsonObject().getAsJsonObject("environments");
        JsonArray filterTypes= environments.getAsJsonArray("filterTypes");
        switch (type){
            case "PROD_ALL":
                applications.addProperty("filterType","ALL");
                filterTypes.add("PRODUCTION_ENVIRONMENTS");
                break;
            case "NON_PROD_ALL":
                applications.addProperty("filterType","ALL");
                filterTypes.add("NON_PRODUCTION_ENVIRONMENTS");
                break;
            case "PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appIds",appId);
                filterTypes.add("PRODUCTION_ENVIRONMENTS");
                break;
            case "NON_PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appIds",appId);
                filterTypes.add("NON_PRODUCTION_ENVIRONMENTS");
                break;
            case "NONE":
//                secretType.remove("usageScope");
                break;
        }

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification,PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }



    public JsonPath getUsersList() throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERS_LIST);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }


    public JsonPath deleteUserGroup(String userGroupId) throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERGROUP_DELETE);
        JsonObject appReqData = jReader.readJSONFiles(UserGroupsConstants.REQUEST_USERGROUP_DELETE_JSON);


        //Update the id dynamically
        JsonObject userGroup= appReqData.getAsJsonObject("userGroup");
        userGroup.addProperty("userGroupId",userGroupId);

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));


        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification,PcfConstants.URI_APP_CREATION);

        return applicationResponse.jsonPath();
    }


    public JsonPath getUserGroupsList() throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERGROUP_LIST_INST_CREATION);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }


    public JsonPath getUserGroupsListRbac2() throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERGROUP_LIST_INST_CREATION);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject("devxteam@mailinator.com","Harness@123!");
        requestSpecification.body(graphqlPayload);
        requestSpecification.queryParam("accountId","-IWqU3BSSjqPbISDIW036Q");
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, "https://qa.harness.io/gateway/api/graphql");
        return applicationResponse.jsonPath();
    }


    public JsonPath getUserGroupByName() throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERGROUP_ByNAME_INST_CREATION);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath getUserGroupByNameTest() throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERGROUP_BYNAME_TEST);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath getUserByNameTest() throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERS_BY_NAME);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath getUserGroupByIdApi(String userGroupId) throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USERSGROUP_BY_ID);
        JsonObject appReqData = new JsonObject();

        appReqData.addProperty("name", userGroupId);
        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath getUserByIdApi(String rbacUserId) throws IOException {
        File file = new File(System.getProperty("user.dir")+ UserGroupsConstants.REQUEST_USER_BY_ID);
        JsonObject appReqData = new JsonObject();

        appReqData.addProperty("name", rbacUserId);
        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }
}
