package io.ll.warden.utils;

/**
 * Author: Brudin
 * Date: 3/20/2015
 * Project: Warden
 * Usage: A simple Timer class.
 */
public class Timer {

  private long lastCheck = getSystemTime();

  /**
   * Checks if the specified amount of second(s) has passed.
   *
   * @param seconds	The specified seconds since last check.
   * @return			<code>true</code> if the time has passed, else <code>false</code>.
   */
  public boolean hasReach(float seconds) {
    return getTimePassed() >= (seconds * 1000);
  }

  /**
   * Resets the <code>lastCheck</code>
   */
  public void reset() {
    lastCheck = getSystemTime();
  }

  /**
   * @return The amount of time (in milliseconds) since the <code>lastCheck</code>.
   */
  private long getTimePassed() {
    return getSystemTime() - lastCheck;
  }

  /**
   * @return The current system time (in milliseconds).
   */
  private long getSystemTime() {
    return System.nanoTime() / (long) (1E6);
  }
}
