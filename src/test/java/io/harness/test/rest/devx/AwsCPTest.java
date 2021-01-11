package io.harness.test.rest.devx;

import io.harness.rest.core.GenericRequestBuilder;
import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.cloudproviders.aws.AwsHelper;
import io.harness.rest.helper.cloudproviders.physicaldata.PhysicalDataCPHelper;
import io.harness.rest.helper.devxusergroups.UserGroupsHelper;
import io.harness.rest.helper.environments.EnvironmentsHelper;
import io.harness.rest.helper.secretsdevxgraphql.SecretsGraphQLHelper;
import io.harness.rest.helper.services.ServiceHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.harness.rest.helper.cloudproviders.InfraDefs.getListOfAvailableCPInsideInfra;
import static io.harness.rest.helper.cloudproviders.InfraDefs.getListOfAvailableCPInsideInfraRbac;

public class AwsCPTest extends AbstractTest {
    AwsHelper awsHelper = new AwsHelper();
    SecretsGraphQLHelper secretsGraphQLHelper = new SecretsGraphQLHelper();
    GenericRequestBuilder genericRequestBuilder =new GenericRequestBuilder();
    ApplicationHelper applicationHelper = new ApplicationHelper();
    ServiceHelper serviceHelper = new ServiceHelper();
    EnvironmentsHelper environmentsHelper = new EnvironmentsHelper();
    String secretPass = secretsProperties.getSecret("AWS_ACCESS_SECRET_KEY");
    String envName = commonHelper.createRandomName("DevX-Automation-Env", 3);
    String rbacUserGroupName = "CP-UserGroup-RBAC";
    String tag = "Sudipto";


    @AfterTest
    public void deleteAppsCreatedInTest() {
        applicationHelper.searchAndDeleteApp("DevX-Automation-");
    }

    @Test
    public void testAWSDelegateCP() throws IOException {
        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath appResponse = awsHelper.createAWSDelegateCp(awsCpName,tag,"PROD_ALL","");
        Assert.assertTrue(appResponse.getString("data.createCloudProvider.cloudProvider.name").equalsIgnoreCase(awsCpName));
    }

    @Test
    public void testEditAWSDelegateCP() throws IOException {
        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath appResponse = awsHelper.createAWSDelegateCp(awsCpName,tag,"NON_PROD_ALL","");
        String id = appResponse.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath updatedAppResponse = awsHelper.editAWSDelegateCp(id,awsCpName+"-Updated",tag);
        Assert.assertTrue(updatedAppResponse.getString("data.updateCloudProvider.cloudProvider.name").equalsIgnoreCase(awsCpName+"-Updated"));
    }

