package io.ll.warden.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Creator: LordLambda
 * Date: 3/29/2015
 * Project: Warden
 * Usage: Gets called when a player is banned by warden
 */
public class BanEvent extends Event {

  public static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  private UUID player;
  private float pointsAtBan;

  public BanEvent(UUID playerBanned, float pointsRecieved) {
    player = playerBanned;
    pointsAtBan = pointsRecieved;
  }

  public UUID getUUID() {
    return player;
  }

  public float getPoints() {
    return pointsAtBan;
  }
}
