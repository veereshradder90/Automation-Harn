package io.harness.test.rest.devx;

import com.google.gson.JsonObject;
import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.cloudproviders.physicaldata.PhysicalDataCPHelper;
import io.harness.rest.helper.devxusergroups.UserGroupsHelper;
import io.harness.rest.helper.environments.EnvironmentsHelper;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static io.harness.rest.helper.cloudproviders.InfraDefs.getListOfAvailableCPInsideInfra;
import static io.harness.rest.helper.cloudproviders.InfraDefs.getListOfAvailableCPInsideInfraRbac;

public class PhysicalDataCentreCPTest extends AbstractTest {
    PhysicalDataCPHelper physicalDataCPHelper = new PhysicalDataCPHelper();
    SecretsGraphQLHelper secretsGraphQLHelper= new SecretsGraphQLHelper();
    ApplicationHelper applicationHelper = new ApplicationHelper();
    EnvironmentsHelper environmentsHelper = new EnvironmentsHelper();

    String envName = commonHelper.createRandomName("DevX-Automation-Env", 3);
    String rbacUserGroupName = "CP-UserGroup-RBAC";

    @AfterTest
    public void deleteAppsCreatedInTest() {
        applicationHelper.searchAndDeleteApp("DevX-Automation-");
    }

    @Test
    public void createPDCCP() throws IOException {
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath appResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createCloudProvider.cloudProvider.name").equalsIgnoreCase(pdcCPName));
    }

    @Test
    public void editPDCCP()  throws IOException{
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath appResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_ALL","");
        String pdcId = appResponse.getString("data.createCloudProvider.cloudProvider.id");
        JsonPath updatedAppResponse=physicalDataCPHelper.editPDCCP(pdcId,pdcCPName+"-Updated");
        Assert.assertTrue(updatedAppResponse.getString("data.updateCloudProvider.cloudProvider.name").equalsIgnoreCase(pdcCPName+"-Updated"));
    }

    @Test
    public void testDeletePDCCp() throws IOException{
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath appResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_ALL","");
        String pdcId = appResponse.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath updatedAppResponse=physicalDataCPHelper.deletePDCCp(pdcId);
        Assert.assertTrue(!updatedAppResponse.getString("data.deleteCloudProvider.clientMutationId").isEmpty());
    }

    @Test
    public void testIfPDCCpByNameIsWorking() throws IOException{
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath appResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_ALL","");
        String cpName = appResponse.getString("data.createCloudProvider.cloudProvider.name");

        JsonPath cpByNameResponse = physicalDataCPHelper.getCPByNameTest(cpName);
        Assert.assertTrue(cpByNameResponse.getString("data.cloudProviderByName.name").equalsIgnoreCase(cpName));
    }

    @Test
    public void testIfPDCCpByIdIsWorking() throws IOException{
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath appResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_ALL","");
        String cpId = appResponse.getString("data.createCloudProvider.cloudProvider.id");

        PhysicalDataCPHelper physicalDataCPHelper = new PhysicalDataCPHelper();
        JsonPath cpByIdResponse = physicalDataCPHelper.getCPByIdTest(cpId);
        Assert.assertTrue(cpByIdResponse.getString("data.cloudProvider.id").equalsIgnoreCase(cpId));
        Assert.assertTrue(!cpByIdResponse.getString("data.cloudProvider.name").isEmpty());
    }

    @Test
    public void testCloudProvidersList() throws  IOException{
        JsonPath appResponse = physicalDataCPHelper.listCloudProviders();
        Assert.assertTrue(!appResponse.getString("data.cloudProviders.nodes[0].name").isEmpty());
        Assert.assertTrue(!appResponse.getString("data.cloudProviders.nodes[0].id").isEmpty());
    }

    @Test
    public void testRbacProdAll() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 3);
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath pcfResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_ALL","");
        String pdcId = pcfResponse.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

//        Positive flow
        JsonPath envResponse = environmentsHelper.createEnvironment(envName, "sample env description", "PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env

        boolean isFound = getListOfAvailableCPInsideInfra(pdcId, appId, envId);
        Assert.assertTrue(isFound);

        //Negative flow [Create another env where it should not be visible.]
        JsonPath envResponseNonProd = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "NON_PROD", appId);
        String envIdNonProd = envResponseNonProd.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresent = getListOfAvailableCPInsideInfra(pdcId, appId, envIdNonProd);
        System.out.println("Should not be present :"+shouldNotBePresent);
        Assert.assertFalse(shouldNotBePresent);
    }


    @Test
    public void testRbacNonProdAll() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 3);
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath pcfResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"NON_PROD_ALL","");
        String pdcId = pcfResponse.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

