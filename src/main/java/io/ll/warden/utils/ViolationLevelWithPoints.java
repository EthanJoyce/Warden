package io.ll.warden.utils;

import io.ll.warden.heuristics.BanManager;

/**
 * Creator: LordLambda
 * Date: 3/25/2015
 * Project: Warden
 * Usage: Keeps track of a violation level as well as points.
 */
public class ViolationLevelWithPoints {

  private BanManager.ViolationLevel level;
  private int points;

  public ViolationLevelWithPoints(BanManager.ViolationLevel level, int points) {
    this.level = level;
    this.points = points;
  }

  public int getPoints() {
    return points;
  }

  public BanManager.ViolationLevel getLevel() {
    return level;
  }
}
