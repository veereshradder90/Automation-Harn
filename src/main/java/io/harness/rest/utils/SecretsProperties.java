package io.harness.rest.utils;

import java.util.Properties;

/*
  @author : nataraja.maruthi
 */
public class SecretsProperties {

  CypherEncoder encoder = new CypherEncoder();

  private static Properties secretsProperties ;


  public static void loadSecrets() {
    try {
      if (null == secretsProperties)
        secretsProperties = new Properties();
        secretsProperties.load(SecretsProperties.class.getClassLoader().getResourceAsStream("secrets.properties"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getSecret(String secret) {
    loadSecrets();
    String env = System.getProperty("env.type");
    String secretWithEnv = "";
    if (null != env) {
      secretWithEnv = secret + "_" + env;
    }
    String encryptedString = secretsProperties.getProperty(secretWithEnv);
    if (null != encryptedString) {
      String decryptedString = encoder.decryptToString(encryptedString);
      return decryptedString;
    }
    encryptedString = secretsProperties.getProperty(secret);
    String decryptedString = encoder.decryptToString(encryptedString);
    return decryptedString;

  }

}
