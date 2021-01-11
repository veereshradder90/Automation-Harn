package io.harness.rest.helper.cdngdx.k8sconnector;

import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;

public class K8sVariationHelper extends CoreUtils {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

    public JsonObject createK8SManualUserNamePasswordJsonKey(String specAuthType, String masterUrl, String username , String passref, String cacert){
        JsonObject spec = new JsonObject();
        spec.addProperty("type",specAuthType);

        JsonObject manualSpec = new JsonObject();
        manualSpec.addProperty("masterUrl",masterUrl);

        JsonObject auth = new JsonObject();
        auth.addProperty("type","UsernamePassword");

        JsonObject UsernamePasswordSpec = new JsonObject();
        UsernamePasswordSpec.addProperty("username",username);
        UsernamePasswordSpec.addProperty("passwordRef",passref);
        UsernamePasswordSpec.addProperty("cacert",cacert);

        auth.add("spec",UsernamePasswordSpec);
        manualSpec.add("auth",auth);
        spec.add("spec",manualSpec);

        return spec;
    }


    public JsonObject createK8SManualServiceTokenJsonKey(String configType, String specAuthType, String masterUrl, String serviceAcccountTokenRef){
        JsonObject spec = new JsonObject();
        spec.addProperty("type",specAuthType);

        JsonObject manualSpec = new JsonObject();
        manualSpec.addProperty("masterUrl",masterUrl);

        JsonObject auth = new JsonObject();
        auth.addProperty("type",configType);

        JsonObject UsernamePasswordSpec = new JsonObject();
        UsernamePasswordSpec.addProperty("serviceAccountTokenRef",serviceAcccountTokenRef);

        auth.add("spec",UsernamePasswordSpec);
        manualSpec.add("auth",auth);
        spec.add("spec",manualSpec);

        return spec;
    }

    public JsonObject createK8SODICJsonKey(String masterUrl, String specAuthType, String oidcIssuerUrl, String oidcClientIdRef, String oidcUsername, String oidcPasswordRef, String oidcSecretRef, String oidcScopes, String k8sType) {
        JsonObject spec = new JsonObject();
        spec.addProperty("type",specAuthType);

        JsonObject manualSpec = new JsonObject();
        manualSpec.addProperty("masterUrl",masterUrl);

        JsonObject auth = new JsonObject();
        auth.addProperty("type",k8sType);

        JsonObject openIdConnectSpec = new JsonObject();
        openIdConnectSpec.addProperty("oidcIssuerUrl",oidcIssuerUrl);
        openIdConnectSpec.addProperty("oidcClientIdRef",oidcClientIdRef);
        openIdConnectSpec.addProperty("oidcUsername",oidcUsername);
        openIdConnectSpec.addProperty("oidcPasswordRef",oidcPasswordRef);
        openIdConnectSpec.addProperty("oidcSecretRef",oidcSecretRef);
        openIdConnectSpec.addProperty("oidcScopes",oidcScopes);


        auth.add("spec",openIdConnectSpec);
        manualSpec.add("auth",auth);
        spec.add("spec",manualSpec);

        return spec;
    }

    public JsonObject createK8SClientJsonKey(String masterUrl, String specAuthType, String clientCertRef, String clientKeyRef, String clientKeyPassphraseRef, String clientKeyAlgo, String k8sType) {
        JsonObject spec = new JsonObject();
        spec.addProperty("type",specAuthType);

        JsonObject manualSpec = new JsonObject();
        manualSpec.addProperty("masterUrl",masterUrl);

        JsonObject auth = new JsonObject();
        auth.addProperty("type",k8sType);

        JsonObject openIdConnectSpec = new JsonObject();
        openIdConnectSpec.addProperty("clientCertRef",clientCertRef);
        openIdConnectSpec.addProperty("clientKeyRef",clientKeyRef);
        openIdConnectSpec.addProperty("clientKeyPassphraseRef",clientKeyPassphraseRef);
        openIdConnectSpec.addProperty("clientKeyAlgo",clientKeyAlgo);

        auth.add("spec",openIdConnectSpec);
        manualSpec.add("auth",auth);
        spec.add("spec",manualSpec);

        return spec;
    }

    public JsonObject createK8SDelegateJsonKey(String delegateName, String k8sType) {
        JsonObject spec = new JsonObject();
        spec.addProperty("type",k8sType);

        JsonObject specDelegate = new JsonObject();
        specDelegate.addProperty("inheritConfigFromDelegate",delegateName);

        spec.add("spec",specDelegate);

        return spec;
    }
}