//        Positive flow
        JsonPath envResponse = environmentsHelper.createEnvironment(envName, "sample env description", "NON_PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env

        boolean isFound = getListOfAvailableCPInsideInfra(pdcId, appId, envId);
        Assert.assertTrue(isFound);

        //Negative flow [Create another env where it should not be visible.]
        JsonPath envResponseNonProd = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "PROD", appId);
        String envIdNonProd = envResponseNonProd.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresent = getListOfAvailableCPInsideInfra(pdcId, appId, envIdNonProd);
        System.out.println("Should not be present :"+shouldNotBePresent);
        Assert.assertFalse(shouldNotBePresent);
    }



    @Test
    public void testRbacCurrentUserOneAppProdCheck() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 3);
        //Create All Apps Prod Secret
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");


        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath pcfResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_SINGLE_APP",appId);
        String pdcId = pcfResponse.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath envResponse = environmentsHelper.createEnvironment(envName, "sample env description", "PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env

        boolean isFound = getListOfAvailableCPInsideInfra(pdcId, appId, envId);
        Assert.assertTrue(isFound);

//        //Negative flow [Create another env where it should not be visible.]
        JsonPath envResponseNonProd = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "NON_PROD", appId);
        String envIdNonProd = envResponseNonProd.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresent = getListOfAvailableCPInsideInfra(pdcId, appId, envIdNonProd);
        System.out.println("Should not be present :"+shouldNotBePresent);
        Assert.assertFalse(shouldNotBePresent);


        //Create another app , It shouldn't be visible there
        JsonPath appResponseNew = applicationHelper.createApplication(appName+"New");
        Assert.assertTrue(appResponseNew.getString("resource.name").equalsIgnoreCase(appName+"New"));
        String appIdNew = appResponseNew.getString("resource.appId");

        JsonPath envResponseNew = environmentsHelper.createEnvironment(envName, "sample env description", "PROD", appIdNew);
        String envIdNew = envResponseNew.getString("resource.uuid");//Create App && Create Env

        boolean isFoundNew = getListOfAvailableCPInsideInfra(pdcId, appIdNew, envIdNew);
        Assert.assertFalse(isFoundNew);

        JsonPath envResponseNonProdNew = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "NON_PROD", appId);
        String envIdNonProdNew = envResponseNonProdNew.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresentNew = getListOfAvailableCPInsideInfra(pdcId, appIdNew, envIdNonProdNew);
        System.out.println("Should not be present :"+shouldNotBePresentNew);
        Assert.assertFalse(shouldNotBePresentNew);
    }

    @Test
    public void testRbacCurrentUserOneAppsNonProdCheck() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 3);
        //Create All Apps Prod Secret
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");


        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath pcfResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"NON_PROD_SINGLE_APP",appId);
        String pdcId = pcfResponse.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath envResponse = environmentsHelper.createEnvironment(envName, "sample env description", "NON_PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env

        boolean isFound = getListOfAvailableCPInsideInfra(pdcId, appId, envId);
        Assert.assertTrue(isFound);

//        //Negative flow [Create another env where it should not be visible.]
        JsonPath envResponseNonProd = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "PROD", appId);
        String envIdNonProd = envResponseNonProd.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresent = getListOfAvailableCPInsideInfra(pdcId, appId, envIdNonProd);
        System.out.println("Should not be present :"+shouldNotBePresent);
        Assert.assertFalse(shouldNotBePresent);


        //Create another app , It shouldn't be visible there
        JsonPath appResponseNew = applicationHelper.createApplication(appName+"New");
        Assert.assertTrue(appResponseNew.getString("resource.name").equalsIgnoreCase(appName+"New"));
        String appIdNew = appResponseNew.getString("resource.appId");

        JsonPath envResponseNew = environmentsHelper.createEnvironment(envName, "sample env description", "PROD", appIdNew);
        String envIdNew = envResponseNew.getString("resource.uuid");//Create App && Create Env

        boolean isFoundNew = getListOfAvailableCPInsideInfra(pdcId, appIdNew, envIdNew);
        Assert.assertFalse(isFoundNew);

        JsonPath envResponseNonProdNew = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "NON_PROD", appId);
        String envIdNonProdNew = envResponseNonProdNew.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresentNew = getListOfAvailableCPInsideInfra(pdcId, appIdNew, envIdNonProdNew);
        System.out.println("Should not be present :"+shouldNotBePresentNew);
        Assert.assertFalse(shouldNotBePresentNew);
    }

    @Test
    public void testCreateProdAllCPAndInviteAnotherProdAllUserToCheckIfHeCanAccessIt() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 3);
        //Create All Apps Prod Secret
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath pcfResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_ALL","");
        String pdcId = pcfResponse.getString("data.createCloudProvider.cloudProvider.id");

        //Create a usergroup && add a user
        UserGroupsHelper userGroupsHelper = new UserGroupsHelper();
        String rbacUserId = "juG75UHCQxu_MSq_OBxOVw";
        System.out.println("Creating an Rbac user group");
        JsonPath rbacResponse = userGroupsHelper.createUserGroup(rbacUserGroupName,rbacUserId,"PROD_ALL","");

        System.out.println("Creating an Rbac app ");
        JsonPath appResponseRbac = applicationHelper.createApplicationRbac(appName+"Rbac");
        Assert.assertTrue(appResponseRbac.getString("resource.name").equalsIgnoreCase(appName+"Rbac"));
        String appIdRbac = appResponseRbac.getString("resource.appId");

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponse = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "PROD", appIdRbac);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envId);

        boolean isFound = getListOfAvailableCPInsideInfraRbac(pdcId, appIdRbac, envId);
        Assert.assertTrue(isFound);

