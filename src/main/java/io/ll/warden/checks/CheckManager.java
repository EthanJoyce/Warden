package io.ll.warden.checks;

import java.util.UUID;

/**
 * Creator: LordLambda
 * Date: 3/16/2015
 * Project: Warden
 * Usage: A check manager
 */
public class CheckManager {

  private static CheckManager instance;

  public boolean shouldCheckPlayerForCheck(UUID uuid, Check c) {
    return true;
  }

  protected CheckManager() {
  }

  public static CheckManager get() {
    if(instance == null) {
      synchronized (CheckManager.class) {
        if(instance == null) {
          instance = new CheckManager();
        }
      }
    }
    return instance;
  }

}
