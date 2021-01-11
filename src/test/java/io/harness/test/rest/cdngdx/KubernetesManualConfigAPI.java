package io.harness.test.rest.cdngdx;

import io.harness.rest.helper.cdngdx.k8sconnector.KubernetesManualDataProvider;
import io.harness.rest.helper.cdngdx.k8sconnector.KubernetesManualHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class KubernetesManualConfigAPI extends AbstractTest {
    KubernetesManualHelper kubernetesManualConfigAPI = new KubernetesManualHelper();
//
//    //Validation for name
//    @Test (dataProvider = "name-data-provider", dataProviderClass = KubernetesManualDataProvider.class)
//    public void testCreateK8SManualConnectorAcceptableNameValidation(String name) throws IOException {
//        String configType = "ManualConfig";
//        String connectorType = "K8sCluster";
//        String masterUrl = "https://34.67.13.218";
//        String specAuthType = "ServiceAccount";
//        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
//        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
//
//        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);
//        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
//        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));
//    }
//
//    @Test (dataProvider = "name-data-provider", dataProviderClass = KubernetesManualDataProvider.class)
//    public void testCreateK8SManualConnectorNonAcceptableNameValidation(String name) throws IOException {
//        String configType = "ManualConfig";
//        String connectorType = "K8sCluster";
//        String masterUrl = "https://34.67.13.218";
//        String specAuthType = "ServiceAccount";
//        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
//        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
//        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);
//        Assert.assertTrue(connectorResponse.getString("name").isEmpty());
//        Assert.assertTrue(connectorResponse.getString("name").equals(identifier));
//    }
//
//    //Validation for identifier
//    @Test (dataProvider = "identifier-data-provider", dataProviderClass = KubernetesManualDataProvider.class)
//    public void testCreateK8SManualConnectorAcceptableIdentifier(String identifier) throws IOException {
//        String accountIdentifier="accountIdentifier";
//        String configType = "ManualConfig";
//        String connectorType = "K8sCluster";
//        String masterUrl = "https://34.67.13.218";
//        String specAuthType = "ServiceAccount";
//        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
//        String name = commonHelper.createRandomName("K8sManual-DevX-");
//        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);
//        Assert.assertTrue(!connectorResponse.getString("accountIdentifier").isEmpty());
//        Assert.assertTrue(connectorResponse.getString("identifier").equals(identifier));
//        kubernetesManualConfigAPI.deleteK8SConnector(identifier,accountIdentifier);
//    }
//
//    //Validation for identifier
//    @Test (dataProvider = "identifier-data-provider", dataProviderClass = KubernetesManualDataProvider.class)
//    public void testCreateK8SManualConnectorNonAcceptableIdentifier(String identifier) throws IOException {
//        String accountIdentifier="accountIdentifier";
//        String configType = "ManualConfig";
//        String connectorType = "K8sCluster";
//        String masterUrl = "https://34.67.13.218";
//        String specAuthType = "ServiceAccount";
//        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
//        String name = commonHelper.createRandomName("K8sManual-DevX-");
//        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);
//        Assert.assertTrue(!connectorResponse.getString("accountIdentifier").isEmpty());
//        Assert.assertTrue(connectorResponse.getString("identifier").equals(identifier));
//        kubernetesManualConfigAPI.deleteK8SConnector(identifier,accountIdentifier);
//    }
//
//    @Test
//    public void testCreateK8SManualConnector() throws IOException {
//        String configType = "ManualConfig";
//        String connectorType = "K8sCluster";
//        String masterUrl = "https://34.67.13.218";
//        String specAuthType = "ServiceAccount";
//        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
//        String name = commonHelper.createRandomName("K8sManual-DevX-");
//        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
//
//        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);
//        Assert.assertTrue(!connectorResponse.getString("accountIdentifier").isEmpty());
//        Assert.assertTrue(connectorResponse.getString("identifier").equals(identifier));
//    }

//    @Test
//    public void testEditK8SManualConnector() throws IOException {
//        String configType = "ManualConfig";
//        String connectorType = "K8sCluster";
//        String masterUrl = "https://34.67.13.218";
//        String specAuthType = "ServiceAccount";
//        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
//        String name = commonHelper.createRandomName("K8sManual-DevX-");
//        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
//        //This created a connector
//        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);
//
//        //Update the connector
//        JsonPath appResponse = kubernetesManualConfigAPI.updateK8SManualConnector(name+"Updated",identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);
//        Assert.assertTrue(appResponse.getString("name").equals(name+"Updated"));
//    }

    @Test
    public void testK8SConnectorList() throws FileNotFoundException {
        String accountIdentifier="accountIdentifier";
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String specAuthType = "ServiceAccount";
        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        //This created a connector
        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);

        JsonPath listResponse = kubernetesManualConfigAPI.listK8SConnector(accountIdentifier);
        System.out.println(listResponse.toString());

        List<String> jsonResponse = listResponse.getList("content");
        System.out.println("Length is :"+jsonResponse.size());
        boolean isFound = false;
        for (int i = 0 ; i < jsonResponse.size();i++){
            if (listResponse.getString("content["+i+"].identifier").equals(identifier)) {
                System.out.println("Found here: "+identifier);
                isFound=!isFound;
            }
        }

        Assert.assertTrue(isFound);
    }

//    @Test
//    public void testGetK8SConnectorAPI() throws FileNotFoundException {
//        String accountIdentifier="accountIdentifier";
//        String configType = "ManualConfig";
//        String connectorType = "K8sCluster";
//        String masterUrl = "https://34.67.13.218";
//        String specAuthType = "ServiceAccount";
//        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
//        String name = commonHelper.createRandomName("K8sManual-DevX-");
//        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
//        //This created a connector
//        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);
//
//        JsonPath getConnectorResponse = kubernetesManualConfigAPI.getK8SConnector(identifier,accountIdentifier);
//        Assert.assertTrue(getConnectorResponse.getString("identifier").equals(identifier));
//    }

    @Test
    public void testDeleteGetK8SConnectorAPI() throws FileNotFoundException {
        String accountIdentifier="accountIdentifier";
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String specAuthType = "ServiceAccount";
        String serviceAccountTokenRef = "6FbgTRx8RDqE2DAJzY3nwg";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        //This created a connector
        JsonPath connectorResponse = kubernetesManualConfigAPI.createK8SManualConnector(name,identifier,connectorType,configType,masterUrl,specAuthType,serviceAccountTokenRef);

        JsonPath getConnectorResponse = kubernetesManualConfigAPI.deleteK8SConnector(identifier,accountIdentifier);
    }
}
