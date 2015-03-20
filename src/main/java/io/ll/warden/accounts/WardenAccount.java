package io.ll.warden.accounts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.ll.warden.commands.AuthAction;
import io.ll.warden.utils.PasswordUtils;

/**
 * Author: LordLambda
 * Date: 3/17/2015
 * Project: Warden
 * Usage: A simple warden account
 */
public class WardenAccount {

  private UUID playerUUID;
  private List<AuthAction.AuthCallback> needsVerification;
  private AuthAction.AuthLevel level;
  private byte[] hashedPassword;

  public WardenAccount(UUID uuid, AuthAction.AuthLevel level, byte[] hashedPassword) {
    this.playerUUID = uuid;
    this.level = level;
    this.hashedPassword = hashedPassword;
    this.needsVerification = new ArrayList<AuthAction.AuthCallback>();
  }

  public boolean passwordCorrect(String password) {
    return passwordCorrect(PasswordUtils.hash(password).getBytes());
  }

  public AuthAction.AuthLevel getLevel() {
    return level;
  }

  public void setLevel(AuthAction.AuthLevel level) {
    this.level = level;
  }

  public boolean passwordCorrect(byte[] hashed) {
    return hashedPassword.equals(hashed);
  }

  public void addCallback(AuthAction.AuthCallback back) {
    needsVerification.add(back);
  }

  public void remCallback(AuthAction.AuthCallback back) {
    if(needsVerification.contains(back)) {
      needsVerification.remove(back);
    }
  }

  public List<AuthAction.AuthCallback> getVerifications() {
    return needsVerification;
  }

  public UUID getPlayerUUID() {
    return playerUUID;
  }
}