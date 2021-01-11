package io.harness.rest.helper.cloudproviders.aws;

public class AwsConstants {
  public static final String REQUEST_JSON_AWS_DELEGATE_CREATION =
      "/src/main/resources/project/dx/aws/create_aws_delegate_query.json";
  public static final String REQUEST_JSON_AWS_MANUAL_CREATION =
      "/src/main/resources/project/dx/aws/create_aws_manual_query.json";
  public static final String REQUEST_JSON_AWS_DELEGATE_UPDATE =
      "/src/main/resources/project/dx/aws/update_aws_delegate_query.json";
  public static final String REQUEST_JSON_AWS_MANUAL_UPDATE =
      "/src/main/resources/project/dx/aws/update_aws_manual_query.json";
  public static final String URI_APP_CREATION =
      "https://qa.harness.io/gateway/api/graphql?accountId=XICOBc_qRa2PJmVaWOx-cQ";

  public static final String REQUEST_AWS_CLOUDPROVIDER_CREATION_JSON =
      "/src/main/resources/common/cloudproviders/awscloudprovider.json";
  public static final String URI_AWS_CLOUDPROVIDER_CREATION = "/settings";
  public static final String URI_AWS_CLOUDPROVIDER_UPDATE = "/settings/{cloudproviderId}";
}
