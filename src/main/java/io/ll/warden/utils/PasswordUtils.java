package io.ll.warden.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author: LordLambda
 * Date: 3/17/2015
 * Project: Warden
 * Usage: Hashing the things
 */
public class PasswordUtils {

  /**
   * Hashes a String with Keccak (SHA-3)
   *
   * @param toHash The String to hash
   * @return The hash interpreted as a String.
   */
  public static String hash(String toHash) {
    return hashBytes(toHash).toString();
  }

  /**
   * Hash a string to byte[] form
   *
   * @param toHash The String to hash
   * @return The Keccak (SHA-3) hash
   */
  public static byte[] hashBytes(String toHash) {
    return hashBytes(toHash.getBytes());
  }

  /**
   * Just hash a byte array
   *
   * @param hash The byte array to hash
   * @return The hashed value.
   */
  public static byte[] hashBytes(byte[] hash) {
    Keccak k = new Keccak(1600);
    return k.getHash(hash.toString(), 576, 64).getBytes();
  }

  /**
   * Encrypt a byte array with another byte array acting as the password.
   *
   * @param passWord The password as a byte array
   * @param data     The data to encrypt as a byte array
   * @return Null if there is an error, or the encrypted response.
   */
  public static byte[] encryptByteAWithByteA(byte[] passWord, byte[] data) {
    try {
      Cipher AesCipher = Cipher.getInstance("AES");
      SecretKey originalKey = new SecretKeySpec(passWord, 0, passWord.length, "AES");
      AesCipher.init(Cipher.ENCRYPT_MODE, originalKey);
      return AesCipher.doFinal(data);
    } catch (Exception e1) {
      e1.printStackTrace();
      return null;
    }
  }

}
