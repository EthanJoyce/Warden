package io.ll.warden.check.checks.combat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;
import io.ll.warden.events.CheckFailedEvent;
import io.ll.warden.utils.Timer;

/**
 * Author: LordLambda
 * Date: 3/23/2015
 * Project: Warden
 * Usage: How often a user can hit another user.
 *
 * This check has a slightly different time than window click speed. This is due to the fact
 * that invincibility ticks, and such throw some things off. So an accurate time is around
 * 100MS (the default).
 */
public class FightFrequencyCheck extends Check implements Listener {

  private long MS_VALUE;
  private HashMap<UUID, Timer> map;

  public FightFrequencyCheck() {
    super("FightFrequencyCheck");
    map = new HashMap<UUID, Timer>();
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    MS_VALUE = w.getConfig().getInt("FIGHTFREQUENCY");
    pm.registerEvents(this, w);
  }

  @EventHandler
  public void onHit(EntityDamageByEntityEvent event) {
    Entity e = event.getDamager();
    if (e instanceof Player) {
      Player p = (Player) e;
      if (shouldCheckPlayer(p.getUniqueId())) {
        if (!map.containsKey(p.getUniqueId())) {
          map.put(p.getUniqueId(), new Timer());
        }
        Timer t = map.get(p.getUniqueId());
        if (!t.hasReachMS(MS_VALUE)) {
          Bukkit.getPluginManager().callEvent(new CheckFailedEvent(
              p.getUniqueId(), getRaiseLevel(), getName()
          ));
        } else {
          t.reset();
        }
      }
    }
  }

  @Override
  public float getRaiseLevel() {
    return 3f;
  }

}
