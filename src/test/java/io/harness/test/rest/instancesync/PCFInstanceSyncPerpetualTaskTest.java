package io.harness.test.rest.instancesync;

import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.rest.helper.instancesync.InstanceSyncConstants;
import io.harness.rest.helper.instancesync.InstanceSyncHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

@Test(groups = {"PL", "INSTANCE_SYNC", "QA"})
public class PCFInstanceSyncPerpetualTaskTest extends AbstractTest {
  String pcfApplicationId = null;
  String pcfServiceId = null;
  String pcfServiceName = null;
  String nonPcfApplicationId = null;
  String nonPcfServiceId = null;
  String nonPcfServiceName = null;

  InstanceSyncHelper instanceSyncHelper = new InstanceSyncHelper();
  ApplicationHelper applicationHelper = new ApplicationHelper();

  @BeforeClass
  public void setup() {
    // TODO: Need to change these to actual appId and serviceId ;
    pcfApplicationId =
        applicationHelper.getApplicationId(InstanceSyncConstants.PCF_APP_NAME).getString("resource.response[0].appId");
    pcfServiceId = InstanceSyncConstants.PCF_SERVICE_ID;
    pcfServiceName = InstanceSyncConstants.PCF_SERVICE_NAME;
    nonPcfApplicationId = applicationHelper.getApplicationId(InstanceSyncConstants.NON_PCF_APP_NAME)
                              .getString("resource.response[0].appId");
    nonPcfServiceId = InstanceSyncConstants.NON_PCF_SERVICE_ID;
    nonPcfServiceName = InstanceSyncConstants.NON_PCF_SERVICE_NAME;
  }

  @Test
  public void PcfInstanceForApplication() {
    Response response = instanceSyncHelper.getApplicationInstanceSummary(pcfApplicationId, "ENVIRONMENT");
    JsonPath appSummary = response.jsonPath();
    Assert.assertEquals(appSummary.getInt("resource.totalCount"), 1, "Application Instance count should return 1");
    Assert.assertEquals(
        appSummary.getInt("resource.countMap.ENVIRONMENT[0].count"), 1, "Environment Instance count should return 1");
    response = instanceSyncHelper.getApplicationInstanceSummary(pcfApplicationId, "SERVICE");
    appSummary = response.jsonPath();
    Assert.assertEquals(appSummary.getString("resource.countMap.SERVICE[0].entitySummary.id"), pcfServiceId,
        "Service Instance Id should match");
    Assert.assertEquals(appSummary.getString("resource.countMap.SERVICE[0].entitySummary.name"), pcfServiceName,
        "Service Instance name should match");
    Assert.assertEquals(
        appSummary.getInt("resource.countMap.SERVICE[0].count"), 1, "Service Instance count should return 1");
    response = instanceSyncHelper.getApplicationInstanceSummary(pcfApplicationId, "CLOUD_PROVIDER");
    appSummary = response.jsonPath();
    Assert.assertEquals(appSummary.getInt("resource.countMap.CLOUD_PROVIDER[0].count"), 1,
        "Cloud Provider Instance count should return 1");
  }

  @Test
  public void PcfInstanceDetails() {
    Response serviceInstanceDetails = instanceSyncHelper.getInstanceDetails(pcfApplicationId, pcfServiceId);
    Object lastDeployedAt = serviceInstanceDetails.jsonPath().getString("resource.lastDeployedAt");
    Long aLong = Long.valueOf(lastDeployedAt.toString());
    long currentTimeMillis = System.currentTimeMillis();
    System.out.println(aLong);
    int diff = (int) (TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis) - TimeUnit.MILLISECONDS.toMinutes(aLong));
    System.out.println(diff);
    Assert.assertTrue(diff < 40, "Instance sync should be less than 40 min");
  }

  @Test
  public void NonPcfInstanceForApplication() {
    Response response = instanceSyncHelper.getApplicationInstanceSummary(nonPcfApplicationId, "ENVIRONMENT");
    JsonPath appSummary = response.jsonPath();
    Assert.assertEquals(appSummary.getInt("resource.totalCount"), 2, "Application Instance count should return 2");
    Assert.assertEquals(
        appSummary.getInt("resource.countMap.ENVIRONMENT[0].count"), 2, "Environment Instance count should return 2");
    response = instanceSyncHelper.getApplicationInstanceSummary(nonPcfApplicationId, "SERVICE");
    appSummary = response.jsonPath();
    Assert.assertEquals(appSummary.getString("resource.countMap.SERVICE[0].entitySummary.id"), nonPcfServiceId,
        "Service Instance Id should match");
    Assert.assertEquals(appSummary.getString("resource.countMap.SERVICE[0].entitySummary.name"), nonPcfServiceName,
        "Service Instance name should match");
    Assert.assertEquals(
        appSummary.getInt("resource.countMap.SERVICE[0].count"), 2, "Service Instance count should return 2");
    response = instanceSyncHelper.getApplicationInstanceSummary(nonPcfApplicationId, "CLOUD_PROVIDER");
    appSummary = response.jsonPath();
    Assert.assertEquals(appSummary.getInt("resource.countMap.CLOUD_PROVIDER[0].count"), 2,
        "Cloud Provider Instance count should return 2");
  }

  @Test
  public void NonPcfInstanceDetails() {
    Response serviceInstanceDetails = instanceSyncHelper.getInstanceDetails(nonPcfApplicationId, nonPcfServiceId);
    Object lastDeployedAt = serviceInstanceDetails.jsonPath().getString("resource.lastDeployedAt");
    Long aLong = Long.valueOf(lastDeployedAt.toString());
    long currentTimeMillis = System.currentTimeMillis();
    System.out.println(aLong);
    int diff = (int) (TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis) - TimeUnit.MILLISECONDS.toMinutes(aLong));
    System.out.println(diff);
    Assert.assertTrue(diff < 40, "Instance sync should be less than 40 min");
  }
}
