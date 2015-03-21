package io.ll.warden.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Author: LordLambda
 * Date: 3/19/2015
 * Project: Warden
 * Usage: A check failed event
 */
public class CheckFailedEvent extends Event {

  public static final HandlerList handlers = new HandlerList();
  private UUID player;
  private float damage;
  private String checkName;

  public CheckFailedEvent(UUID player, float damage, String check) {
    this.player = player;
    this.damage = damage;
    this.checkName = check;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  /**
   * Get the player who failed the check.
   *
   * @return The player who failed the check
   */
  public UUID getPlayer() {
    return player;
  }

  /**
   * Get the check damage
   *
   * @return The total amount of "damage" or amount to add to possibility of hacker.
   */
  public float getDamage() {
    return damage;
  }

  /**
   * Get the checks name
   *
   * @return Get the checks name that they failed.
   */
  public String getCheckName() {
    return checkName;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}
