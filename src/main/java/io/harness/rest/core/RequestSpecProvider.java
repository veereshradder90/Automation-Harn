package io.harness.rest.core;

import io.harness.rest.utils.ConfigProperties;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ParamConfig;
import io.restassured.config.ParamConfig.UpdateStrategy;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

/*
  @author : nataraja.maruthi
 */
@Slf4j
public class RequestSpecProvider {
  public static ConfigProperties configPropertis = new ConfigProperties();

  public RequestSpecification useDefaultSpec() {
    String baseUri = System.getProperty("BASE_URI");
    String basePath;
    String port;
    if (null == baseUri) {
      baseUri = getBaseUri(System.getProperty("env.type", "QA"));
    }
    String baseURIConfig = configPropertis.getConfig("BASE_URI_LOCAL");
    if (baseUri.equalsIgnoreCase(baseURIConfig)) {
      port = "9090";
    } else {
      port = "0000";
    }
    basePath = getBasePath(baseUri);
    return createRequestSpec(baseUri, port, basePath);
  }

  private RequestSpecification createRequestSpec(String baseUri, String port, String basePath) {
    RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    requestSpecBuilder.setAccept(ContentType.JSON);
    requestSpecBuilder.setContentType(ContentType.JSON);
    requestSpecBuilder.setBaseUri(baseUri);
    requestSpecBuilder.setBasePath(basePath);
    requestSpecBuilder.setConfig(
        RestAssured.config().paramConfig(new ParamConfig().queryParamsUpdateStrategy(UpdateStrategy.REPLACE)));
    requestSpecBuilder.setRelaxedHTTPSValidation();
    if (!port.equals("0000")) {
      requestSpecBuilder.setPort(Integer.parseInt(port));
    }
    // log.info("Querying environment : " + baseUri + ":" + port + basePath);
    if (port.equals("0000")) {
      //       log.info(
      //        "This querying environment would use the default port for the service. This option is good to be used in
      //        non local envs such as QA");
    }

    return requestSpecBuilder.build();
  }

  private static String getBasePath(String baseUri) {
    String basePath = "/api";

    if (baseUri.contains("qa.harness.io") || baseUri.contains("app.harness.io")) {
      basePath = "/gateway/api";
    }
    return basePath;
  }

  private static String getBaseUri(String env) {
    switch (env) {
      case "QA":
        return configPropertis.getConfig("BASE_URI_QA");
      case "PR":
        String namespace = System.getProperty("pr_namespace", "dummy");
        return "https://pr.harness.io/" + namespace;
      case "PROD":
        return "https://app.harness.io";
      case "QB":
        return "https://qb.harness.io";
      case "DEV":
        return "https://dev.harness.io";
      case "UAT":
        return "https://uat.harness.io";
      case "LOCAL":
        return configPropertis.getConfig("BASE_URI_LOCAL");
      default:
        return configPropertis.getConfig("BASE_URI_QA");
    }
  }
}
