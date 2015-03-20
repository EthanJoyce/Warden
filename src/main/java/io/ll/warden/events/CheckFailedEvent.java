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

  public UUID getPlayer() {
    return player;
  }

  public float getDamage() {
    return damage;
  }

  public String getCheckName() {
    return checkName;
  }

  @Override
  public HandlerList getHandlers(){
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
