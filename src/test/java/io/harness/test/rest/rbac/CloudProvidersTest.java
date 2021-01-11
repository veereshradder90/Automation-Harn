package io.harness.test.rest.rbac;

import io.harness.rest.helper.cloudproviders.cloudprovider.CloudProviderHelper;
import io.harness.test.rest.base.AbstractTest;
import io.harness.rest.helper.cloudproviders.aws.AwsHelper;
import io.harness.rest.helper.cloudproviders.azure.AzureHelper;
import io.harness.rest.helper.cloudproviders.gcp.GCPHelper;
import io.harness.rest.helper.cloudproviders.k8s.K8SHelper;
import io.harness.rest.helper.cloudproviders.pcf.PcfHelper;
import io.harness.rest.helper.cloudproviders.physicaldata.PhysicalDataCPHelper;
import io.harness.rest.helper.cloudproviders.spotinst.SpotInstCPHelper;
import io.harness.rest.core.GenericRequestBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CloudProvidersTest extends AbstractTest {
  CloudProviderHelper cloudProviderHelper = new CloudProviderHelper();
  AwsHelper awsHelper = new AwsHelper();
  AzureHelper azureHelper = new AzureHelper();
  GCPHelper gcpHelper = new GCPHelper();
  K8SHelper k8sHelper = new K8SHelper();
  PcfHelper pcfHelper = new PcfHelper();
  PhysicalDataCPHelper pdcHelper = new PhysicalDataCPHelper();
  SpotInstCPHelper spotinstHelper = new SpotInstCPHelper();
  String rbacUser = configPropertis.getConfig("RBAC_ACCOUNT_LEVEL_USER");
  String rbacPassword = secretsProperties.getSecret("RBAC_TEST_USER_PASSWORD_QA");

  @Test
  public void azureTest() {
    // add azure cloud provider- user not authorized
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    String azureName = commonHelper.createRandomName("azure-");
    JsonPath addResponse = azureHelper.createAzureCloudProvider(requestSpecificationObject, azureName);
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // delete azure cloud provider- user not authorized
    Response deleteCloudProvider = cloudProviderHelper.deleteCloudProviderById("zVRSeyz8TvKJupLZG32KnQ");
    assertThat(deleteCloudProvider.getStatusCode()).isEqualTo(400);
  }

  @Test
  public void kubernetesTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    // add k8s cloud provider
    String k8sName = commonHelper.createRandomName("k8s-");
    JsonPath addResponse = k8sHelper.createKubernetesCloudProvider(requestSpecificationObject, k8sName);
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // delete k8s cloud provider- user not authorized
    Response deleteCloudProvider = cloudProviderHelper.deleteCloudProviderById("ondf5CKGSU2ln05VvYnO5Q");
    assertThat(deleteCloudProvider.getStatusCode()).isEqualTo(400);
  }

  @Test
  public void physicalDataCenterTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    // add physical data center cloud provider
    String pdcName = commonHelper.createRandomName("pdc-");
    JsonPath addResponse = pdcHelper.createPhysicalDataCenterCloudProvider(requestSpecificationObject, pdcName);
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // delete pdc cloud provider- user not authorized
    Response deleteCloudProvider = cloudProviderHelper.deleteCloudProviderById("PQF1semtS-2O5z7xxmwPmA");
    assertThat(deleteCloudProvider.getStatusCode()).isEqualTo(400);
  }

  @Test
  public void googleTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    // add google cloud provider
    String gcpName = commonHelper.createRandomName("gcp-");
    JsonPath addResponse = gcpHelper.createGoogleCloudProvider(requestSpecificationObject, gcpName);
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // delete google cloud provider- user not authorized
    Response deleteCloudProvider = cloudProviderHelper.deleteCloudProviderById("rM4k05SqR3SPftBILqwHDg");
    assertThat(deleteCloudProvider.getStatusCode()).isEqualTo(400);
  }

  @Test
  public void spotinstTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    // add spotinst cloud provider
    String spotinstName = commonHelper.createRandomName("spotinst-");
    JsonPath addResponse = spotinstHelper.createSpotInstCloudProvider(requestSpecificationObject, spotinstName);
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // delete spotinst cloud provider- user not authorized
    Response deleteCloudProvider = cloudProviderHelper.deleteCloudProviderById("C20bP8yVT-SSd8MJroBGJQ");
    assertThat(deleteCloudProvider.getStatusCode()).isEqualTo(400);
  }

  @Test
  public void pivotalCloudFoundryTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    // add pcf cloud provider
    String pcfName = commonHelper.createRandomName("pcf-");
    JsonPath addResponse = pcfHelper.createPCFCloudProvider(requestSpecificationObject, pcfName);
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // delete pcf cloud provider- user not authorized
    Response deleteCloudProvider = cloudProviderHelper.deleteCloudProviderById("yBRUwxotRhmLINbNUphxaA");
    assertThat(deleteCloudProvider.getStatusCode()).isEqualTo(400);
  }

  @Test
  public void awsCloudProviderTest() {
    RequestSpecification requestSpecificationObject =
        GenericRequestBuilder.getRequestSpecificationObject(rbacUser, rbacPassword);
    // add aws cloud provider
    String awsName = commonHelper.createRandomName("aws-");
    JsonPath addResponse = awsHelper.createAWSCloudProvider(requestSpecificationObject, awsName);
    assertThat(
        addResponse.getString("responseMessages.message").equalsIgnoreCase("Invalid request: User not authorized"));
    // delete pcf cloud provider- user not authorized
    Response deleteCloudProvider = cloudProviderHelper.deleteCloudProviderById("1IU5xoCxTBSQs7Bswv4NRQ");
    assertThat(deleteCloudProvider.getStatusCode()).isEqualTo(400);
  }
}