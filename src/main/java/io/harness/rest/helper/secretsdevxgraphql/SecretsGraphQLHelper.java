package io.harness.rest.helper.secretsdevxgraphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vimalselvam.graphql.GraphqlTemplate;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

public class SecretsGraphQLHelper extends CoreUtils {

    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

    public JsonPath createSecretUsageScopeGeneric(String secretName, String type, String appId,String secretPass) throws IOException {
//        String secretManagerId = "mtGpFlPNSFaUJNUztLoIaA"; //Azure
        String secretManagerId = "mjcTTijZTIyAWZNzA1_wCw"; //Harness-KMS

//        String secretPass = "Mlg80bgo+ue5zF3o5/rT8xcXeKdXmPoNKgPTjIqK";
        File file = new File(System.getProperty("user.dir")+ SecretsGraphQLConstants.REQUEST_JSON_SECRETS_CREATION_QUERY);
        JsonObject requestData = jReader.readJSONFiles(SecretsGraphQLConstants.REQUEST_JSON_SECRETS_CREATION_PARAM);

        //Update the name dynamically
        JsonObject secret= requestData.getAsJsonObject("secret");
        JsonObject secretType =secret.getAsJsonObject("encryptedText");
        secretType.addProperty("secretManagerId",secretManagerId);
        secretType.addProperty("name",secretName);
        secretType.addProperty("value",secretPass);
        JsonObject usageScope =secretType.getAsJsonObject("usageScope");
        JsonObject appEnvScopes =usageScope.getAsJsonObject("appEnvScopes");
        JsonObject applications = new JsonObject();
        JsonObject environments = new JsonObject();
        switch (type){
            case "PROD_ALL":
                applications.addProperty("filterType","ALL");
                environments.addProperty("filterType","PRODUCTION_ENVIRONMENTS");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "NON_PROD_ALL":
                applications.addProperty("filterType","ALL");
                environments.addProperty("filterType","NON_PRODUCTION_ENVIRONMENTS");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.addProperty("filterType","PRODUCTION_ENVIRONMENTS");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "NON_PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.addProperty("filterType","NON_PRODUCTION_ENVIRONMENTS");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "LOCAL_ENV_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.remove("filterType");
                environments.addProperty("envId","lNnSwhWlQO2D4RcrRDr3LQ");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "LOCAL_ENV_ALL": // Error case, it should always throw error
                applications.addProperty("filterType","ALL");
                environments.remove("filterType");
                environments.addProperty("envId","lNnSwhWlQO2D4RcrRDr3LQ");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "NONE":
                secretType.remove("usageScope");
                break;
        }
        //Convert the json to ObjectNode which will be passed to create the graphQL payload
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode jsonNode = mapper.readValue(requestData.toString(), ObjectNode.class);
        System.out.println(mapper.writeValueAsString(jsonNode));

        //Convert the Query to graphQL accepted format.
        String graphqlPayload = GraphqlTemplate.parseGraphql(file, jsonNode);
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.body(graphqlPayload);
        Response applicationResponse = genericRequestBuilder.postCall(requestSpecification, SecretsGraphQLConstants.URI_APP_CREATION);
        return applicationResponse.jsonPath();
    }


    public JsonObject directUsageScopeInCp(String type, String appId, JsonObject pcfCloudProvider) {
        JsonObject usageScope =pcfCloudProvider.getAsJsonObject("usageScope");
        JsonObject appEnvScopes =usageScope.getAsJsonObject("appEnvScopes");
        JsonObject applications = new JsonObject();
        JsonObject environments = new JsonObject();
        switch (type){
            case "PROD_ALL":
                applications.addProperty("filterType","ALL");
                environments.addProperty("filterType","PRODUCTION_ENVIRONMENTS");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "NON_PROD_ALL":
                applications.addProperty("filterType","ALL");
                environments.addProperty("filterType","NON_PRODUCTION_ENVIRONMENTS");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.addProperty("filterType","PRODUCTION_ENVIRONMENTS");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "NON_PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.addProperty("filterType","NON_PRODUCTION_ENVIRONMENTS");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "LOCAL_ENV_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.remove("filterType");
                environments.addProperty("envId","lNnSwhWlQO2D4RcrRDr3LQ");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "LOCAL_ENV_ALL":  // Error case it should always throw error
                applications.addProperty("filterType","ALL");
                environments.remove("filterType");
                environments.addProperty("envId","lNnSwhWlQO2D4RcrRDr3LQ");
                appEnvScopes.add("application",applications);
                appEnvScopes.add("environment",environments);
                break;
            case "NONE":
                break;


        }

        return pcfCloudProvider;
    }

    public JsonObject directUsageScopeInSecrets(String type, String appId, JsonObject pcfCloudProvider) {
        JsonObject usageScope =pcfCloudProvider.getAsJsonObject("usageScope");
        JsonArray appEnvScopes =usageScope.getAsJsonArray("appEnvScopes");
        JsonObject applications = new JsonObject();
        JsonObject environments = new JsonObject();
        switch (type){
            case "PROD_ALL":
                applications.addProperty("filterType","ALL");
                environments.addProperty("filterType","PRODUCTION_ENVIRONMENTS");
                appEnvScopes.get(0).getAsJsonObject().add("application",applications);
                appEnvScopes.get(0).getAsJsonObject().add("environment",environments);
                break;
            case "NON_PROD_ALL":
                applications.addProperty("filterType","ALL");
                environments.addProperty("filterType","NON_PRODUCTION_ENVIRONMENTS");
                appEnvScopes.get(0).getAsJsonObject().add("application",applications);
                appEnvScopes.get(0).getAsJsonObject().add("environment",environments);
                break;
            case "PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.addProperty("filterType","PRODUCTION_ENVIRONMENTS");
                appEnvScopes.get(0).getAsJsonObject().add("application",applications);
                appEnvScopes.get(0).getAsJsonObject().add("environment",environments);
                break;
            case "NON_PROD_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.addProperty("filterType","NON_PRODUCTION_ENVIRONMENTS");
                appEnvScopes.get(0).getAsJsonObject().add("application",applications);
                appEnvScopes.get(0).getAsJsonObject().add("environment",environments);
                break;
            case "LOCAL_ENV_SINGLE_APP":
                applications.remove("filterType");
                applications.addProperty("appId",appId);
                environments.remove("filterType");
                environments.addProperty("envId","lNnSwhWlQO2D4RcrRDr3LQ");
                appEnvScopes.get(0).getAsJsonObject().add("application",applications);
                appEnvScopes.get(0).getAsJsonObject().add("environment",environments);
                break;
            case "LOCAL_ENV_ALL":  // Error case it should always throw error
                applications.addProperty("filterType","ALL");
                environments.remove("filterType");
                environments.addProperty("envId","lNnSwhWlQO2D4RcrRDr3LQ");
                appEnvScopes.get(0).getAsJsonObject().add("application",applications);
                appEnvScopes.get(0).getAsJsonObject().add("environment",environments);
                break;
            case "NONE":
                break;
        }

        return pcfCloudProvider;
    }
}