//        //Negative flow [Create another env where it should not be visible.]
         /*
        Create a usergroup with the different permission than CP
         */

        System.out.println("Creating an Rbac user group");
        JsonPath rbacResponseNegative = userGroupsHelper.createUserGroup(rbacUserGroupName,rbacUserId,"NON_PROD_ALL","");

        System.out.println("Creating an Rbac app ");
        JsonPath appResponseRbacOpposite = applicationHelper.createApplicationRbac(appName+"Rbac-opposite");
        Assert.assertTrue(appResponseRbacOpposite.getString("resource.name").equalsIgnoreCase(appName+"Rbac-opposite"));
        String appIdRbacOpposite = appResponseRbacOpposite.getString("resource.appId");

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponseOpposite = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "NON_PROD", appIdRbacOpposite);
        String envIdOpposite = envResponseOpposite.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envIdOpposite);

        boolean isFoundOpposite = getListOfAvailableCPInsideInfraRbac(pdcId, appIdRbacOpposite, envIdOpposite);
        Assert.assertFalse(isFoundOpposite);
    }

    @Test
    public void testCreateNonProdAllCPAndInviteAnotherNonProdAllUserToCheckIfHeCanAccessIt() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 3);
        //Create All Apps Prod Secret
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath pcfResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"NON_PROD_ALL","");
        String pdcId = pcfResponse.getString("data.createCloudProvider.cloudProvider.id");

        String rbacUserId = "juG75UHCQxu_MSq_OBxOVw";
        /*
        Create a usergroup with the same permission as the CP
         */
        UserGroupsHelper userGroupsHelper = new UserGroupsHelper();
        System.out.println("Creating an Rbac user group");
        JsonPath rbacResponse = userGroupsHelper.createUserGroup(rbacUserGroupName,rbacUserId,"NON_PROD_ALL","");

        System.out.println("Creating an Rbac app ");
        JsonPath appResponseRbac = applicationHelper.createApplicationRbac(appName+"Rbac");
        Assert.assertTrue(appResponseRbac.getString("resource.name").equalsIgnoreCase(appName+"Rbac"));
        String appIdRbac = appResponseRbac.getString("resource.appId");

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponse = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "NON_PROD", appIdRbac);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envId);

        boolean isFound = getListOfAvailableCPInsideInfraRbac(pdcId, appIdRbac, envId);
        Assert.assertTrue(isFound);

