package io.ll.warden.utils;

import org.bukkit.Location;

/**
 * Author: m1enkrafftman
 * Date: 3/19/2015
 * Project: Warden
 * Usage:  A math helper written by a good guy
 */
public class MathHelper {

  public static double getDistance3D(Location one, Location two) {
    double toReturn = 0.0;
    double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
    double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
    double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
    double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
    toReturn = Math.abs(sqrt);
    return toReturn;
  }

  public static double getHorizontalDistance(Location one, Location two) {
    double toReturn = 0.0;
    double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
    double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
    double sqrt = Math.sqrt(xSqr + zSqr);
    toReturn = Math.abs(sqrt);
    return toReturn;
  }

}
