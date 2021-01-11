package io.harness.rest.helper.secretmanager;

public class SecretManagerConstants {
  public static final String LIST_SECRET_MANAGERS = "/secrets/list-configs";

  public static final String REQUEST_JSON_VAULT_SM_TOKEN =
      "/src/main/resources/project/pl/secretmanagers/vaulttoken.json";

  public static final String REQUEST_JSON_AZURE_VAULT =
      "/src/main/resources/project/pl/secretmanagers/azure_vault.json";

  public static final String REQUEST_JSON_AWS_KMS = "/src/main/resources/project/pl/secretmanagers/aws_kms.json";

  public static final String ADD_VAULT_SM = "/vault";

  public static final String KMS = "/kms";

  public static final String ADD_AWS_KMS = "/kms/save-kms";

  public static final String ADD_AWS_SM = "/aws-secrets-manager";

  public static final String ADD_AZURE_VAULT = "/azure-secrets-manager";

  public static final String ADD_GCP_KMS = "/gcp-secrets-manager";

  public static final String ADD_CUSTOM_SM = "/custom-secrets-managers";

  public static final String UPDATE_CUSTOM_SM = "/custom-secrets-managers/{secretManagerId}";

  public static final String MIGRATE_SM = "/secrets/transition-config";

  public static final String VAULT_SECRET_MANAGER = "VaultSM";

  public static final String DEFAULT_GCP_SECRET_MANAGER = "Harness Secrets Manager ";

  public static final String GOOGKE_KMS = "GoogleKMS";

  public static final String AZURE_SECRET_MANAGER = "AzureVaultSecretManager";
}
