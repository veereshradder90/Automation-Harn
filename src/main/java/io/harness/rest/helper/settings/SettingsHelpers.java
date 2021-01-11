package io.harness.rest.helper.settings;

/* Created by sonypriyadarshini
on 11/08/20 */

import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SettingsHelpers {
    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();

    public JsonPath getConnectorsByType(String appId, String connectorType) {
        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("currentAppId", appId);
        requestSpecification.queryParam("search[0][field]", "value.type");
        requestSpecification.queryParam("search[0][op]", "IN");
        requestSpecification.queryParam("search[0][value]", connectorType);
        Response response = genericRequestBuilder.getCall(requestSpecification, SettingsConstants.GET_SETTINGS);
        return response.jsonPath();
    }


}
