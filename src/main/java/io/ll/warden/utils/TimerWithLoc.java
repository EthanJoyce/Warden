package io.ll.warden.utils;

import org.bukkit.Location;

/**
 * Author: LordLambda
 * Date: 3/20/2015
 * Project: Warden
 * Usage: Timer with location attached
 */
public class TimerWithLoc extends Timer {

  private Location loc;

  public TimerWithLoc(Location loc) {
    this.loc = loc;
  }

  public Location getLoc() {
    return loc;
  }
}
