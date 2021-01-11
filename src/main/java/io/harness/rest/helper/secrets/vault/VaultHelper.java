package io.harness.rest.helper.secrets.vault;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.api.Auth;
import com.bettercloud.vault.response.AuthResponse;
import com.bettercloud.vault.response.LogicalResponse;
import com.google.gson.JsonObject;
import io.harness.rest.core.CoreUtils;
import io.harness.rest.helper.CommonHelper;
import io.harness.rest.helper.secrets.ssh.SshConstants;
import io.harness.rest.utils.SecretsProperties;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class VaultHelper extends CoreUtils {
    SecretsProperties secretsProperties = new SecretsProperties();
    String token=secretsProperties.getSecret("HASHICORP_VAULT_TOKEN");
    final VaultConfig config = new VaultConfig()
            .address("https://vaultqa.harness.io/")
            .token(token)
            .build();
    final Vault vault = new Vault(config);

    public VaultHelper() throws VaultException {
    }


    public LogicalResponse createSecret(String Key,Object Value,String Path) throws VaultException {
        final Map<String, Object> secrets = new HashMap<String, Object>();
        secrets.put(Key, Value);
        final LogicalResponse writeResponse = vault.logical()
                .write("secret/"+Path, secrets);
        return writeResponse;
    }
    public AuthResponse createToken() throws VaultException {
        final AuthResponse response = vault.auth().createToken(new Auth.TokenRequest());
        return response;
    }
    public AuthResponse createTokenWithLease(String time) throws VaultException {
        final AuthResponse response = vault.auth().createToken(new Auth.TokenRequest().ttl(time));
        return response;
    }
    public String readToken(String Path, String Key) throws VaultException {
        final String value = vault.logical()
                .read("secret/"+Path)
                .getData().get(Key);
        return value;
    }


}
