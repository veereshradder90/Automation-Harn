package io.harness.rest.helper.dxsecrets.ssh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.dxsecrets.encryptedfile.EncryptedFileTypeConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

public class SshHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
    SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();

    public JsonPath getSSHSecretsByIdSanity(String etId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstants.SSH_GET_BY_ID);
        JsonObject appReqData = new JsonObject();

        appReqData.addProperty("name", etId);
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

    public JsonPath getSSHSecretsByNameSanity(String etName) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstants.SSH_GET_BY_NAME);
        JsonObject appReqData = new JsonObject();

        appReqData.addProperty("name", etName);
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