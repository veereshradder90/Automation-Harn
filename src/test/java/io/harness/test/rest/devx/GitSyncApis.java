package io.harness.test.rest.devx;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.helper.dxapplicationsgraphql.GraphQLApplicationHelper;
import io.harness.rest.helper.gitsync.GitSyncHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class GitSyncApis extends AbstractTest {
    GitSyncHelper gitSyncHelper = new GitSyncHelper();
    GraphQLApplicationHelper graphQLApplicationHelper = new GraphQLApplicationHelper();

    String connectorId = "dPiu0WYOQSelSfTZ34mWCw";
    String branchName = commonHelper.createRandomName("DevX-Git-Sync", 3);

    @Test
    public void testAddGitSyncToApplication() throws IOException {
        boolean syncEnabled = false;

        String appName = commonHelper.createRandomName("Application-DevX-");
        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());
        String appId = appResponse.getString("data.createApplication.application.id");
        System.out.println("appName: "+appResponse.getString("data.createApplication.application.name"));

        JsonPath updateGitConfig = gitSyncHelper.addGitSyncToApplication(appId,connectorId,branchName,syncEnabled);
        Assert.assertTrue(updateGitConfig.getString("data.updateApplicationGitSyncConfig.gitSyncConfig.gitConnector.id").equalsIgnoreCase(connectorId));
        Assert.assertTrue(updateGitConfig.getString("data.updateApplicationGitSyncConfig.gitSyncConfig.syncEnabled").equalsIgnoreCase(String.valueOf(syncEnabled)));
        //Verify the step
    }

    @Test
    public void testRemoveGitSyncFromApplication() throws IOException {
        boolean syncEnabled = false;
        String appName = commonHelper.createRandomName("Application-DevX-");
        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());
        String appId = appResponse.getString("data.createApplication.application.id");
        System.out.println("appName: "+appResponse.getString("data.createApplication.application.name"));

        JsonPath updateGitConfig = gitSyncHelper.addGitSyncToApplication(appId,connectorId,branchName,syncEnabled);
        Assert.assertTrue(updateGitConfig.getString("data.updateApplicationGitSyncConfig.gitSyncConfig.gitConnector.id").equalsIgnoreCase(connectorId));

        //Remove the application git sync
        JsonPath removeGitConfig = gitSyncHelper.removeGitSyncFromAnApplication(appId);
        Assert.assertTrue(removeGitConfig.getString("data.removeApplicationGitSyncConfig.application.id").equalsIgnoreCase(appId));
    }

    @Test
    public void testUpdateGitSyncStatusOfAnApplicationApplication() throws IOException {
        boolean syncEnabled = false;
        String appName = commonHelper.createRandomName("Application-DevX-");
        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());
        String appId = appResponse.getString("data.createApplication.application.id");
        System.out.println("appName: "+appResponse.getString("data.createApplication.application.name"));

        JsonPath updateGitConfig = gitSyncHelper.addGitSyncToApplication(appId,connectorId,branchName,syncEnabled);
        Assert.assertTrue(updateGitConfig.getString("data.updateApplicationGitSyncConfig.gitSyncConfig.gitConnector.id").equalsIgnoreCase(connectorId));

        //Remove the application git sync
        JsonPath removeGitConfig = gitSyncHelper.enableOrDisableGitSync(appId,!syncEnabled);
        Assert.assertTrue(removeGitConfig.getString("data.updateApplicationGitSyncConfigStatus.gitSyncConfig.gitConnector.id").equalsIgnoreCase(connectorId));
        Assert.assertTrue(removeGitConfig.getString("data.updateApplicationGitSyncConfigStatus.gitSyncConfig.syncEnabled").equalsIgnoreCase(String.valueOf(!syncEnabled)));
    }


    @Test
    public void testGitSyncDetailsOfSpecificApplication() throws IOException {
        boolean syncEnabled = false;
        String appName = commonHelper.createRandomName("Application-DevX-");
        JsonPath appResponse = graphQLApplicationHelper.createApplication(appName);
        Assert.assertTrue(!appResponse.getString("data.createApplication.application.id").isEmpty());
        String appId = appResponse.getString("data.createApplication.application.id");
        System.out.println("appName: "+appResponse.getString("data.createApplication.application.name"));

        JsonPath gitSyncDetails = gitSyncHelper.detailsOfGitSync(appId);
        Assert.assertTrue(gitSyncDetails.getString("data.application.gitSyncConfig")==null);

        JsonPath updateGitConfig = gitSyncHelper.addGitSyncToApplication(appId,connectorId,branchName,syncEnabled);
        Assert.assertTrue(updateGitConfig.getString("data.updateApplicationGitSyncConfig.gitSyncConfig.gitConnector.id").equalsIgnoreCase(connectorId));

        JsonPath gitSyncDetailsUpdated = gitSyncHelper.detailsOfGitSync(appId);
        Assert.assertTrue(gitSyncDetailsUpdated.getString("data.application.gitSyncConfig")!=null);

        Assert.assertTrue(gitSyncDetailsUpdated.getString("data.application.gitSyncConfig.branch").equalsIgnoreCase(branchName));
        Assert.assertTrue(gitSyncDetailsUpdated.getString("data.application.gitSyncConfig.gitConnector.id").equalsIgnoreCase(connectorId));
        Assert.assertTrue(gitSyncDetailsUpdated.getString("data.application.gitSyncConfig.syncEnabled").equalsIgnoreCase(String.valueOf(syncEnabled)));

        //Remove the application git sync
        JsonPath removeGitConfig = gitSyncHelper.enableOrDisableGitSync(appId,!syncEnabled);
        Assert.assertTrue(removeGitConfig.getString("data.updateApplicationGitSyncConfigStatus.gitSyncConfig.gitConnector.id").equalsIgnoreCase(connectorId));
        Assert.assertTrue(removeGitConfig.getString("data.updateApplicationGitSyncConfigStatus.gitSyncConfig.syncEnabled").equalsIgnoreCase(String.valueOf(!syncEnabled)));


        JsonPath gitSyncDetailsUpdatedAgain = gitSyncHelper.detailsOfGitSync(appId);
        Assert.assertTrue(gitSyncDetailsUpdatedAgain.getString("data.application.gitSyncConfig")!=null);
        Assert.assertTrue(gitSyncDetailsUpdatedAgain.getString("data.application.gitSyncConfig.branch").equalsIgnoreCase(branchName));
        Assert.assertTrue(gitSyncDetailsUpdatedAgain.getString("data.application.gitSyncConfig.gitConnector.id").equalsIgnoreCase(connectorId));
        Assert.assertTrue(gitSyncDetailsUpdatedAgain.getString("data.application.gitSyncConfig.syncEnabled").equalsIgnoreCase(String.valueOf(!syncEnabled)));
    }

    @Test
    public void testListGitConnectorsApi() throws IOException {
        JsonPath listConnectors = gitSyncHelper.listGitConnectors();
        Assert.assertTrue(!listConnectors.getString("data.connectors.nodes[0].name").isEmpty());
        Assert.assertTrue(!listConnectors.getString("data.connectors.nodes[0].id").isEmpty());
    }



}
