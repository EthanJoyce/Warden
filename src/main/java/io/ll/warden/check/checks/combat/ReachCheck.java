package io.ll.warden.check.checks.combat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;
import io.ll.warden.events.CheckFailedEvent;
import io.ll.warden.utils.MathHelper;
import io.ll.warden.utils.MovementHelper;

/**
 * Author: LordLambda
 * Date: 3/21/2015
 * Project: Warden
 * Usage: Checks reach distance in combat
 *
 * This is a fairly simple check. Check if the player is hitting
 * someone farther away than they're allowed too. Make it a pretty
 * low check because lagging people can hit this easily
 */
public class ReachCheck extends Check implements Listener {

  public ReachCheck() {
    super("ReachCheck");
  }

  @Override
  public void registerListeners(Warden w, PluginManager pw) {
    pw.registerEvents(this, w);
  }

  @Override
  public float getRaiseLevel() {
    return 0.5f;
  }

  @EventHandler
  public void onHit(EntityDamageByEntityEvent event) {
    Entity e = event.getDamager();
    if(e instanceof Player) {
      Player p = (Player) e;
      Location playerLocation = MovementHelper.get().getPlayerNLocation(p.getUniqueId());
      Entity damaged = event.getEntity();
      Location damagedLocation = damaged.getLocation();
      //TODO: Make this a not hardcoded value, and make more leninent for lagging players??
      if(MathHelper.getDistance3D(playerLocation, damagedLocation) >= 4.25) {
        Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
            p.getUniqueId(), getRaiseLevel(), getName()
        ));
      }
    }
  }

}