    @Test
    public void testDeleteAWSDelegateCP() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");
        JsonPath awsResponse = awsHelper.createAWSDelegateCp(awsCpName, tag,"PROD_SINGLE_APP",appId);
        String id = awsResponse.getString("data.createCloudProvider.cloudProvider.id");
        JsonPath updatedAppResponse=awsHelper.deleteAWSDelegateCP(id);
        Assert.assertTrue(!updatedAppResponse.getString("data.deleteCloudProvider.clientMutationId").isEmpty());
    }

    @Test
    public void testAWSManualCP() throws IOException {
        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");
        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");
        JsonPath appResponse = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        Assert.assertTrue(appResponse.getString("data.createCloudProvider.cloudProvider.name").equalsIgnoreCase(awsCpName));
    }


    @Test
    public void testIfAWSCpByNameIsWorking() throws IOException{
        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");
        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");
        JsonPath appResponse = awsHelper.createAWSManualCp(awsCpName,keySecretId);

        PhysicalDataCPHelper physicalDataCPHelper = new PhysicalDataCPHelper();
        JsonPath cpByNameResponse = physicalDataCPHelper.getCPByNameTest(awsCpName);
        Assert.assertTrue(cpByNameResponse.getString("data.cloudProviderByName.name").equalsIgnoreCase(awsCpName));
    }

    @Test
    public void testIfAWSCpByIdIsWorking() throws IOException{
        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");
        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");
        JsonPath appResponse = awsHelper.createAWSManualCp(awsCpName,keySecretId);

        String cpId = appResponse.getString("data.createCloudProvider.cloudProvider.id");

        PhysicalDataCPHelper physicalDataCPHelper = new PhysicalDataCPHelper();
        JsonPath cpByIdResponse = physicalDataCPHelper.getCPByIdTest(cpId);
        Assert.assertTrue(cpByIdResponse.getString("data.cloudProvider.id").equalsIgnoreCase(cpId));
        Assert.assertTrue(!cpByIdResponse.getString("data.cloudProvider.name").isEmpty());
    }

    @Test
    public void testEditAWSManualCP() throws IOException {
        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");
        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");
        JsonPath appResponse = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String id = appResponse.getString("data.createCloudProvider.cloudProvider.id");
        JsonPath updatedAppResponse = awsHelper.editAWSManualCp(id,awsCpName+"-Updated",keySecretId);
        Assert.assertTrue(updatedAppResponse.getString("data.updateCloudProvider.cloudProvider.name").equalsIgnoreCase(awsCpName+"-Updated"));
    }

    @Test
    public void testDeleteAWSManualCP() throws IOException {
        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");
        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");
        JsonPath appResponse = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String id = appResponse.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath updatedAppResponse=awsHelper.deleteAWSDelegateCP(id);
        Assert.assertTrue(!updatedAppResponse.getString("data.deleteCloudProvider.clientMutationId").isEmpty());
    }

    @Test
    public void testRbacCurrentUserAllAppsProdCheck() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        //Create All Apps Prod Secret
        String secretName = commonHelper.createRandomName("DevX-Secret-");

        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");

        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath awsManualCp = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String awsId = awsManualCp.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

//        Positive flow
        JsonPath envResponse = environmentsHelper.createEnvironment(envName, "sample env description", "PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env

        boolean isFound = getListOfAvailableCPInsideInfra(awsId, appId, envId);
        Assert.assertTrue(isFound);

        //Negative flow [Create another env where it should not be visible.]
        JsonPath envResponseNonProd = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "NON_PROD", appId);
        String envIdNonProd = envResponseNonProd.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresent = getListOfAvailableCPInsideInfra(awsId, appId, envIdNonProd);
        System.out.println("Should not be present :"+shouldNotBePresent);
        Assert.assertFalse(shouldNotBePresent);
    }
    //
    @Test
    public void testRbacCurrentUserAllAppsNonProdCheck() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        //Create All Apps Prod Secret
        String azureName = commonHelper.createRandomName("Azure-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");

        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"NON_PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");

        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath awsManualCp = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String awsId = awsManualCp.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

