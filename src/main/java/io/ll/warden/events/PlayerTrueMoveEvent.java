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

  Player player;

  public PlayerTrueMoveEvent(Player p) {
    this.player = p;
  }

  public Player getPlayer() {
    return player;
  }

  public static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers(){
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
