package io.harness.rest.helper.dxsecrets.encryptedtext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.cloudproviders.physicaldata.PhysicalDataCPConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

public class EncryptedTextHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
    SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();

    public JsonPath createEncryptedText(String secretName, String secretManagerId, String secretReference, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_CREATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(EncryptedTextConstants.ENCRYPTED_TEXT_CREATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject encryptedText = secret.getAsJsonObject("encryptedText");
        encryptedText.addProperty("name", secretName);
        encryptedText.addProperty("secretReference", secretReference);
        encryptedText.addProperty("secretManagerId", secretManagerId);

        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, encryptedText);

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

    public JsonPath updateEncryptedText(String secretName, String secretId, String secretManagerId, String secretReference, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(EncryptedTextConstants.ENCRYPTED_TEXT_UPDATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        secret.addProperty("secretId", secretId);
        JsonObject encryptedText = secret.getAsJsonObject("encryptedText");
        encryptedText.addProperty("name", secretName);
        encryptedText.addProperty("secretReference", secretReference);

        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, encryptedText);

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

    public JsonPath deleteEncryptedText(String secretId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SecretsGraphQLConstants.REQUEST_JSON_SECRETS_DELETION_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SecretsGraphQLConstants.REQUEST_JSON_SECRETS_DELETION_PARAM);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        secret.addProperty("secretId", secretId);
        secret.addProperty("secretType", "ENCRYPTED_TEXT");

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

    public JsonPath getSecretsByNameTest(String secretName) throws IOException {
        File file = new File(System.getProperty("user.dir") + SecretsGraphQLConstants.REQUEST_GET_BY_NAME_SECRET);
        JsonObject appReqData = jReader.readJSONFiles(SecretsGraphQLConstants.REQUEST_GET_BY_NAME_SECRET_JSON);

        appReqData.addProperty("name", secretName);
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


    public JsonPath getEncryptedTextSecretsByNameSanity(String secretName) throws IOException {
        File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_GET_BY_NAME);
        JsonObject appReqData = new JsonObject();

        appReqData.addProperty("name", secretName);
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

    public JsonPath getEncryptedTextSecretsByIdSanity(String secretName) throws IOException {
            File file = new File(System.getProperty("user.dir") + EncryptedTextConstants.ENCRYPTED_TEXT_GET_BY_ID);
            JsonObject appReqData = new JsonObject();

            appReqData.addProperty("name", secretName);
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
