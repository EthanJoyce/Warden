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

  public TimerWithLoc getTwl() {
    return twl;
  }

  public Material getBrokeWith() {
    return brokeWith;
  }

  public Material getBroken() {
    return broken;
  }
}
