package io.harness.rest.helper.gitsync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.dxapplicationsgraphql.GraphQLApplicationConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GitSyncHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

    public JsonPath addGitSyncToApplication(String appId, String connectorId, String branchName, boolean syncEnabled) throws IOException {
        File file = new File(System.getProperty("user.dir")+ GitSyncConstants.ADD_GIT_CONFIG_TO_APP);
        JsonObject appReqData = jReader.readJSONFiles(GitSyncConstants.ADD_GIT_CONFIG_TO_APP_JSON);

//        //Update the name dynamically
        JsonObject gitConfig= appReqData.getAsJsonObject("gitConfig");
        gitConfig.addProperty("clientMutationId",appId);
        gitConfig.addProperty("applicationId",appId);
        gitConfig.addProperty("gitConnectorId",connectorId);

        gitConfig.addProperty("branch",branchName);
        gitConfig.addProperty("syncEnabled",syncEnabled);

        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GraphQLApplicationConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath removeGitSyncFromAnApplication(String appId) throws IOException {
        File file = new File(System.getProperty("user.dir")+ GitSyncConstants.REMOVE_GIT_SYNC_TO_APP);
        JsonObject appReqData = jReader.readJSONFiles(GitSyncConstants.REMOVE_GIT_SYNC_TO_APP_JSON);

//        //Update the name dynamically
        JsonObject gitConfig= appReqData.getAsJsonObject("removeGitConfig");
        gitConfig.addProperty("clientMutationId",appId);
        gitConfig.addProperty("applicationId",appId);

        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GraphQLApplicationConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath enableOrDisableGitSync(String appId,boolean syncEnabled) throws IOException {
        File file = new File(System.getProperty("user.dir")+ GitSyncConstants.ENABLE_GIT_SYNC_TO_APP);
        JsonObject appReqData = jReader.readJSONFiles(GitSyncConstants.ENABLE_GIT_SYNC_TO_APP_JSON);

//        //Update the name dynamically
        JsonObject status= appReqData.getAsJsonObject("status");
        status.addProperty("clientMutationId",appId);
        status.addProperty("applicationId",appId);
        status.addProperty("syncEnabled",syncEnabled);

        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GraphQLApplicationConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();

    }

    public JsonPath detailsOfGitSync(String appId) throws IOException  {
        File file = new File(System.getProperty("user.dir")+ GitSyncConstants.ASSOCIATED_GIT_CONNECTOR_DETAILS);
        JsonObject appReqData = new JsonObject();

        appReqData.addProperty("name",appId);

        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GraphQLApplicationConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();

    }

    public JsonPath listGitConnectors()  throws IOException  {
        File file = new File(System.getProperty("user.dir")+ GitSyncConstants.GIT_CONNECTORS_LIST);

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file,null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GraphQLApplicationConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();

    }
}
