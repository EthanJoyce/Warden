package io.ll.warden.check.checks.combat;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;
import io.ll.warden.events.CheckFailedEvent;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: Checks for certain type of non-allowed hits
 *
 * Checks for invalid types of hits two specifically.
 * 1. Hits that occur post mortem.
 * 2. Hits that occur while a player is in spectator mode.
 *
 * How to patch.
 * 1. Check if someone is trying to hit while dead.
 * 2. Check if someone is trying to hit while in spectator mode.
 * Throw checks accordingly
 */
public class GhostHitCheck extends Check implements Listener {

  public GhostHitCheck() {
    super("GhostHit");
  }

  public void registerListeners(Warden w, PluginManager pm) {
    pm.registerEvents(this, w);
  }

  public float getRaiseLevel() {
    return 5.0f;
  }

  @EventHandler
  public void onHit(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player) {
      Player p = (Player) event.getDamager();
      if (shouldCheckPlayer(p.getUniqueId())) {
        //Post mortem check first
        if (p.isDead()) {
          Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
              p.getUniqueId(), getRaiseLevel(), getName()
          ));
        }
        //Spectator mode check
        if (p.getGameMode() == GameMode.SPECTATOR) {
          Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
              p.getUniqueId(), getRaiseLevel(), getName()
          ));
        }
      }
    }
  }
}
