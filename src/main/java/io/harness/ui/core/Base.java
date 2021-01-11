package io.harness.ui.core;

import io.harness.rest.utils.ConfigProperties;
import io.harness.rest.utils.JsonFileReader;
import io.harness.rest.utils.SecretsProperties;
import io.harness.ui.listeners.Listener;
import org.testng.annotations.Listeners;

/**
 * author: shaswat.deep
 */
@Listeners(Listener.class)
public abstract class Base {
  static {
    System.setProperty("env.type", "QA");
  }
  public static SecretsProperties secretsProperties = new SecretsProperties();
  public static ConfigProperties configPropertis = new ConfigProperties();
  public static JsonFileReader jReader = new JsonFileReader();
  public static String defaultUser = configPropertis.getConfig("DEFAULT_USER");
  public static String defaultPassword = secretsProperties.getSecret("DEFAULT_PASSWORD");
}
