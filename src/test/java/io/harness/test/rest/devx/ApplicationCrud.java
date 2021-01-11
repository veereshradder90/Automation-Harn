package io.harness.test.rest.devx;

import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.dxapplicationsgraphql.GraphQLApplicationHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApplicationCrud extends AbstractTest {
    GraphQLApplicationHelper graphQLApplicationHelper = new GraphQLApplicationHelper();
    ApplicationHelper applicationHelper = new ApplicationHelper();
    @AfterTest
    public void deleteAppsCreatedInTest() {
        applicationHelper.searchAndDeleteApp("Application-DevX-");
    }

    @Test
    public void testApplicationCreation() throws IOException {
        String appName = commonHelper.createRandomName("Application-DevX-");
        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());
        Assert.assertTrue(appResponse.getString("data.createApplication.application.name").equalsIgnoreCase(appName));
    }

    @Test
    public void testApplicationUpdateFlow() throws IOException {
        String appName = commonHelper.createRandomName("Application-DevX-");
        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());
        String appId = appResponse.getString("data.createApplication.application.id");

        JsonPath appUpdatedResponse = graphQLApplicationHelper.updateApplication(appName+"-Updated",appId);
        Assert.assertTrue(!appUpdatedResponse.getString("data.updateApplication.application.id").isEmpty());
        Assert.assertTrue(appUpdatedResponse.getString("data.updateApplication.application.name").equalsIgnoreCase(appName+"-Updated"));
    }

    @Test
    public void testApplicationDeleteFlow() throws IOException{
        String appName = commonHelper.createRandomName("Application-DevX-");
        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());
        String appId = appResponse.getString("data.createApplication.application.id");

        JsonPath appDeletedResponse = graphQLApplicationHelper.deleteApplication(appName+"-Updated",appId);
        Assert.assertTrue(!appDeletedResponse.getString("data.deleteApplication.clientMutationId").isEmpty());
    }

    @Test
    public void testApplicationByName() throws IOException{
        String appName = commonHelper.createRandomName("Application-DevX-");

        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());

        JsonPath appGetByNameResponse = graphQLApplicationHelper.getApplicationByName(appName);
        Assert.assertTrue(!appGetByNameResponse.getString("data.applicationByName.id").isEmpty());
        Assert.assertTrue(!appGetByNameResponse.getString("data.applicationByName.name").isEmpty());

        String appIdFromResponse = appGetByNameResponse.getString("data.applicationByName.id");

        JsonPath appDeletedResponse = graphQLApplicationHelper.deleteApplication(appName+"-Updated",appIdFromResponse);
        Assert.assertTrue(!appDeletedResponse.getString("data.deleteApplication.clientMutationId").isEmpty());
    }

    @Test
    public void testApplicationListApi() throws  IOException{
        JsonPath appResponse = graphQLApplicationHelper.listApplications();
        Assert.assertTrue(!appResponse.getString("data.applications.nodes[0].name").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.applications.nodes[0].id").isEmpty());
    }

    @Test
    public void testApplicationById() throws IOException{
        String appName = commonHelper.createRandomName("Application-DevX-");
        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());
        String appId = appResponse.getString("data.createApplication.application.id");
        JsonPath appGetByNameResponse = graphQLApplicationHelper.getApplicationById(appId);
        Assert.assertTrue(!appGetByNameResponse.getString("data.application.id").isEmpty());
        Assert.assertTrue(!appGetByNameResponse.getString("data.application.name").isEmpty());
    }


}
