package io.harness.rest.helper.dxaudittrails;

import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.devxusergroups.UserGroupsConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

public class AuditTrailsHelper extends CoreUtils {

    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();


    public JsonPath FetchAuditWithoutFilter() throws IOException {
        File file = new File(System.getProperty("user.dir")+ AuditTrailsConstants.REQUEST_JSON_FETCH_AUDIT_NO_FILTERS);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, AuditTrailsConstants.AUDIT_END_POINT);
        return applicationResponse.jsonPath();
    }

    public JsonPath FetchAuditSpecificTimeRange() throws IOException {
        File file = new File(System.getProperty("user.dir")+ AuditTrailsConstants.REQUEST_JSON_FETCH_AUDIT_TIME_RANGE);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, AuditTrailsConstants.AUDIT_END_POINT);
        return applicationResponse.jsonPath();
    }

    public JsonPath FetchAuditRelativeTime() throws IOException {
        File file = new File(System.getProperty("user.dir")+ AuditTrailsConstants.REQUEST_JSON_FETCH_AUDIT_RELATIVE_TIME_RANGE);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, AuditTrailsConstants.AUDIT_END_POINT);
        return applicationResponse.jsonPath();
    }

    public JsonPath FetchAuditYamlChangeSet() throws IOException {
        File file = new File(System.getProperty("user.dir")+ AuditTrailsConstants.REQUEST_JSON_FETCH_AUDIT_YAML_DIFF);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, AuditTrailsConstants.AUDIT_END_POINT);
        return applicationResponse.jsonPath();
    }

    public JsonPath FetchAuditYamlChangeSetAndResourceId() throws IOException {
        File file = new File(System.getProperty("user.dir")+ AuditTrailsConstants.REQUEST_JSON_FETCH_AUDIT_CHANGESET_AND_RESOURCE);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, AuditTrailsConstants.AUDIT_END_POINT);
        return applicationResponse.jsonPath();
    }


    public JsonPath FetchAuditCombinedQuery() throws IOException {
        File file = new File(System.getProperty("user.dir")+ AuditTrailsConstants.REQUEST_JSON_FETCH_AUDIT_COMBINED_QUERIES);
        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, null);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, AuditTrailsConstants.AUDIT_END_POINT);
        return applicationResponse.jsonPath();
    }
}