//        Positive flow
        JsonPath envResponse = environmentsHelper.createEnvironment(envName, "sample env description", "NON_PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env

        boolean isFound = getListOfAvailableCPInsideInfra(awsId, appId, envId);
        Assert.assertTrue(isFound);

        //Negative flow [Create another env where it should not be visible.]
        JsonPath envResponseNonProd = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "PROD", appId);
        String envIdNonProd = envResponseNonProd.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresent = getListOfAvailableCPInsideInfra(awsId, appId, envIdNonProd);
        System.out.println("Should not be present :"+shouldNotBePresent);
        Assert.assertFalse(shouldNotBePresent);
    }
    //
    @Test
    public void testRbacCurrentUserOneAppProdCheck() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        //Create All Apps Prod Secret
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");


        String azureName = commonHelper.createRandomName("Azure-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");

        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_SINGLE_APP",appId,secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");

        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath awsManualCp = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String awsId = awsManualCp.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath envResponse = environmentsHelper.createEnvironment(envName, "sample env description", "PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env

        boolean isFound = getListOfAvailableCPInsideInfra(awsId, appId, envId);
        Assert.assertTrue(isFound);
//
//        //Negative flow [Create another env where it should not be visible.]
        JsonPath envResponseNonProd = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "NON_PROD", appId);
        String envIdNonProd = envResponseNonProd.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresent = getListOfAvailableCPInsideInfra(awsId, appId, envIdNonProd);
        System.out.println("Should not be present :"+shouldNotBePresent);
        Assert.assertFalse(shouldNotBePresent);


        //Create another app , It shouldn't be visible there
        JsonPath appResponseNew = applicationHelper.createApplication(appName+"New");
        Assert.assertTrue(appResponseNew.getString("resource.name").equalsIgnoreCase(appName+"New"));
        String appIdNew = appResponseNew.getString("resource.appId");

        JsonPath envResponseNew = environmentsHelper.createEnvironment(envName, "sample env description", "PROD", appIdNew);
        String envIdNew = envResponseNew.getString("resource.uuid");//Create App && Create Env

        boolean isFoundNew = getListOfAvailableCPInsideInfra(awsId, appIdNew, envIdNew);
        Assert.assertFalse(isFoundNew);

        JsonPath envResponseNonProdNew = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "NON_PROD", appId);
        String envIdNonProdNew = envResponseNonProdNew.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresentNew = getListOfAvailableCPInsideInfra(awsId, appIdNew, envIdNonProdNew);
        System.out.println("Should not be present :"+shouldNotBePresentNew);
        Assert.assertFalse(shouldNotBePresentNew);
    }
    //
    @Test
    public void testRbacCurrentUserOneAppsNonProdCheck() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        //Create All Apps Prod Secret
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

        String azureName = commonHelper.createRandomName("Azure-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");

        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"NON_PROD_SINGLE_APP",appId,secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");

        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath awsManualCp = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String awsId = awsManualCp.getString("data.createCloudProvider.cloudProvider.id");

        JsonPath envResponse = environmentsHelper.createEnvironment(envName, "sample env description", "NON_PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env

        boolean isFound = getListOfAvailableCPInsideInfra(awsId, appId, envId);
        Assert.assertTrue(isFound);
//
//        //Negative flow [Create another env where it should not be visible.]
        JsonPath envResponseNonProd = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "PROD", appId);
        String envIdNonProd = envResponseNonProd.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresent = getListOfAvailableCPInsideInfra(awsId, appId, envIdNonProd);
        System.out.println("Should not be present :"+shouldNotBePresent);
        Assert.assertFalse(shouldNotBePresent);


        //Create another app , It shouldn't be visible there
        JsonPath appResponseNew = applicationHelper.createApplication(appName+"New");
        Assert.assertTrue(appResponseNew.getString("resource.name").equalsIgnoreCase(appName+"New"));
        String appIdNew = appResponseNew.getString("resource.appId");

        JsonPath envResponseNew = environmentsHelper.createEnvironment(envName, "sample env description", "PROD", appIdNew);
        String envIdNew = envResponseNew.getString("resource.uuid");//Create App && Create Env

        boolean isFoundNew = getListOfAvailableCPInsideInfra(awsId, appIdNew, envIdNew);
        Assert.assertFalse(isFoundNew);

        JsonPath envResponseNonProdNew = environmentsHelper.createEnvironment(envName+"-Different", "sample env description", "NON_PROD", appId);
        String envIdNonProdNew = envResponseNonProdNew.getString("resource.uuid");//Create App && Create Env

        boolean shouldNotBePresentNew = getListOfAvailableCPInsideInfra(awsId, appIdNew, envIdNonProdNew);
        System.out.println("Should not be present :"+shouldNotBePresentNew);
        Assert.assertFalse(shouldNotBePresentNew);
    }

    @Test
    public void testCreateProdAllCPAndInviteAnotherProdAllUserToCheckIfHeCanAccessIt() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        //Create All Apps Prod Secret
        String azureName = commonHelper.createRandomName("Azure-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");

        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");

        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath awsManualCp = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String awsId = awsManualCp.getString("data.createCloudProvider.cloudProvider.id");

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

        boolean isFound = getListOfAvailableCPInsideInfraRbac(awsId, appIdRbac, envId);
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

        boolean isFoundOpposite = getListOfAvailableCPInsideInfraRbac(awsId, appIdRbacOpposite, envIdOpposite);
        Assert.assertFalse(isFoundOpposite);
    }



    @Test
    public void testCreateProdOnlyOneAppCPAndInviteAnotherProdAOneAppUserToCheckIfHeCanAccessIt() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

        //Create All Apps Prod Secret
        String azureName = commonHelper.createRandomName("Azure-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");

        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"PROD_SINGLE_APP",appId,secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");

        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath awsManualCp = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String awsId = awsManualCp.getString("data.createCloudProvider.cloudProvider.id");

        //Create a usergroup && add a user
        UserGroupsHelper userGroupsHelper = new UserGroupsHelper();
        String rbacUserId = "juG75UHCQxu_MSq_OBxOVw";
        System.out.println("Creating an Rbac user group");
        JsonPath rbacResponse = userGroupsHelper.createUserGroup(rbacUserGroupName,rbacUserId,"PROD_SINGLE_APP",appId);

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponse = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envId);

        boolean isFound = getListOfAvailableCPInsideInfraRbac(awsId, appId, envId);
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
    public void testCreateNonProdAllCPAndInviteAnotherNonProdAllUserToCheckIfHeCanAccessIt() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        //Create All Apps Prod Secret
        String azureName = commonHelper.createRandomName("Azure-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");

        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"NON_PROD_ALL","",secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");

        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath awsManualCp = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String awsId = awsManualCp.getString("data.createCloudProvider.cloudProvider.id");

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

        boolean isFound = getListOfAvailableCPInsideInfraRbac(awsId, appIdRbac, envId);
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

        boolean isFoundOpposite = getListOfAvailableCPInsideInfraRbac(awsId, appIdRbacOpposite, envIdOpposite);
        Assert.assertFalse(isFoundOpposite);
    }

    @Test
    public void testCreateNonProdOnlyOneAppCPAndInviteAnotherNonProdAOneAppUserToCheckIfHeCanAccessIt() throws IOException {
        String appName = commonHelper.createRandomName("DevX-Automation-", 8);
        JsonPath appResponse = applicationHelper.createApplication(appName);
        Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));
        String appId = appResponse.getString("resource.appId");

        //Create All Apps Prod Secret
        String azureName = commonHelper.createRandomName("Azure-DevX-");
        String secretName = commonHelper.createRandomName("DevX-Secret-");

        JsonPath secretCreatedResponse = secretsGraphQLHelper.createSecretUsageScopeGeneric(secretName,"NON_PROD_SINGLE_APP",appId,secretPass);
        String keySecretId = secretCreatedResponse.getString("data.createSecret.secret.id");

        String awsCpName = commonHelper.createRandomName("AWS-DevX-");
        JsonPath awsManualCp = awsHelper.createAWSManualCp(awsCpName,keySecretId);
        String awsId = awsManualCp.getString("data.createCloudProvider.cloudProvider.id");

        //Create a usergroup && add a user
        UserGroupsHelper userGroupsHelper = new UserGroupsHelper();
        String rbacUserId = "juG75UHCQxu_MSq_OBxOVw";
        System.out.println("Creating an Rbac user group");
        JsonPath rbacResponse = userGroupsHelper.createUserGroup(rbacUserGroupName,rbacUserId,"NON_PROD_SINGLE_APP",appId);

        System.out.println("Creating an Rbac Env ");
        JsonPath envResponse = environmentsHelper.createEnvironmentRbac(envName, "sample env description", "NON_PROD", appId);
        String envId = envResponse.getString("resource.uuid");//Create App && Create Env
        System.out.println("Env Id is :"+envId);

        boolean isFound = getListOfAvailableCPInsideInfraRbac(awsId, appId, envId);
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
}
