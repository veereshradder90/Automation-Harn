package io.harness.rest.helper.services;

public class ServiceConstants {
  public static final String REQUEST_JSON_SERVICE_CREATION =
      "/src/main/resources/common/application/applicationCreation.json";
  public static final String URI_SERVICE_CREATION = "/services";
  public static final String URI_SERVICE_DELETION = "/services/{serviceId}";
  public static final String K8SV2_SERVICE_ARTIFACT_TYPE = "DOCKER";
  public static final String K8SV2_SERVICE_DEPLOYMENT_TYPE = "KUBERNETES";
  public static final String URI_SERVICE_VARIABLES = "/service-variables";
  public static final String SSH_SERVICE_ARTIFACT_TYPE = "WAR";
  public static final String SSH_SERVICE_DEPLOYMENT_TYPE = "SSH";

  public static final String REQUEST_SERVICE_CREATION_JSON = "/src/main/resources/common/service/serviceCreation.json";
}
