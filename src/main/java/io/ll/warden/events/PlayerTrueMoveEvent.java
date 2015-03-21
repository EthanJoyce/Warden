package io.ll.warden.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Author: LordLambda
 * Date: 3/20/2015
 * Project: Warden
 * Usage: A true move event.
 */
public class PlayerTrueMoveEvent extends Event {

  public static final HandlerList handlers = new HandlerList();
  Player player;

  public PlayerTrueMoveEvent(Player p) {
    this.player = p;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  /**
   * Get the player who moved
   *
   * @return the player who moved.
   */
  public Player getPlayer() {
    return player;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}
