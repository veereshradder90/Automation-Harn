package io.harness.rest.helper.secrets.winRM;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cloudproviders.pcf.PcfConstants;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @author Boopesh.Shanmugam
 * @apiNote Serves as the Helper class for WinRMGraph scripts that handles test automation for WINRM secrets
 */

@Slf4j
public class WinRmHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
    SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();

    public JsonPath createWinRMUsingNTLM(String WinRMName, String userName, String domain, String password, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + winRmConstants.WINRM_CREATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(winRmConstants.WINRM_CREATE_NTLM_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject winRMCredential = secret.getAsJsonObject("winRMCredential");
        winRMCredential.addProperty("name", WinRMName);
        winRMCredential.addProperty("userName",userName );
        winRMCredential.addProperty("passwordSecretId", password);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, winRMCredential);


        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath updateWinRMUsingNTLM(String WinRMName, String userName, String domain, String password,String WinId, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + winRmConstants.WINRM_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(winRmConstants.WINRM_UPDATE_NTLM_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject winRMCredential = secret.getAsJsonObject("winRMCredential");
        secret.addProperty("secretId",WinId);
        winRMCredential.addProperty("name", WinRMName);
        winRMCredential.addProperty("userName",userName );
        winRMCredential.addProperty("passwordSecretId", password);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, winRMCredential);


        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath createWinRMUsingNTLMAdvanced(String WinRMName, String userName, String domain, String password, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + winRmConstants.WINRM_CREATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(winRmConstants.WINRM_CREATE_NTLM_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject winRMCredential = secret.getAsJsonObject("winRMCredential");
        winRMCredential.addProperty("name", WinRMName);
        winRMCredential.addProperty("userName",userName );
        winRMCredential.addProperty("passwordSecretId", password);
        winRMCredential.addProperty("useSSL",true);
        winRMCredential.addProperty("skipCertCheck",true);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, winRMCredential);


        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath updateWinRMUsingNTLMAdvanced(String WinRMName, String userName, String domain, String password,String WinId, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + winRmConstants.WINRM_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(winRmConstants.WINRM_UPDATE_NTLM_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject winRMCredential = secret.getAsJsonObject("winRMCredential");
        secret.addProperty("secretId",WinId);
        winRMCredential.addProperty("name", WinRMName);
        winRMCredential.addProperty("userName",userName );
        winRMCredential.addProperty("passwordSecretId", password);
        winRMCredential.addProperty("useSSL",true);
        winRMCredential.addProperty("skipCertCheck",true);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, winRMCredential);


        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }

    public JsonPath deleteWinRm(String winId) throws IOException {
        File file = new File(System.getProperty("user.dir") + winRmConstants.WINRM_DELETE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(winRmConstants.WINRM_DELETE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        secret.addProperty("secretId", winId);
        secret.addProperty("secretType", "WINRM_CREDENTIAL");

        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }
    public JsonPath getSecretsByName(String winRmName) throws IOException {
        File file = new File(System.getProperty("user.dir") + winRmConstants.WINRM_GETBYNAME_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(winRmConstants.WINRM_GETBY_JSON);

        appReqData.addProperty("name", winRmName);
        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);

        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }
    public JsonPath getSecretsById(String id) throws IOException {
        File file = new File(System.getProperty("user.dir") + winRmConstants.WINRM_GETBYID_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(winRmConstants.WINRM_GETBY_JSON);

        appReqData.addProperty("name", id);
        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(appReqData.toString(), ObjectNode.class);

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);

        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, PcfConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }


}
