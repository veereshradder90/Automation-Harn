package io.harness.rest.helper.cloudproviders.k8s;

public class K8SConstants {
  public static final String REQUEST_JSON_K8S_DELEGATE_CREATION =
      "/src/main/resources/project/dx/k8s/create_k8s_delegate_query.json";
  public static final String REQUEST_JSON_K8S_DELEGATE_UPDATE =
      "/src/main/resources/project/dx/k8s/update_k8s_delegate_query.json";

  public static final String REQUEST_JSON_K8S_MANUAL_USERNAME_PASSWORD_CREATION =
      "/src/main/resources/project/dx/k8s/create_k8s_username_password_query.json";
  public static final String REQUEST_JSON_K8S_MANUAL_USERNAME_PASSWORD_UPDATE =
      "/src/main/resources/project/dx/k8s/update_k8s_username_password_query.json";

  public static final String REQUEST_JSON_K8S_MANUAL_SERVICE_ID_CREATION =
      "/src/main/resources/project/dx/k8s/create_k8s_service_account_query.json";
  public static final String REQUEST_JSON_K8S_MANUAL_SERVICE_ID_UPDATE =
      "/src/main/resources/project/dx/k8s/update_k8s_service_account_query.json";

  public static final String REQUEST_JSON_K8S_MANUAL_OIDC_CREATION =
      "/src/main/resources/project/dx/k8s/create_k8s_oidc_query.json";
  public static final String REQUEST_JSON_K8S_MANUAL_OIDC_UPDATE =
      "/src/main/resources/project/dx/k8s/update_k8s_oidc_query.json";

  public static final String REQUEST_JSON_K8S_MANUAL_CUSTOM_CREATION =
      "/src/main/resources/project/dx/k8s/create_k8s_manual_query.json";
  public static final String REQUEST_JSON_K8S_MANUAL_CUSTOM_UPDATE =
      "/src/main/resources/project/dx/k8s/update_k8s_manual_query.json";

  public static final String URI_APP_CREATION =
      "https://qa.harness.io/gateway/api/graphql?accountId=XICOBc_qRa2PJmVaWOx-cQ";

  public static final String REQUEST_KUBERNETES_CP_CREATION_JSON =
      "/src/main/resources/common/cloudproviders/kubernetescloudprovider.json";

  public static final String URI_K8S_CLOUDPROVIDER_CREATION = "/settings";
}
