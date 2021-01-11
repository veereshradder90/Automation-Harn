package io.harness.rest.helper.cerecommendations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.stream.MalformedJsonException;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

public class GraphQLRecommendationHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder =new GenericRequestBuilder();


    public JsonPath listAllRecommendations() throws IOException {
        File file = new File(System.getProperty("user.dir")+ GraphQLRecommendationConstants.REQUEST_LIST_RECOMMENDATIONS);
        JsonObject appReqData = jReader.readJSONFiles(GraphQLRecommendationConstants.REQUEST_JSON_LIST_RECOMMENDATIONS);
        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("accountId", defaultCEAccountId);
        requestSpecification.queryParam("__rtag", "recommendations_api");
        requestSpecification.body(graphqlPayload);
        System.out.println(mapper.writeValueAsString(graphqlPayload));

        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GraphQLRecommendationConstants.URI_LIST_RECOMMENDATIONS);
        return applicationResponse.jsonPath();
    }
    public JsonPath listWorkloadRecommendations() throws IOException {
        File file = new File(System.getProperty("user.dir")+ GraphQLRecommendationConstants.REQUEST_LIST_RECOMMENDATIONS);
        JsonObject appReqData = jReader.readJSONFiles(GraphQLRecommendationConstants.REQUEST_JSON_WORKLOAD_RECOMMENDATIONS);
        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("accountId", defaultCEAccountId);
        requestSpecification.queryParam("__rtag", "recommendations_api");
        requestSpecification.body(graphqlPayload);
        System.out.println(mapper.writeValueAsString(graphqlPayload));

        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GraphQLRecommendationConstants.URI_LIST_RECOMMENDATIONS);
        return applicationResponse.jsonPath();
    }
    
    public JsonPath billingTrendStatsValues() throws IOException, MalformedJsonException {
        File file = new File(System.getProperty("user.dir")+ GraphQLRecommendationConstants.REQUEST_BILLINGTRENDSTATS);
        JsonObject appReqData = jReader.readJSONFiles(GraphQLRecommendationConstants.REQUEST_JSON_BILLINGTRENDSTATS);
        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("accountId", defaultCEAccountId);
        requestSpecification.queryParam("__rtag", "explorer_summary_data");
        requestSpecification.body(graphqlPayload);
        System.out.println(mapper.writeValueAsString(graphqlPayload));

        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, GraphQLRecommendationConstants.URI_LIST_RECOMMENDATIONS);
        return applicationResponse.jsonPath();
    }
    
}
