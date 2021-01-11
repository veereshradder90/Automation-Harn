package io.harness.rest.utils;

import java.util.Properties;

/*
  @author : nataraja.maruthi
 */
public class ConfigProperties {
  private static Properties configProperties;


  public static void loadConfigs() {
    try {
      if (null == configProperties)
        configProperties = new Properties();
        configProperties.load(SecretsProperties.class.getClassLoader().getResourceAsStream("config.properties"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getConfig(String config) {
    loadConfigs();
    String env = System.getProperty("env.type");
    String secretWithEnv = "";
    if (null != env) {
      secretWithEnv = config + "_" + env;
    }
    String configValue = configProperties.getProperty(secretWithEnv);
    if (null != configValue) {
      return  configValue;
    }
    config = configProperties.getProperty(config);
    return config;
  }

}
