package io.harness.rest.helper.cloudproviders;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class InfraDefs extends CoreUtils {


    public static  boolean  getListOfAvailableCPInsideInfra(String azureId, String appId, String envId) {
        //Get list of available infra
        GenericRequestBuilder genericRequestBuilder =new GenericRequestBuilder();
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.param("currentEnvId",envId);
        requestSpecification.param("currentAppId",appId);
        requestSpecification.param("search[0][field]","category");
        requestSpecification.param("search[0][op]","IN");
        requestSpecification.param("search[0][value]","CLOUD_PROVIDER");

        Response infraDefGet = genericRequestBuilder.getCall(requestSpecification,"/settings?accountId="+defaultAccountId);
        ;
//        Response infraDefGet = genericRequestBuilder.getWithParams(queryParams, "/settings?accountId="+defaultAccountId);
        List<String> jsonResponse = infraDefGet.jsonPath().getList("resource.response");
        System.out.println("Length is :"+jsonResponse.size());

        for (int i = 0 ; i < jsonResponse.size();i++){
            if (infraDefGet.jsonPath().getString("resource.response["+i+"].uuid").equals(azureId)) {
                System.out.println("Found here: "+azureId);
                return true;
            }
        }
        return false;
    }


    public static  boolean  getListOfAvailableCPInsideInfraRbac(String azureId, String appId, String envId) {
        //Get list of available infra
        GenericRequestBuilder genericRequestBuilder =new GenericRequestBuilder();
        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject("dx-cp-rbac-user@mailinator.com","Harness@123!");
        requestSpecification.param("currentEnvId",envId);
        requestSpecification.param("currentAppId",appId);
        requestSpecification.param("search[0][field]","category");
        requestSpecification.param("search[0][op]","IN");
        requestSpecification.param("search[0][value]","CLOUD_PROVIDER");

        Response infraDefGet = genericRequestBuilder.getCall(requestSpecification,"/settings?accountId="+defaultAccountId);
        ;
//        Response infraDefGet = genericRequestBuilder.getWithParams(queryParams, "/settings?accountId="+defaultAccountId);
        List<String> jsonResponse = infraDefGet.jsonPath().getList("resource.response");
        System.out.println("Length is :"+jsonResponse.size());

        for (int i = 0 ; i < jsonResponse.size();i++){
            if (infraDefGet.jsonPath().getString("resource.response["+i+"].uuid").equals(azureId)) {
                System.out.println("Found here: "+azureId);
                return true;
            }
        }
        return false;
    }
}
