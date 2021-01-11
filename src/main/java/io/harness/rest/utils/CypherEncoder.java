package io.harness.rest.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/*
  @author : nataraja.maruthi
 */
public class CypherEncoder {
  public static final String passphrase = "00b4b62eeef694e718613aa370ae17dbd2559db46d6d72064c1182612069f835";

  /*
  Temporary main method to encrypt the secrets,
   */
  public static void main(String[] args) {
    System.out.println(new CypherEncoder().encrypt("3765bf1b6717846d89257956c15b583478a11fb65f97f134a5922e806618f060"));
  }

  public static String encrypt(byte[] secret, String passphrase) {
    try {
      Key aesKey = new SecretKeySpec(Hex.decodeHex(passphrase.toCharArray()), "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, aesKey);
      return Hex.encodeHexString(cipher.doFinal(secret));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String encrypt(byte[] secret) {
    return encrypt(secret, passphrase);
  }

  public String encrypt(String secret) {
    return encrypt(secret.getBytes());
  }

  public static byte[] decrypt(String cipheredSecretHex) {
    try {
      Key aesKey = new SecretKeySpec(Hex.decodeHex(passphrase.toCharArray()), "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, aesKey);
      return cipher.doFinal(Hex.decodeHex(cipheredSecretHex.toCharArray()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String decryptToString(String cipheredSecretHex) {
    return new String(decrypt(cipheredSecretHex));
  }
}
