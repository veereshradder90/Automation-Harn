package io.harness.rest.helper.jira;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.connector.ConnectorNames;
import io.harness.rest.helper.settings.SettingsHelpers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class JiraHelper extends CoreUtils {

    GenericRequestBuilder genericRequestBuilder = new GenericRequestBuilder();
    SettingsHelpers settingsHelpers=new SettingsHelpers();

    public Response getJiraProjects(String appId, String jiraConnectorId) {
        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("appId", appId);
        requestSpecification.pathParam("jiraConnectorId", jiraConnectorId);
        Response response = genericRequestBuilder.getCall(requestSpecification, JiraConstants.GET_JIRA_PROJECTS);
        return response;
    }

    public String getJiraTestProjectKeyByName(String appId, String jiraConnectorId, String jiraProjectName) {
        JsonPath jiraProjects=getJiraProjects(appId,jiraConnectorId).jsonPath();
        String jiraProjectKey="";
        for(int i=0,size=jiraProjects.getList("resource").size();i<size;i++){
            if(jiraProjects.getString("resource["+i+"].name").contains(jiraProjectName))
                jiraProjectKey= jiraProjects.getString("resource["+i+"].key");
        }
        return jiraProjectKey;
    }

    public JsonPath getIssueTypes(String appId, String jiraProjectId) {
        RequestSpecification requestSpecification = GenericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.queryParam("appId", appId);
        requestSpecification.pathParam("project", jiraProjectId);
        Response response = genericRequestBuilder.getCall(requestSpecification, JiraConstants.GET_JIRA_ISSUETYPES);
        return response.jsonPath();
    }

    public String getJiraConnectorIdByName(String appId, String connectorName) {
        String jiraConnectorId="";
        JsonPath connectorsData=settingsHelpers.getConnectorsByType(appId, ConnectorNames.JIRA.toString());
        for(int i=0,size=connectorsData.getList("resource.response").size();i<size;i++){
            if(connectorsData.getString("resource.response["+i+"].name").equals(connectorName))
                jiraConnectorId= connectorsData.getString("resource.response["+i+"].uuid");
        }
        return jiraConnectorId;
    }

    public String getValidJiraConnector(String appId) {
        String jiraConnectorId="";
        JsonPath connectorsData=settingsHelpers.getConnectorsByType(appId, ConnectorNames.JIRA.toString());
        for(int i=0,size=connectorsData.getList("resource.response").size();i<size;i++){
            jiraConnectorId=connectorsData.getString("resource.response["+i+"].uuid");
            Response jiraProjects=getJiraProjects(appId,jiraConnectorId);
            if(jiraProjects.getStatusCode()==200){
                for(int j=0,projectSize=jiraProjects.jsonPath().getList("resource").size();j<projectSize;j++){
                    if(jiraProjects.jsonPath().getString("resource["+j+"].name").contains("Test"))
                        return  jiraConnectorId;
                }
            }
        }
        return jiraConnectorId;
    }


}
