package io.ll.warden.check.checks.movement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.LinkedHashMap;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;
import io.ll.warden.events.CheckFailedEvent;
import io.ll.warden.events.PlayerTrueMoveEvent;
import io.ll.warden.utils.BlockUtilities;
import io.ll.warden.utils.MathHelper;
import io.ll.warden.utils.MovementHelper;

/**
 * Author: LordLambda
 * Date: 3/19/2015
 * Project: Warden
 * Usage: A sprint check.
 *
 * Sprint checks are hard. Because normal people can sprint too! Sprint is literally just sprinting
 * all the time. So what do we do? We make it a super low check. Then we just check for people
 * sprinting while they're standing still, or why they're swimming , etc.
 */
public class SprintCheck extends Check implements Listener {

  private LinkedHashMap<UUID, Location> sprintingPlayers;

  public SprintCheck() {
    super("SprintCheck");
    sprintingPlayers = new LinkedHashMap<UUID, Location>();
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    pm.registerEvents(this, w);
  }

  @EventHandler
  public void onMove(PlayerTrueMoveEvent event) {
    UUID u = event.getPlayer().getUniqueId();
    MovementHelper mh = MovementHelper.get();
    if (mh.isFalling(u) && mh.isSprinting(u)) {
      Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
          u, getRaiseLevel(), getName()
      ));
      return;
    }
    if (mh.isSprinting(u)) {
      if (!sprintingPlayers.containsKey(u)) {
        sprintingPlayers.put(u, mh.getPlayerNLocation(u));
        return;
      }
      Location l = sprintingPlayers.get(u);
      if (MathHelper.getDistance3D(l, mh.getPlayerNLocation(u)) < 1) {
        Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
            u, getRaiseLevel(), getName()
        ));
      }
      //TODO: Check if player is sprinting into water, this can cause them to sprint in water
      if(BlockUtilities.get().isPlayerInLiquid(event.getPlayer())) {
        Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
            u, getRaiseLevel(), getName()
        ));
      }
    }
  }

  @Override
  public float getRaiseLevel() {
    return 1.1f;
  }

}