//        //Negative flow [Create another env where it should not be visible.]
         /*
        Create a usergroup with the different permission than CP
         */

        System.out.println("Creating an Rbac user group");
        JsonPath rbacResponseNegative = userGroupsHelper.createUserGroup(rbacUserGroupName,rbacUserId,"PROD_ALL","");

        System.out.println("Creating an Rbac app ");
        JsonPath appResponseRbacOpposite = applicationHelper.createApplicationRbac(appName+"Rbac-opposite");
        Assert.assertTrue(appResponseRbacOpposite.getString("resource.name").equalsIgnoreCase(appName+"Rbac-opposite"));
        String appIdRbacOpposite = appResponseRbacOpposite.getString("resource.appId");

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponseOpposite = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "PROD", appIdRbacOpposite);
        String envIdOpposite = envResponseOpposite.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envIdOpposite);

        boolean isFoundOpposite = getListOfAvailableCPInsideInfraRbac(pdcId, appIdRbacOpposite, envIdOpposite);
        Assert.assertFalse(isFoundOpposite);
    }


    @Test
    public void testCreateProdOnlyOneAppCPAndInviteAnotherProdAOneAppUserToCheckIfHeCanAccessIt() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 3);
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

        //Create All Apps Prod Secret
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath pcfResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_SINGLE_APP",appId);
        String pdcId = pcfResponse.getString("data.createCloudProvider.cloudProvider.id");


        //Create a usergroup && add a user
        UserGroupsHelper userGroupsHelper = new UserGroupsHelper();
        String rbacUserId = "juG75UHCQxu_MSq_OBxOVw";
        System.out.println("Creating an Rbac user group");
        JsonPath rbacResponse = userGroupsHelper.createUserGroup(rbacUserGroupName,rbacUserId,"PROD_SINGLE_APP",appId);

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponse = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envId);

        boolean isFound = getListOfAvailableCPInsideInfraRbac(pdcId, appId, envId);
        Assert.assertTrue(isFound);

//        //Negative flow [Create another env where it should not be visible.]
        System.out.println("Creating an Rbac Env ");
        JsonPath envResponseRbac = environmentsHelper.createEnvironmentRbac(envName+"RBAC", "sample env description", "NON_PROD", appId);
        String envIdRbac = envResponseRbac.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envIdRbac);
        Assert.assertTrue(envIdRbac==null);


         /*
            Create another application in Rbac user
            See if he can access CP in another application
         */

        System.out.println("Creating an Rbac app ");
        JsonPath appResponseRbac = applicationHelper.createApplicationRbac(appName+"Rbac");
        Assert.assertTrue(appResponseRbac.getString("resource.name").equalsIgnoreCase(appName+"Rbac"));
        String appIdRbac = appResponseRbac.getString("resource.appId");

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponseOtherRbac = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "PROD", appIdRbac);
        String envIdOtherRbac = envResponseOtherRbac.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envIdOtherRbac);
        Assert.assertTrue(envIdOtherRbac==null);
    }


    @Test
    public void testCreateNonProdOnlyOneAppCPAndInviteAnotherNonProdAOneAppUserToCheckIfHeCanAccessIt() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 3);
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

        //Create All Apps Prod Secret
        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath pcfResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"NON_PROD_SINGLE_APP",appId);
        String pdcId = pcfResponse.getString("data.createCloudProvider.cloudProvider.id");

        //Create a usergroup && add a user
        UserGroupsHelper userGroupsHelper = new UserGroupsHelper();
        String rbacUserId = "juG75UHCQxu_MSq_OBxOVw";
        System.out.println("Creating an Rbac user group");
        JsonPath rbacResponse = userGroupsHelper.createUserGroup(rbacUserGroupName,rbacUserId,"NON_PROD_SINGLE_APP",appId);

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponse = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "NON_PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envId);

        boolean isFound = getListOfAvailableCPInsideInfraRbac(pdcId, appId, envId);
        Assert.assertTrue(isFound);

