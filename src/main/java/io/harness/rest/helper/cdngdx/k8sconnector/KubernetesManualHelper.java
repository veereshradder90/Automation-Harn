package io.harness.rest.helper.cdngdx.k8sconnector;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.applications.ApplicationConstants;
import io.harness.rest.helper.cdngdx.connectors.ConnectorsConstants;
import io.harness.rest.helper.cloudproviders.aws.AwsConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;

public class KubernetesManualHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

    public JsonPath createK8SManualConnector(String name , String identifier, String connectorType, String configType, String masterUrl, String specAuthType,String serviceAccountTokenRef) throws FileNotFoundException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        JsonObject connectorJson = jReader.readJSONFiles(KubernetesManualConstants.REQUEST_JSON_K8S_MANUAL);
        connectorJson.addProperty("name",name);
        connectorJson.addProperty("description",name);
        connectorJson.addProperty("identifier",identifier);
        connectorJson.addProperty("type",connectorType);
        JsonObject spec= connectorJson.getAsJsonObject("spec");
        spec.addProperty("type",configType);
        JsonObject ManualSpec= spec.getAsJsonObject("spec");
        ManualSpec.addProperty("masterUrl",masterUrl);
        JsonObject auth= ManualSpec.getAsJsonObject("auth");
        auth.addProperty("type",specAuthType);
        JsonObject authSpec= auth.getAsJsonObject("spec");
        authSpec.addProperty("serviceAcccountTokenRef",serviceAccountTokenRef);
        requestSpecification.body(connectorJson.toString());

        requestSpecification.pathParam("accounts","accountIdentifier");
        Response post = genericRequestBuilder.postCall(requestSpecification, ConnectorsConstants.CONNECTOR_END_POINT);
        return post.jsonPath();
    }

    public JsonPath updateK8SManualConnector(String name , String identifier, String connectorType, String configType, String masterUrl, String specAuthType,String serviceAccountTokenRef) throws FileNotFoundException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        JsonObject connectorJson = jReader.readJSONFiles(KubernetesManualConstants.REQUEST_JSON_K8S_MANUAL);
        connectorJson.addProperty("name",name);
        connectorJson.addProperty("description",name);
        connectorJson.addProperty("identifier",identifier);
        connectorJson.addProperty("type",connectorType);
        JsonObject spec= connectorJson.getAsJsonObject("spec");
        spec.addProperty("type",configType);
        JsonObject ManualSpec= spec.getAsJsonObject("spec");
        ManualSpec.addProperty("masterUrl",masterUrl);
        JsonObject auth= ManualSpec.getAsJsonObject("auth");
        auth.addProperty("type",specAuthType);
        JsonObject authSpec= auth.getAsJsonObject("spec");
        authSpec.addProperty("serviceAcccountTokenRef",serviceAccountTokenRef);
        requestSpecification.body(connectorJson.toString());

        requestSpecification.queryParam("accountIdentifier","accountIdentifier");
        Response post = genericRequestBuilder.putCall(requestSpecification, KubernetesManualConstants.URI_K8S_MANUAL);
        return post.jsonPath();
    }

    public JsonPath listK8SConnector(String accountIdentifier) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("accountIdentifier",accountIdentifier);
        Response post = genericRequestBuilder.getCall(requestSpecification, KubernetesManualConstants.URI_K8S_MANUAL);
        return post.jsonPath();
    }

    public JsonPath getK8SConnector(String identifier, String accountIdentifier) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("accountIdentifier",accountIdentifier);
        requestSpecification.pathParam("connectors", identifier);
        Response getK8SConnector =
                genericRequestBuilder.getCall(requestSpecification, KubernetesManualConstants.GET_K8S_MANUAL);
        return getK8SConnector.jsonPath();
    }

    public JsonPath deleteK8SConnector(String identifier, String accountIdentifier) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("accountIdentifier",accountIdentifier);
        requestSpecification.pathParam("connectors", identifier);
        Response getK8SConnector =
                genericRequestBuilder.deleteCall(requestSpecification, KubernetesManualConstants.GET_K8S_MANUAL);

        System.out.println("Method :"+getK8SConnector.jsonPath().toString());
        return getK8SConnector.jsonPath();
    }
}
