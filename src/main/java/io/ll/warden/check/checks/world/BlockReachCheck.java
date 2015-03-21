package io.ll.warden.check.checks.world;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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
 * Usage: A simple block reach check
 */
public class BlockReachCheck extends Check implements Listener {

  public BlockReachCheck() {
    super("BlockReachCheck");
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    pm.registerEvents(this, w);
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    double down = MathHelper.getDistance3D(event.getBlock().getLocation(),
                                           MovementHelper.get().getPlayerNLocation(
                                               event.getPlayer().getUniqueId()
                                           ));
    if (down > getReachDistance(event.getPlayer())) {
      Bukkit.getPluginManager().callEvent(
          new CheckFailedEvent(event.getPlayer().getUniqueId(),
                               getRaiseLevel(), getName())
      );
    }
  }

  public double getReachDistance(Player p) {
    return (p.getGameMode() == GameMode.CREATIVE ? 7.5 : 6.5);
  }

  @Override
  public float getRaiseLevel() {
    return 2.5f;
  }
}
