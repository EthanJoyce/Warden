package io.ll.warden.check.checks.inventory;

import org.bukkit.plugin.PluginManager;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;

/**
 * Author: LordLambda
 * Date: 3/23/2015
 * Project: Warden
 * Usage: Checks for how fast your clicking a window.
 *
 * Look. These people get no slacks. At all. They have hacks like ChestStealer, and such.
 * This is basically just them automating clicking things out of a chest. Well. We want to
 * try, and nerf this advantage as much as possible. Average people click around 11.5 times
 * per second (gamers that is). So if we make sure you can't click faster than that. We've
 * nerfed some originally players. But we've made it an even playing field.
 */
public class WindowClickSpeedCheck extends Check {

  public WindowClickSpeedCheck() {
    super("WindowClickSpeedCheck");
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {

  }

  @Override
  public float getRaiseLevel() {
    return 0.3f;
  }
}
