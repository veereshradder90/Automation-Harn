package io.harness.test.rest.cdc.sample;

import io.harness.rest.helper.applications.ApplicationHelper;
import io.harness.test.rest.base.AbstractTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.harness.rest.helper.CommonHelper;
import java.io.FileNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;

public class SampleApplicationDemoTest extends AbstractTest {
  ApplicationHelper applicationHelper = new ApplicationHelper();
  CommonHelper commonHelper = new CommonHelper();
  String appName = commonHelper.createRandomName("CDC-Automation-", 5);

  @Test
  public void createApplicationTest() throws FileNotFoundException {
    JsonPath appResponse = applicationHelper.createApplication(appName);
    Assert.assertTrue(appResponse.getString("resource.name").equalsIgnoreCase(appName));

    // assertion through AssertJ:
    assertThat(appResponse.getString("resource.name").equalsIgnoreCase(appName));
  }
}
