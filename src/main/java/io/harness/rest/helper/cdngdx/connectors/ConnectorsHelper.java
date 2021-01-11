package io.harness.rest.helper.cdngdx.connectors;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cdngdx.k8sconnector.KubernetesManualConstants;
import io.harness.rest.helper.cdngdx.project.ProjectLevelConnectorsConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

public class ConnectorsHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();


    public JsonPath createProjectK8SConnector(JsonObject spec , String name , String identifier, String orgId , String projectId, String connectorType) throws IOException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        JsonObject connectorJson = new JsonObject();

        connectorJson.addProperty("name",name);
        connectorJson.addProperty("description",name);
        connectorJson.addProperty("identifier",identifier);
        connectorJson.addProperty("orgIdentifier",orgId);
        connectorJson.addProperty("projectIdentifer",projectId);
        connectorJson.addProperty("type",connectorType);

        connectorJson.remove("spec");
        connectorJson.add("spec",spec);

        System.out.println("JSON: "+connectorJson.toString());
        requestSpecification.body(connectorJson.toString());
        requestSpecification.pathParam("accounts","zEaak-FLS425IEO7OLzMUg");
        Response post = genericRequestBuilder.postCall(requestSpecification, ConnectorsConstants.CONNECTOR_END_POINT);
        return post.jsonPath();
    }

