package io.harness.rest.helper.cdngdx.project;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.cdngdx.connectors.ConnectorsConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

public class ProjectConnectorHelper extends CoreUtils {

    GenericRequestBuilder genericRequestBuilder =new GenericRequestBuilder();

//    public JsonPath createProjectK8SConnector(JsonObject spec , String name , String identifier, String orgId , String projectId, String connectorType) throws IOException {
//        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
//        JsonObject connectorJson = jReader.readJSONFiles(ProjectLevelConnectorsConstants.PROJECT_CREATE_K8S_SERVICE_TOKEN);
//
//        connectorJson.addProperty("name",name);
//        connectorJson.addProperty("description",name);
//        connectorJson.addProperty("identifier",identifier);
//        connectorJson.addProperty("orgIdentifier",orgId);
//        connectorJson.addProperty("projectIdentifer",projectId);
//        connectorJson.addProperty("type",connectorType);
//
//        connectorJson.remove("spec");
//        connectorJson.add("spec",spec);
//
//        System.out.println("JSON: "+connectorJson.toString());
//        requestSpecification.body(connectorJson.toString());
//        requestSpecification.pathParam("accounts","accountIdentifier");
//        Response post = genericRequestBuilder.postCall(requestSpecification, ConnectorsConstants.CONNECTOR_END_POINT);
//        return post.jsonPath();
//    }


//    public JsonObject createK8SManualUserNamePasswordJsonKey(String specAuthType, String masterUrl, String username , String passref, String cacert){
//      JsonObject spec = new JsonObject();
//      spec.addProperty("type",specAuthType);
//
//      JsonObject manualSpec = new JsonObject();
//      manualSpec.addProperty("masterUrl",masterUrl);
//
//      JsonObject auth = new JsonObject();
//      auth.addProperty("type","UsernamePassword");
//
//      JsonObject UsernamePasswordSpec = new JsonObject();
//      UsernamePasswordSpec.addProperty("username",username);
//      UsernamePasswordSpec.addProperty("passwordRef",passref);
//      UsernamePasswordSpec.addProperty("cacert",cacert);
//
//      auth.add("spec",UsernamePasswordSpec);
//      manualSpec.add("auth",auth);
//      spec.add("spec",manualSpec);
//
//      return spec;
//    }
//
//
//    public JsonObject createK8SManualServiceTokenJsonKey(String configType, String specAuthType, String masterUrl, String serviceAcccountTokenRef){
//        JsonObject spec = new JsonObject();
//        spec.addProperty("type",specAuthType);
//
//        JsonObject manualSpec = new JsonObject();
//        manualSpec.addProperty("masterUrl",masterUrl);
//
//        JsonObject auth = new JsonObject();
//        auth.addProperty("type",configType);
//
//        JsonObject UsernamePasswordSpec = new JsonObject();
//        UsernamePasswordSpec.addProperty("serviceAccountTokenRef",serviceAcccountTokenRef);
//
//        auth.add("spec",UsernamePasswordSpec);
//        manualSpec.add("auth",auth);
//        spec.add("spec",manualSpec);
//
//        return spec;
//    }

}
