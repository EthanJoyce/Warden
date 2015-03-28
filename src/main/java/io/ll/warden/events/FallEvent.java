package io.ll.warden.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: An event for when a player falls.
 */
public class FallEvent extends Event {

  public static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  private Player p;
  private Location start;
  private Location finish;

  public FallEvent(Player p, Location start, Location finish) {
    this.p = p;
    this.start = start;
    this.finish = finish;
  }

  public Player getPlayer() {
    return p;
  }

  public Location getStart() {
    return start;
  }

  public Location getFinish() {
    return finish;
  }
}
