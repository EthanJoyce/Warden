package io.ll.warden.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.ll.warden.utils.BlockBreak;

/**
 * Author: LordLambda
 * Date: 3/21/2015
 * Project: Warden
 * Usage: A true block break event
 */
public class TrueBlockBreakEvent extends Event {

  public static final HandlerList handlers = new HandlerList();
  private Player player;
  private BlockBreak blockBreak;

  public TrueBlockBreakEvent(Player p, BlockBreak bb) {
    player = p;
    blockBreak = bb;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  /**
   * Get the player who broke the block
   *
   * @return The player who broke the block
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Get the actual block break
   *
   * @return The actual block break.
   */
  public BlockBreak getBlockBreak() {
    return blockBreak;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

}
