package io.harness.test.rest.base;

import io.harness.rest.core.CoreUtils;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.applications.ApplicationHelper;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;

@Slf4j
public class AbstractTest extends CoreUtils {
  public CommonHelper commonHelper = new CommonHelper();
  ApplicationHelper applicationHelper=new ApplicationHelper();

  @BeforeClass
  public void setupTest() {
    if (System.getProperty("env.type") == "QA") {
      if (!restClient.isTokenValid(restClient.getBearerToken())) {
        String bearerToken = restClient.generateNewBearerToken();
        restClient.setBearerToken(bearerToken);
      }
    } else if (System.getProperty("env.type") == "LOCAL") {
      defaultAccountId = configPropertis.getConfig("DEFAULT_ACCOUNT_LOCAL");
      defaultUser = configPropertis.getConfig("DEFAULT_USER_LOCAL");
      defaultUserPassword = secretsProperties.getSecret("DEFAULT_PASSWORD_LOCAL");
      restClient.loginUser(defaultUser, defaultUserPassword);
    }
  }
}
