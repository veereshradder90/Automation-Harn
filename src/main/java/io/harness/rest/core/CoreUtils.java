package io.harness.rest.core;

import io.harness.rest.helper.accessmanagement.login.LoginHelper;
import io.harness.rest.utils.ConfigProperties;
import io.harness.rest.utils.GlobalDataProvider;
import io.harness.rest.utils.JsonFileReader;
import io.harness.rest.utils.RestAssuredResponseParser;
import io.harness.rest.utils.SecretsProperties;

/*
  @author : nataraja.maruthi
 */
public abstract class CoreUtils {
  static {
    if (System.getProperty("env.type") == null) {
      System.setProperty("env.type", "QA");
    }
  }

  public static RestAssuredClient restClient = new RestAssuredClient();
  public static SecretsProperties secretsProperties = new SecretsProperties();
  public static ConfigProperties configPropertis = new ConfigProperties();
  public static String defaultAccountId = configPropertis.getConfig("DEFAULT_ACCOUNT");
  public static String defaultCEAccountId = configPropertis.getConfig("DEFAULT_CE_ACCOUNT_QA");
  public static String defaultUser = configPropertis.getConfig("DEFAULT_USER");
  public static String defaultUserPassword = secretsProperties.getSecret("DEFAULT_PASSWORD");
  public static GlobalDataProvider globalDataProvider = new GlobalDataProvider();
  public static JsonFileReader jReader = new JsonFileReader();
  public static RestAssuredResponseParser responseParser = new RestAssuredResponseParser();
  public static LoginHelper loginHelper = new LoginHelper();
}
