package io.harness.rest.helper.secrets.sshGraphql;
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
 * @apiNote Serves as the Helper class for SshGraphql script that contains the test cases for SSH secrets
 */

@Slf4j
public class SshHelperGraphql extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
    SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();
    public  JsonPath createSshKey(String sshName, String username,  String credentials, SshType keytype, String type, String appId) throws IOException{
        JsonPath response=null;

            switch (keytype) {
                case INLINE:
                    response = createSshUsingInline(sshName, username, credentials, type, appId);
                    return response;
                case PATH:
                    response = createSshUsingKeyPath(sshName, username, credentials, type, appId);
                    return response;
                case PASSWORD:
                    response = createSshUsingPassword(sshName, username, credentials, type, appId);
                    return response;
                default:
                    System.out.println("Wrong SSH Credential");
                    return response;
            }


    }

    public  JsonPath updateSshKey(String sshName, String username,  String credentials, String uuid, SshType keytype, String type, String appId) throws IOException {
        JsonPath response = null;

            switch (keytype) {
                case INLINE:
                    response = updateSshWithInline(sshName, username, credentials, uuid, type, appId);
                    return response;
                case PATH:
                    response = updateSshWithKeyPath(sshName, username, credentials, uuid, type, appId);
                    return response;
                case PASSWORD:
                    response = updateSshWithPassword(sshName, username, credentials, uuid, type, appId);
                    return response;
                default:
                    System.out.println("Wrong SSH Credential");
                    return response;
            }

    }


    public JsonPath createSshUsingInline(String sshName, String userName, String inlineKey, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_CREATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSHInline_CREATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject sshAuthentication = sshCredential.getAsJsonObject("sshAuthentication");
        JsonObject sshAuthenticationMethod = sshAuthentication.getAsJsonObject("sshAuthenticationMethod");
        JsonObject inlineSSHKey = sshAuthenticationMethod.getAsJsonObject("inlineSSHKey");
        sshCredential.addProperty("name", sshName);
        sshAuthentication.addProperty("userName",userName );
        inlineSSHKey.addProperty("sshKeySecretFileId", inlineKey);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);


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

    public JsonPath updateSshWithInline(String sshName, String userName, String inlineKey, String sshId, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSHInline_UPDATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject sshAuthentication = sshCredential.getAsJsonObject("sshAuthentication");
        JsonObject sshAuthenticationMethod = sshAuthentication.getAsJsonObject("sshAuthenticationMethod");
        JsonObject inlineSSHKey = sshAuthenticationMethod.getAsJsonObject("inlineSSHKey");
        secret.addProperty("secretId",sshId);
        sshCredential.addProperty("name", sshName);
        sshAuthentication.addProperty("userName",userName );
        inlineSSHKey.addProperty("sshKeySecretFileId", inlineKey);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);

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

    public JsonPath deleteSsh(String sshId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_DELETE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSH_DELETE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        secret.addProperty("secretId", sshId);
        secret.addProperty("secretType", "SSH_CREDENTIAL");

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
    public JsonPath createSshUsingKeyPath(String sshName, String userName, String keyPath, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_CREATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSHKeyPath_CREATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject sshAuthentication = sshCredential.getAsJsonObject("sshAuthentication");
        JsonObject sshAuthenticationMethod = sshAuthentication.getAsJsonObject("sshAuthenticationMethod");
        JsonObject sshKeyFile = sshAuthenticationMethod.getAsJsonObject("sshKeyFile");
        sshCredential.addProperty("name", sshName);
        sshAuthentication.addProperty("userName",userName );
        sshKeyFile.addProperty("path", keyPath);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);


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
    public JsonPath updateSshWithKeyPath(String sshName, String userName, String keyPath, String sshId, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSHKeyPath_UPDATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject sshAuthentication = sshCredential.getAsJsonObject("sshAuthentication");
        JsonObject sshAuthenticationMethod = sshAuthentication.getAsJsonObject("sshAuthenticationMethod");
        JsonObject sshKeyFile = sshAuthenticationMethod.getAsJsonObject("sshKeyFile");
        secret.addProperty("secretId",sshId);
        sshCredential.addProperty("name", sshName);
        sshAuthentication.addProperty("userName",userName );
        sshKeyFile.addProperty("path", keyPath);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);

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
    public JsonPath createSshUsingPassword(String sshName, String userName, String password, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_CREATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSHPassword_CREATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject sshAuthentication = sshCredential.getAsJsonObject("sshAuthentication");
        JsonObject sshAuthenticationMethod = sshAuthentication.getAsJsonObject("sshAuthenticationMethod");
        JsonObject serverPassword = sshAuthenticationMethod.getAsJsonObject("serverPassword");
        sshCredential.addProperty("name", sshName);
        sshAuthentication.addProperty("userName",userName );
        serverPassword.addProperty("passwordSecretId", password);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);


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
    public JsonPath updateSshWithPassword(String sshName, String userName, String password, String sshId, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSHPassword_UPDATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject sshAuthentication = sshCredential.getAsJsonObject("sshAuthentication");
        JsonObject sshAuthenticationMethod = sshAuthentication.getAsJsonObject("sshAuthenticationMethod");
        JsonObject serverPassword = sshAuthenticationMethod.getAsJsonObject("serverPassword");
        secret.addProperty("secretId",sshId);
        sshCredential.addProperty("name", sshName);
        sshAuthentication.addProperty("userName",userName );
        serverPassword.addProperty("passwordSecretId", password);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);

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

    public JsonPath createSshUsingKerberosKeytab(String sshName, String principal, String realm, String keyPath, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_CREATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSH_KERBEROS_KEYTAB_CREATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject kerberosAuthentication = sshCredential.getAsJsonObject("kerberosAuthentication");
        JsonObject tgtGenerationMethod = kerberosAuthentication.getAsJsonObject("tgtGenerationMethod");
        JsonObject keyTabFile = tgtGenerationMethod.getAsJsonObject("keyTabFile");
        sshCredential.addProperty("name", sshName);
        kerberosAuthentication.addProperty("principal",principal );
        kerberosAuthentication.addProperty("realm",realm );
        keyTabFile.addProperty("filePath", keyPath);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);


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

    public JsonPath updateSshUsingKerberosKeytab(String sshName, String principal, String realm, String keyPath, String sshId, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSH_KERBEROS_KEYTAB_UPDATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject kerberosAuthentication = sshCredential.getAsJsonObject("kerberosAuthentication");
        JsonObject tgtGenerationMethod = kerberosAuthentication.getAsJsonObject("tgtGenerationMethod");
        JsonObject keyTabFile = tgtGenerationMethod.getAsJsonObject("keyTabFile");
        secret.addProperty("secretId",sshId);
        sshCredential.addProperty("name", sshName);
        kerberosAuthentication.addProperty("principal",principal );
        kerberosAuthentication.addProperty("realm",realm );
        keyTabFile.addProperty("filePath", keyPath);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);


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


    public JsonPath createSshUsingKerberosPassword(String sshName, String principal, String realm, String password, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_CREATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSH_KERBEROS_PASSWORD_CREATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject kerberosAuthentication = sshCredential.getAsJsonObject("kerberosAuthentication");
        JsonObject tgtGenerationMethod = kerberosAuthentication.getAsJsonObject("tgtGenerationMethod");
        JsonObject kerberosPassword = tgtGenerationMethod.getAsJsonObject("kerberosPassword");
        sshCredential.addProperty("name", sshName);
        kerberosAuthentication.addProperty("principal",principal );
        kerberosAuthentication.addProperty("realm",realm );
        kerberosPassword.addProperty("passwordSecretId", password);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);


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

    public JsonPath updateSshUsingKerberosPassword(String sshName, String principal, String realm, String password, String sshId, String type, String appId) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_UPDATE_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSH_KERBEROS_PASSWORD_UPDATE_JSON);

        //Update the name dynamically
        JsonObject secret = appReqData.getAsJsonObject("secret");
        JsonObject sshCredential = secret.getAsJsonObject("sshCredential");
        JsonObject kerberosAuthentication = sshCredential.getAsJsonObject("kerberosAuthentication");
        JsonObject tgtGenerationMethod = kerberosAuthentication.getAsJsonObject("tgtGenerationMethod");
        JsonObject kerberosPassword = tgtGenerationMethod.getAsJsonObject("kerberosPassword");
        secret.addProperty("secretId",sshId);
        sshCredential.addProperty("name", sshName);
        kerberosAuthentication.addProperty("principal",principal );
        kerberosAuthentication.addProperty("realm",realm );
        kerberosPassword.addProperty("passwordSecretId", password);
        secretsGraphQLHelper.directUsageScopeInSecrets(type, appId, sshCredential);


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

    public JsonPath getSecretsByName(String sshName) throws IOException {
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_GETNAME_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSH_GETNAME_JSON);

        appReqData.addProperty("name", sshName);
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
        File file = new File(System.getProperty("user.dir") + SshConstantsGraphql.SSH_GETID_QUERY);
        JsonObject appReqData = jReader.readJSONFiles(SshConstantsGraphql.SSH_GETID_JSON);

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
