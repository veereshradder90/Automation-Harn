package io.harness.test.rest.cdngdx.orglevelconnectors;

import com.google.gson.JsonObject;
import io.harness.rest.helper.cdngdx.connectors.ConnectorsHelper;
import io.harness.rest.helper.cdngdx.k8sconnector.K8sVariationHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class OrgLevelKubernetesClientKey extends AbstractTest {
    K8sVariationHelper k8sVariationHelper = new K8sVariationHelper();
    ConnectorsHelper connectorsHelper = new ConnectorsHelper();

    @Test
    public void testCreateK8SClientKeyOrgLevelConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";

        String k8sType = "ClientKeyCert";
        String clientCertRef = "clientCertRef";
        String clientKeyRef = "clientKeyRef";
        String clientKeyPassphraseRef = "clientKeyPassphraseRef";
        String clientKeyAlgo = "clientKeyAlgo";

        JsonObject k8sVariation = k8sVariationHelper.createK8SClientJsonKey(masterUrl,
                configType,clientCertRef,clientKeyRef,clientKeyPassphraseRef,clientKeyAlgo,k8sType);
        System.out.println(k8sVariation.toString());

        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");

        JsonPath connectorResponse = connectorsHelper.createOrgK8SConnector(k8sVariation,name,identifier,orgId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));
    }

    @Test
    public void testEditK8SClientKeyOrgLevelConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";

        String k8sType = "ClientKeyCert";
        String clientCertRef = "clientCertRef";
        String clientKeyRef = "clientKeyRef";
        String clientKeyPassphraseRef = "clientKeyPassphraseRef";
        String clientKeyAlgo = "clientKeyAlgo";

        JsonObject k8sVariation = k8sVariationHelper.createK8SClientJsonKey(masterUrl,
                configType,clientCertRef,clientKeyRef,clientKeyPassphraseRef,clientKeyAlgo,k8sType);
        System.out.println(k8sVariation.toString());

        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");

        JsonPath connectorResponse = connectorsHelper.createOrgK8SConnector(k8sVariation,name,identifier,orgId,connectorType);

        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));


        //Update the connector
        JsonPath appResponse = connectorsHelper.updateOrgK8SConnector(k8sVariation,name+"Updated",identifier,connectorType,orgId);
        Assert.assertTrue(appResponse.getString("data.name").equals(name+"Updated"));
    }


    @Test
    public void testGetK8SClientKeyOrgLevelConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";

        String k8sType = "ClientKeyCert";
        String clientCertRef = "clientCertRef";
        String clientKeyRef = "clientKeyRef";
        String clientKeyPassphraseRef = "clientKeyPassphraseRef";
        String clientKeyAlgo = "clientKeyAlgo";
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";

        JsonObject k8sVariation = k8sVariationHelper.createK8SClientJsonKey(masterUrl,
                configType,clientCertRef,clientKeyRef,clientKeyPassphraseRef,clientKeyAlgo,k8sType);
        System.out.println(k8sVariation.toString());

        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");

        JsonPath connectorResponse = connectorsHelper.createOrgK8SConnector(k8sVariation,name,identifier,orgId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));

        JsonPath getConnectorResponse = connectorsHelper.getK8SOrgConnector(identifier,accountIdentifier,orgId);
        Assert.assertTrue(getConnectorResponse.getString("data.identifier").equals(identifier));
    }


    @Test
    public void testDeleteK8SClientKeyOrgLevelConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";

        String k8sType = "ClientKeyCert";
        String clientCertRef = "clientCertRef";
        String clientKeyRef = "clientKeyRef";
        String clientKeyPassphraseRef = "clientKeyPassphraseRef";
        String clientKeyAlgo = "clientKeyAlgo";
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";

        JsonObject k8sVariation = k8sVariationHelper.createK8SClientJsonKey(masterUrl,
                configType,clientCertRef,clientKeyRef,clientKeyPassphraseRef,clientKeyAlgo,k8sType);
        System.out.println(k8sVariation.toString());

        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");

        JsonPath connectorResponse = connectorsHelper.createOrgK8SConnector(k8sVariation,name,identifier,orgId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));

        JsonPath getConnectorResponse = connectorsHelper.deleteK8SOrgConnector(identifier,accountIdentifier,orgId);
        Assert.assertTrue(getConnectorResponse.getString("status").equals("SUCCESS"));

        JsonPath listResponse = connectorsHelper.listOrgK8SConnector(accountIdentifier,orgId);
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
    public void testListK8SClientKeyOrgLevelConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";

        String k8sType = "ClientKeyCert";
        String clientCertRef = "clientCertRef";
        String clientKeyRef = "clientKeyRef";
        String clientKeyPassphraseRef = "clientKeyPassphraseRef";
        String clientKeyAlgo = "clientKeyAlgo";
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";

        JsonObject k8sVariation = k8sVariationHelper.createK8SClientJsonKey(masterUrl,
                configType,clientCertRef,clientKeyRef,clientKeyPassphraseRef,clientKeyAlgo,k8sType);
        System.out.println(k8sVariation.toString());

        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String orgId = commonHelper.createRandomName("K8sManual-DevX-");

        JsonPath connectorResponse = connectorsHelper.createOrgK8SConnector(k8sVariation,name,identifier,orgId,connectorType);
        Assert.assertTrue(connectorResponse.getString("status").equals("SUCCESS"));
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));


        JsonPath listResponse = connectorsHelper.listOrgK8SConnector(accountIdentifier,orgId);
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
