package io.ll.warden.checks;

import org.bukkit.plugin.PluginManager;

import java.util.UUID;

import io.ll.warden.Warden;

/**
 * Creator: LordLambda
 * Date: 3/16/2015
 * Project: Warden
 * Usage: A check manager
 */
public class CheckManager {

  private static CheckManager instance;

  /**
   * Initalize everything we need to.
   */
  protected CheckManager() {
  }

  /**
   * Gets the public singleton.
   *
   * @return Get the singleton instance.
   */
  public static CheckManager get() {
    if (instance == null) {
      synchronized (CheckManager.class) {
        if (instance == null) {
          instance = new CheckManager();
        }
      }
    }
    return instance;
  }

  /**
   * Determine if a player is extempt from the check
   *
   * @param uuid The UUID of the player
   * @param c    The check
   * @return If the player based off the UUID should be checked by the passed check.
   */
  public boolean shouldCheckPlayerForCheck(UUID uuid, Check c) {
    return true;
  }

  /**
   * Register a listener for every single check.
   *
   * @param w  An instance of warden
   * @param pm An instance of the PluginManager
   */
  public void registerListeners(Warden w, PluginManager pm) {

  }

}
