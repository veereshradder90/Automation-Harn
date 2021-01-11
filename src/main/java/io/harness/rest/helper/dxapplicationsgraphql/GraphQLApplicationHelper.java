package io.harness.rest.helper.dxapplicationsgraphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.applications.ApplicationConstants;
import io.harness.rest.helper.cloudproviders.cpcrud.CPCrud;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.cloudproviders.spotinst.SpotInstCPConstants;
import io.harness.rest.helper.devxusergroups.UserGroupsConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class GraphQLApplicationHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder =new GenericRequestBuilder();

    public JsonPath createApplication(String appName) throws IOException {
        File file = new File(System.getProperty("user.dir")+ GraphQLApplicationConstants.REQUEST_JSON_APP_CREATION_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(GraphQLApplicationConstants.REQUEST_JSON_APP_CREATION_JSON);

        //Update the name dynamically
        JsonObject appData= appReqData.getAsJsonObject("app");

        appData.addProperty("clientMutationId",appName);
        appData.addProperty("name",appName);
        appData.addProperty("description",appName+ RandomStringUtils.randomAlphanumeric(11));

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

    public JsonPath updateApplication(String appName,String appId) throws IOException {

        File file = new File(System.getProperty("user.dir")+ GraphQLApplicationConstants.REQUEST_JSON_APP_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(GraphQLApplicationConstants.REQUEST_JSON_APP_UPDATE_JSON);

        //Update the name dynamically
        JsonObject appData= appReqData.getAsJsonObject("app");
        appData.addProperty("applicationId",appId);
        appData.addProperty("clientMutationId",appName+"-Updated");
        appData.addProperty("name",appName);
        appData.addProperty("description",appName+ RandomStringUtils.randomAlphanumeric(11));

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



    public JsonPath deleteApplication(String appName,String appId) throws IOException {

        File file = new File(System.getProperty("user.dir")+ GraphQLApplicationConstants.REQUEST_JSON_APP_DELETE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(GraphQLApplicationConstants.REQUEST_JSON_APP_DELETE_JSON);

        //Update the name dynamically
        JsonObject appData= appReqData.getAsJsonObject("app");
        appData.addProperty("applicationId",appId);
        appData.addProperty("clientMutationId",appName+"-Updated");

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

    public JsonPath getApplicationByName(String appName) throws IOException {
        File file = new File(System.getProperty("user.dir")+ GraphQLApplicationConstants.REQUEST_JSON_APP_GETBYNAME_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(GraphQLApplicationConstants.REQUEST_JSON_APP_GETBYNAME_QUERY_JSON);

        appReqData.addProperty("name", appName);
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

    public JsonPath getApplicationById(String appId) throws IOException {
        File file = new File(System.getProperty("user.dir")+ GraphQLApplicationConstants.REQUEST_JSON_APP_GETBYID);
        JsonObject appReqData =new JsonObject();
        appReqData.addProperty("name", appId);
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

    public JsonPath listApplications() throws IOException {
        File file = new File(System.getProperty("user.dir")+ GraphQLApplicationConstants.REQUEST_APP_LIST_INST_CREATION);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath listApplicationsByFilters() throws IOException {
        File file = new File(System.getProperty("user.dir")+ GraphQLApplicationConstants.REQUEST_APP_LIST_BY_FILTER_INST_CREATION);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath listApplicationsByFiltersRBAC() throws IOException {

        File file = new File(System.getProperty("user.dir")+ GraphQLApplicationConstants.REQUEST_APP_LIST_BY_FILTER_INST_CREATION);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject("cdc-automation-user@mailinator.com","Harness@123");
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }
}