//    String accountIdentifier="zEaak-FLS425IEO7OLzMUg";

    public JsonPath createOrgK8SConnector(JsonObject spec , String name , String identifier, String orgId  ,String connectorType) throws IOException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        JsonObject connectorJson = new JsonObject();

        connectorJson.addProperty("name",name);
        connectorJson.addProperty("description",name);
        connectorJson.addProperty("identifier",identifier);
        connectorJson.addProperty("orgIdentifier",orgId);
        connectorJson.addProperty("type",connectorType);

        connectorJson.remove("spec");
        connectorJson.add("spec",spec);

        System.out.println("JSON: "+connectorJson.toString());
        requestSpecification.body(connectorJson.toString());
        requestSpecification.pathParam("accounts","zEaak-FLS425IEO7OLzMUg");
        Response post = genericRequestBuilder.postCall(requestSpecification, ConnectorsConstants.CONNECTOR_END_POINT);
        return post.jsonPath();
    }


    public JsonPath createAccountK8SConnector(JsonObject spec , String name , String identifier,String connectorType) throws IOException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        JsonObject connectorJson = new JsonObject();
        connectorJson.addProperty("name",name);
        connectorJson.addProperty("description",name);
        connectorJson.addProperty("identifier",identifier);
        connectorJson.addProperty("type",connectorType);

        connectorJson.remove("spec");
        connectorJson.add("spec",spec);

        System.out.println("JSON: "+connectorJson.toString());
        requestSpecification.body(connectorJson.toString());
        requestSpecification.pathParam("accounts","zEaak-FLS425IEO7OLzMUg");
        Response post = genericRequestBuilder.postCall(requestSpecification, ConnectorsConstants.CONNECTOR_END_POINT);
        return post.jsonPath();
    }

    public JsonPath listAccountK8SConnector(String accountIdentifier) throws IOException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accountIdentifier",accountIdentifier);
        Response post = genericRequestBuilder.getCall(requestSpecification, ConnectorsConstants.LIST_CONNECTOR_END_POINT);
        return post.jsonPath();
    }

    public JsonPath listOrgK8SConnector(String accountIdentifier, String orgIdentifier) throws IOException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accountIdentifier",accountIdentifier);
        requestSpecification.pathParam("orgIdentifier",orgIdentifier);
        Response post = genericRequestBuilder.getCall(requestSpecification, ConnectorsConstants.LIST_ORG_CONNECTOR_END_POINT);
        return post.jsonPath();
    }

    public JsonPath listProjK8SConnector(String accountIdentifier, String orgIdentifier,String projectIdentifier) throws IOException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accountIdentifier",accountIdentifier);
        requestSpecification.pathParam("orgIdentifier",orgIdentifier);
        requestSpecification.pathParam("projectIdentifier",projectIdentifier);
        Response post = genericRequestBuilder.getCall(requestSpecification, ConnectorsConstants.LIST_PROJ_CONNECTOR_END_POINT);
        return post.jsonPath();
    }

    public JsonPath getK8SConnector(String identifier, String accountIdentifier) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accountIdentifier",accountIdentifier);
        requestSpecification.pathParam("connectors", identifier);
        Response getK8SConnector =
                genericRequestBuilder.getCall(requestSpecification,  ConnectorsConstants.GET_CONNECTOR_END_POINT);
        return getK8SConnector.jsonPath();
    }

    public JsonPath getK8SOrgConnector(String identifier, String accountIdentifier, String orgIdentifier) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accountIdentifier",accountIdentifier);
        requestSpecification.pathParam("connectors", identifier);
        requestSpecification.pathParam("orgIdentifier", orgIdentifier);
        Response getK8SConnector =
                genericRequestBuilder.getCall(requestSpecification,  ConnectorsConstants.GET_ORG_CONNECTOR_END_POINT);
        return getK8SConnector.jsonPath();
    }

    public JsonPath getK8SOProjConnector(String identifier, String accountIdentifier, String orgIdentifier, String projectIdentifer) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accountIdentifier",accountIdentifier);
        requestSpecification.pathParam("connectors", identifier);
        requestSpecification.pathParam("orgIdentifier", orgIdentifier);
        requestSpecification.pathParam("projectIdentifier", projectIdentifer);
        Response getK8SConnector =
                genericRequestBuilder.getCall(requestSpecification,  ConnectorsConstants.GET_PROJ_CONNECTOR_END_POINT);
        return getK8SConnector.jsonPath();
    }

    public JsonPath deleteK8SConnector(String identifier, String accountIdentifier) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accounts",accountIdentifier);
        requestSpecification.pathParam("connectors", identifier);
        Response getK8SConnector =
                genericRequestBuilder.deleteCall(requestSpecification,  ConnectorsConstants.DELETE_CONNECTOR_END_POINT);

        System.out.println("Method :"+getK8SConnector.jsonPath().toString());
        return getK8SConnector.jsonPath();
    }

    public JsonPath deleteK8SOrgConnector(String identifier, String accountIdentifier, String orgIdentifier) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accounts",accountIdentifier);
        requestSpecification.pathParam("connectors", identifier);
        requestSpecification.pathParam("orgIdentifier", orgIdentifier);
        Response getK8SConnector =
                genericRequestBuilder.deleteCall(requestSpecification,  ConnectorsConstants.DELETE_ORG_CONNECTOR_END_POINT);

        System.out.println("Method :"+getK8SConnector.jsonPath().toString());
        return getK8SConnector.jsonPath();
    }

    public JsonPath deleteK8SProjConnector(String identifier, String accountIdentifier, String orgIdentifier,String projectIdentifier ) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.pathParam("accounts",accountIdentifier);
        requestSpecification.pathParam("connectors", identifier);
        requestSpecification.pathParam("orgIdentifier", orgIdentifier);
        requestSpecification.pathParam("projectIdentifier", projectIdentifier);
        Response getK8SConnector =
                genericRequestBuilder.deleteCall(requestSpecification,  ConnectorsConstants.DELETE_PROJ_CONNECTOR_END_POINT);

        System.out.println("Method :"+getK8SConnector.jsonPath().toString());
        return getK8SConnector.jsonPath();
    }

    public JsonPath updateAccountK8SConnector(JsonObject spec , String name , String identifier,String connectorType) throws IOException {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        JsonObject connectorJson = new JsonObject();

        connectorJson.addProperty("name",name);
        connectorJson.addProperty("description",name);
        connectorJson.addProperty("identifier",identifier);
        connectorJson.addProperty("type",connectorType);

        connectorJson.remove("spec");
        connectorJson.add("spec",spec);

        System.out.println("JSON: "+connectorJson.toString());
        requestSpecification.body(connectorJson.toString());
        requestSpecification.pathParam("accounts","zEaak-FLS425IEO7OLzMUg");
        Response post = genericRequestBuilder.putCall(requestSpecification, ConnectorsConstants.CONNECTOR_END_POINT);
        return post.jsonPath();
    }

    public JsonPath updateOrgK8SConnector(JsonObject spec, String name, String identifier, String connectorType, String orgId) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        JsonObject connectorJson = new JsonObject();

        connectorJson.addProperty("name",name);
        connectorJson.addProperty("description",name);
        connectorJson.addProperty("identifier",identifier);
        connectorJson.addProperty("orgIdentifier",orgId);
        connectorJson.addProperty("type",connectorType);

        connectorJson.remove("spec");
        connectorJson.add("spec",spec);

        System.out.println("JSON: "+connectorJson.toString());
        requestSpecification.body(connectorJson.toString());
        requestSpecification.pathParam("accounts","zEaak-FLS425IEO7OLzMUg");
        Response post = genericRequestBuilder.putCall(requestSpecification, ConnectorsConstants.CONNECTOR_END_POINT);
        return post.jsonPath();

    }

    public JsonPath updateProjectK8SConnector(JsonObject spec, String name, String identifier, String connectorType, String orgId, String projectIdentifer) {
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        JsonObject connectorJson = new JsonObject();

        connectorJson.addProperty("name",name);
        connectorJson.addProperty("description",name);
        connectorJson.addProperty("identifier",identifier);
        connectorJson.addProperty("orgIdentifier",orgId);
        connectorJson.addProperty("projectIdentifer",projectIdentifer);
        connectorJson.addProperty("type",connectorType);

        connectorJson.remove("spec");
        connectorJson.add("spec",spec);

        System.out.println("JSON: "+connectorJson.toString());
        requestSpecification.body(connectorJson.toString());
        requestSpecification.pathParam("accounts","zEaak-FLS425IEO7OLzMUg");
        Response post = genericRequestBuilder.putCall(requestSpecification, ConnectorsConstants.CONNECTOR_END_POINT);
        return post.jsonPath();
    }
}
