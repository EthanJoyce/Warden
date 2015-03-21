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

  /**
   * Checks if a password string is correct
   *
   * @param password The password to check
   * @return If it's correct
   */
  public boolean passwordCorrect(String password) {
    return passwordCorrect(PasswordUtils.hash(password).getBytes());
  }

  /**
   * Get it's auth level
   *
   * @return The current accounts auth level
   */
  public AuthAction.AuthLevel getLevel() {
    return level;
  }

  /**
   * Set the auth level
   *
   * @param level Sets the current accounts auth level
   */
  public void setLevel(AuthAction.AuthLevel level) {
    this.level = level;
  }

  /**
   * Determines if a hashed password is correct
   *
   * @param hashed The hashed password
   * @return If it's correct
   */
  public boolean passwordCorrect(byte[] hashed) {
    return hashedPassword.equals(hashed);
  }

  /**
   * Add an auth callback
   *
   * @param back For verifying commands. the auth callback
   */
  public void addCallback(AuthAction.AuthCallback back) {
    needsVerification.add(back);
  }

  /**
   * Remove an AUTH callback
   *
   * @param back Remove it
   */
  public void remCallback(AuthAction.AuthCallback back) {
    if (needsVerification.contains(back)) {
      needsVerification.remove(back);
    }
  }

  /**
   * Get a list of all pending things that need verification
   *
   * @return Returns a list of all pending things that need verification.
   */
  public List<AuthAction.AuthCallback> getVerifications() {
    return needsVerification;
  }

  /**
   * Get the players UUID
   *
   * @return Get the current accounts UUID.
   */
  public UUID getPlayerUUID() {
    return playerUUID;
  }
}