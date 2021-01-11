package io.harness.test.rest.cdngdx.projectlevelconnectors;

import com.google.gson.JsonObject;
import io.harness.rest.helper.cdngdx.connectors.ConnectorsHelper;
import io.harness.rest.helper.cdngdx.k8sconnector.K8sVariationHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class ProjectLevelKubernetesServiceToken extends AbstractTest {

    K8sVariationHelper k8sVariationHelper = new K8sVariationHelper();
    ConnectorsHelper connectorsHelper = new ConnectorsHelper();

    @Test
    public void testCreateK8SManualServiceTokenProjectConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String specAuthType = "ServiceAccount";
        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");
        String projId = commonHelper.createRandomName("K8sManual-DevX-");

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualServiceTokenJsonKey(specAuthType,configType,masterUrl,serviceAccountTokenRef);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createProjectK8SConnector(k8sVariation,name,identifier,orgId,projId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));
    }




    @Test
    public void testUpdateK8SManualServiceTokenProjectConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String specAuthType = "ServiceAccount";
        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");
        String projId = commonHelper.createRandomName("K8sManual-DevX-");

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualServiceTokenJsonKey(specAuthType,configType,masterUrl,serviceAccountTokenRef);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createProjectK8SConnector(k8sVariation,name,identifier,orgId,projId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));

        //Update the connector
        JsonPath appResponse = connectorsHelper.updateProjectK8SConnector(k8sVariation,name+"Updated",identifier,connectorType,orgId,projId);
        Assert.assertTrue(appResponse.getString("data.name").equals(name+"Updated"));
    }

    @Test
    public void testGetK8SManualServiceTokenProjectConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String specAuthType = "ServiceAccount";
        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");
        String projId = commonHelper.createRandomName("K8sManual-DevX-");
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";
        JsonObject k8sVariation = k8sVariationHelper.createK8SManualServiceTokenJsonKey(specAuthType,configType,masterUrl,serviceAccountTokenRef);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createProjectK8SConnector(k8sVariation,name,identifier,orgId,projId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));

        JsonPath getConnectorResponse = connectorsHelper.getK8SOProjConnector(identifier,accountIdentifier,orgId,projId);
        Assert.assertTrue(getConnectorResponse.getString("data.identifier").equals(identifier));
    }

    @Test
    public void testDeleteK8SManualServiceTokenProjectConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String specAuthType = "ServiceAccount";
        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");
        String projId = commonHelper.createRandomName("K8sManual-DevX-");
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";
        JsonObject k8sVariation = k8sVariationHelper.createK8SManualServiceTokenJsonKey(specAuthType,configType,masterUrl,serviceAccountTokenRef);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createProjectK8SConnector(k8sVariation,name,identifier,orgId,projId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));

        JsonPath getConnectorResponse = connectorsHelper.deleteK8SProjConnector(identifier,accountIdentifier,orgId,projId);

        Assert.assertTrue(getConnectorResponse.getString("status").equals("SUCCESS"));

        JsonPath listResponse = connectorsHelper.listProjK8SConnector(accountIdentifier,orgId,projId);
        System.out.println(listResponse.toString());

        List<String> jsonResponse = listResponse.getList("data.content");
        System.out.println("Length is :"+jsonResponse.size());
        boolean isFound = false;
        for (int i = 0 ; i < jsonResponse.size();i++){
            if (listResponse.getString("data.content["+i+"].identifier").equals(identifier)) {
                System.out.println("Found here: "+identifier);
                isFound=!isFound;
            }
        }
        Assert.assertTrue(isFound);
    }

    @Test
    public void testListK8SManualServiceTokenProjectConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String specAuthType = "ServiceAccount";
        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");
        String projId = commonHelper.createRandomName("K8sManual-DevX-");
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";
        JsonObject k8sVariation = k8sVariationHelper.createK8SManualServiceTokenJsonKey(specAuthType,configType,masterUrl,serviceAccountTokenRef);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createProjectK8SConnector(k8sVariation,name,identifier,orgId,projId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));

        JsonPath listResponse = connectorsHelper.listProjK8SConnector(accountIdentifier,orgId,projId);
        System.out.println(listResponse.toString());

        List<String> jsonResponse = listResponse.getList("data.content");
        System.out.println("Length is :"+jsonResponse.size());
        boolean isFound = false;
        for (int i = 0 ; i < jsonResponse.size();i++){
            if (listResponse.getString("data.content["+i+"].identifier").equals(identifier)) {
                System.out.println("Found here: "+identifier);
                isFound=!isFound;
            }
        }
        Assert.assertTrue(isFound);

    }


}