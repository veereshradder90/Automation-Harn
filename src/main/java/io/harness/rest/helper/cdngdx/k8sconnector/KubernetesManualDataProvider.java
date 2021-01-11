package io.harness.rest.helper.cdngdx.k8sconnector;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class KubernetesManualDataProvider {

    @DataProvider(name = "name-data-provider")
    public Object[][] nameProvider(Method m){
        switch (m.getName()) {
            case "testCreateK8SManualConnectorNonAcceptableNameValidation":
                return new Object[][]{{""},{" "}};
            case "testCreateK8SManualConnectorAcceptableNameValidation":
                return new Object[][]{{" Prabhat "}, {" Prabhat Pandey "}, {"%%%"}};
        }
        return null;
    }

    @DataProvider(name ="identifier-data-provider")
    public Object[][] identifierProvider(Method m){
        switch (m.getName()) {
            case "testCreateK8SManualConnectorNonAcceptableIdentifier":
                return new Object[][]{{""},{" "}};
            case "testCreateK8SManualConnectorAcceptableIdentifier":
                return new Object[][]{{" Prabhat "}, {" Prabhat Pandey "}, {"%%%"}};
        }
        return null;
    }

    @DataProvider(name ="tags-data-provider")
    public Object[][] tagsProvider(Method m){
//        switch (m.getName()) {
//            case "testCreateK8SManualConnectorNonAcceptableIdentifier":
//                return new Object[][]{{""}};
//            case "testCreateK8SManualConnectorAcceptableIdentifier":
//                return new Object[][]{{{"FirstTag,"Second",Tag,ThirdTag"}}};
//        }
        return null;
    }
}
