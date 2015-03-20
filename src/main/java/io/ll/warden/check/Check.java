package io.ll.warden.check;

import org.bukkit.plugin.PluginManager;

import java.util.UUID;

import io.ll.warden.Warden;

/**
 * Creator: LordLambda
 * Date: 3/16/2015
 * Project: Warden
 * Usage: An abstract check class
 */
public abstract class Check {

  private String name;

  public Check(String name) {
    this.name = name;
  }

  /**
   * Get the name of this check
   *
   * @return The name of the check
   */
  public String getName() {
    return name;
  }

  /**
   * Register all the listeners for this check
   *
   * @param w  -> warden instance
   * @param pm -> PluginManager instance
   */
  public abstract void registerListeners(Warden w, PluginManager pm);

  /**
   * Get the amount this check raises a players level by
   *
   * @return The level to raise
   */
  public abstract float getRaiseLevel();

  /**
   * Determine if a player should be checked by this check
   *
   * @param uuid The uuid of the player
   * @return If he should be checked
   */
  public boolean shouldCheckPlayer(UUID uuid) {
    return CheckManager.get().shouldCheckPlayerForCheck(uuid, this);
  }
}
