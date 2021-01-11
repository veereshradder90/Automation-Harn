package io.harness.rest.helper.environments;

public class EnvironmentConstants {
  public static final String REQUEST_JSON_GCP_K8S_CREATION = "/src/main/resources/common/environments/gcpk8s.json";
  public static final String ENV_CREATION_URI = "/environments";
  public static final String ENV_UPDATE_URI = "/environments/{envId}";
  public static final String INFRADEF_CREATE_URI = "/infrastructure-definitions";
  public static final String INFRADEF_UPDATE_URI = "/infrastructure-definitions/{infraDefId}";
  public static final String REQUEST_JSON_AWS_SSH_CREATION = "/src/main/resources/common/environments/awsSSH.json";
  public static final String REQUEST_ENVIRONMENT_CREATION_JSON =
      "/src/main/resources/common/environments/environmentCreation.json";
}
