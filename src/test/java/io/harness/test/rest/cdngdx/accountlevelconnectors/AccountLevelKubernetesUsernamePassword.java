package io.harness.test.rest.cdngdx.accountlevelconnectors;

import com.google.gson.JsonObject;
import io.harness.rest.helper.cdngdx.connectors.ConnectorsHelper;
import io.harness.rest.helper.cdngdx.k8sconnector.K8sVariationHelper;
import io.harness.rest.helper.cdngdx.k8sconnector.KubernetesManualDataProvider;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class AccountLevelKubernetesUsernamePassword extends AbstractTest {
    K8sVariationHelper k8sVariationHelper = new K8sVariationHelper();
    ConnectorsHelper connectorsHelper = new ConnectorsHelper();

    @Test
    public void testCreateK8SUsernamePassConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));
    }

    //Validation for name
    @Test (dataProvider = "name-data-provider", dataProviderClass = KubernetesManualDataProvider.class)
    public void testCreateAccountK8SUsernamePassConnectorAcceptableNameValidation(String name) throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));
    }

    @Test (dataProvider = "name-data-provider", dataProviderClass = KubernetesManualDataProvider.class)
    public void testCreateAccountK8SUsernamePassConnectorNonAcceptableNameValidation(String name) throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));
    }

//
    //Validation for identifier
    @Test (dataProvider = "identifier-data-provider", dataProviderClass = KubernetesManualDataProvider.class)
    public void testCreateAccountK8SUsernamePassConnectorAcceptableIdentifier(String identifier) throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));
    }
//
    //Validation for identifier
    @Test (dataProvider = "identifier-data-provider", dataProviderClass = KubernetesManualDataProvider.class)
    public void testCreateAccountK8SUsernamePassConnectorNonAcceptableIdentifier(String identifier) throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);
        Assert.assertTrue(!connectorResponse.getString("data.accountIdentifier").isEmpty());
        Assert.assertTrue(connectorResponse.getString("data.identifier").equals(identifier));
    }


    @Test
    public void testEditAccountK8SUsernamePassConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);

        //Update the connector
        JsonPath appResponse = connectorsHelper.updateAccountK8SConnector(k8sVariation,name+"Updated",identifier,connectorType);
         Assert.assertTrue(appResponse.getString("data.name").equals(name+"Updated"));
    }

    @Test
    public void testGetK8SUsernamePassConnectorAPI() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);

        JsonPath getConnectorResponse = connectorsHelper.getK8SConnector(identifier,accountIdentifier);
        Assert.assertTrue(getConnectorResponse.getString("data.identifier").equals(identifier));
    }

    @Test
    public void testDeleteK8SUsernamePassAccountConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);


        JsonPath getConnectorResponse = connectorsHelper.deleteK8SConnector(identifier,accountIdentifier);
        Assert.assertTrue(getConnectorResponse.getString("status").equals("SUCCESS"));

        JsonPath listResponse = connectorsHelper.listAccountK8SConnector(accountIdentifier);
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
    public void testListK8SUsernamePassAccountConnector() throws IOException {
        String configType = "ManualConfig";
        String connectorType = "K8sCluster";
        String masterUrl = "https://34.67.13.218";
        String name = commonHelper.createRandomName("K8sManual-DevX-");
        String identifier = commonHelper.createRandomName("K8sManual-DevX-");
        String admin = "admin";
        String passref = "6nmjPJDYQiiNrvG0Am63BA";
        String cacert = "Random";
        String accountIdentifier="zEaak-FLS425IEO7OLzMUg";

        JsonObject k8sVariation = k8sVariationHelper.createK8SManualUserNamePasswordJsonKey(configType,masterUrl,admin,passref,cacert);
        System.out.println(k8sVariation.toString());

        JsonPath connectorResponse = connectorsHelper.createAccountK8SConnector(k8sVariation,name,identifier,connectorType);


        JsonPath listResponse = connectorsHelper.listAccountK8SConnector(accountIdentifier);
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
