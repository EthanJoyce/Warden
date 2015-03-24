package io.ll.warden.check.checks.world;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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
 * Usage: The block place speed check.
 *
 * Check how fast a player can place blocks down. This will block NoBlockPlaceLimit as well as
 * Timers, and such that let you place down blocks faster. This works because limits, and testing.
 */
public class BlockPlaceSpeedCheck extends Check implements Listener {

  private long MS_LIMIT;
  private HashMap<UUID, Timer> map;

  public BlockPlaceSpeedCheck() {
    super("BlockPlaceSpeedCheck");
    map = new HashMap<UUID, Timer>();
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    pm.registerEvents(this, w);
    MS_LIMIT = (long) w.getConfig().getInt("BLOCKPLACEVALUE");
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    Player p = event.getPlayer();
    if(!map.containsKey(p.getUniqueId())) {
      map.put(p.getUniqueId(), new Timer());
      return;
    }
    Timer t = map.get(p.getUniqueId());
    if(!t.hasReachMS(MS_LIMIT)) {
      Bukkit.getPluginManager().callEvent(new CheckFailedEvent(
          p.getUniqueId(), getRaiseLevel(), getName()
      ));
    } else {
      t.reset();
    }
  }

  @Override
  public float getRaiseLevel() {
    return 3f;
  }
}
