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

  public static String hash(String toHash) {
    return hashBytes(toHash).toString();
  }

  public static byte[] hashBytes(String toHash) {
    return hashBytes(toHash.getBytes());
  }

  public static byte[] hashBytes(byte[] hash) {
    Keccak k = new Keccak(1600);
    return k.getHash(hash.toString(), 576, 64).getBytes();
  }

  public static byte[] encryptWithPassword(byte[] passWord, byte[] data) {
    try {
      Cipher AesCipher = Cipher.getInstance("AES");
      SecretKey originalKey = new SecretKeySpec(passWord, 0, passWord.length, "AES");
      AesCipher.init(Cipher.ENCRYPT_MODE, originalKey);
      return AesCipher.doFinal(data);
    }catch(Exception e1) {
      e1.printStackTrace();
      return null;
    }
  }

}
