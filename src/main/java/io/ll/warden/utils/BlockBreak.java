package io.ll.warden.utils;

import org.bukkit.Material;

/**
 * Author: LordLambda
 * Date: 3/20/2015
 * Project: Warden
 * Usage: A block break.
 */
public class BlockBreak {

  private TimerWithLoc twl;
  private Material brokeWith;
  private Material broken;

  public BlockBreak(TimerWithLoc twl, Material brokeWith, Material broken) {
    this.twl = twl;
    this.brokeWith = brokeWith;
    this.broken = broken;
  }

  /**
   * Get the timer with location
   *
   * @return The timer with location.
   */
  public TimerWithLoc getTwl() {
    return twl;
  }

  /**
   * Get what the block was broken with
   *
   * @return The type of item that was used to break the block.
   */
  public Material getBrokeWith() {
    return brokeWith;
  }

  /**
   * Get the block that was broken
   *
   * @return The type of block that was destroyed.
   */
  public Material getBroken() {
    return broken;
  }
}