//        //Negative flow [Create another env where it should not be visible.]
        System.out.println("Creating an Rbac Env ");
        JsonPath envResponseRbac = environmentsHelper.createEnvironmentRbac(envName+"RBAC", "sample env description", "PROD", appId);
        String envIdRbac = envResponseRbac.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envIdRbac);
        Assert.assertTrue(envIdRbac==null);
         /*
            Create another application in Rbac user
            See if he can access CP in another application
         */
        System.out.println("Creating an Rbac app ");
        JsonPath appResponseRbac = applicationHelper.createApplicationRbac(appName+"Rbac");
        Assert.assertTrue(appResponseRbac.getString("resource.name").equalsIgnoreCase(appName+"Rbac"));
        String appIdRbac = appResponseRbac.getString("resource.appId");

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponseOtherRbac = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "PROD", appIdRbac);
        String envIdOtherRbac = envResponseOtherRbac.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envIdOtherRbac);
        Assert.assertTrue(envIdOtherRbac==null);
    }











    @Test
    public void testConnectorsListAPI() throws IOException{

        GenericRequestBuilder genericRequestBuilder =new GenericRequestBuilder();
        String pdcId= "RfdvaajrS6SHP-OOEAfpBg";
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.param("search[0][field]","category");
        requestSpecification.param("search[0][op]","IN");
        requestSpecification.param("search[0][value]","CLOUD_PROVIDER");

        //List out the list of cloud providers
        Response applicationResponse =
                genericRequestBuilder.getCall(requestSpecification,"/settings");
        //        Response applicationResponse = genericRequestBuilder.getWithParams(queryParams, "/settings");

        List<String> jsonResponse = applicationResponse.jsonPath().getList("resource.response");
        System.out.println("Length is :"+jsonResponse.size());


        for (int i = 0 ; i < jsonResponse.size();i++){
            if (applicationResponse.jsonPath().getString("resource.response["+i+"].uuid").equals(pdcId)) {
                System.out.println("Available here: "+pdcId);
            }
        }
    }

    @Test
    public void testCreateEnvWithCP() throws IOException{
        GenericRequestBuilder genericRequestBuilder =new GenericRequestBuilder();

        String pdcCPName = commonHelper.createRandomName("PDC-DevX-");
        JsonPath appResponse = physicalDataCPHelper.createPDCCp(pdcCPName,"PROD_ALL","");
        String pdcId = appResponse.getString("data.createCloudProvider.cloudProvider.id");

        String appId = "BacnWss4RLGTDt4w80VmlA";
        String envName = commonHelper.createRandomName("Env-DevX-");
        JsonObject envData = new JsonObject();
        envData.addProperty("name",envName);
        envData.addProperty("description",envName);
//        envData.addProperty("environmentType","NON_PROD");
        envData.addProperty("environmentType","PROD");

        //Create an Env
        Response applicationResponse = genericRequestBuilder.postCall(envData, "environments?appId="+appId+"&routingId="+defaultAccountId);
        String envId = applicationResponse.jsonPath().getString("resource.uuid");
        System.out.println("Env Id is :"+envId);

        //Get list of available infra
        RequestSpecification requestSpecification = genericRequestBuilder.getRequestSpecificationObject();
        requestSpecification.param("currentEnvId",envId);
        requestSpecification.param("currentAppId",appId);
        requestSpecification.param("search[0][field]","category");
        requestSpecification.param("search[0][op]","IN");
        requestSpecification.param("search[0][value]","CLOUD_PROVIDER");

        Response infraDefGet = genericRequestBuilder.getCall(requestSpecification,"/settings?accountId="+defaultAccountId);;
//        Response infraDefGet = genericRequestBuilder.getWithParams(queryParams, "/settings?accountId="+defaultAccountId);
        List<String> jsonResponse = infraDefGet.jsonPath().getList("resource.response");
        System.out.println("Length is :"+jsonResponse.size());

        for (int i = 0 ; i < jsonResponse.size();i++){
            if (infraDefGet.jsonPath().getString("resource.response["+i+"].uuid").equals(pdcId)) {
                System.out.println("Found here: "+pdcId);
            }
        }
    }

    @Test
    public void testIfWeCanCreateSecretsGeneric() throws IOException {
        String secretName = commonHelper.createRandomName("DevX-Secret-");
        String secretPass = "3dH7aKJ/0rwGh+dx0YheGcUbh1dxYfrfTqTpKFcujd4=";
        String appId = "q09S9_tVRXCV6iOXkGVl-w";
        JsonPath appResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_ALL",appId,secretPass);
        String secretId = appResponse.getString("data.createSecret.secret.id");
        System.out.println("Secret Id is :"+secretId);
    }
}
