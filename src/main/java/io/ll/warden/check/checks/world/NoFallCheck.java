package io.ll.warden.check.checks.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;
import io.ll.warden.events.CheckFailedEvent;
import io.ll.warden.events.FallEvent;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: Make sure a player has recieved Fall Damage.
 *
 * No fall is pretty self explanatory on how it works. You basically just cancel out
 * recieving fall damage. Here we should check if the player should've recieved fall damage
 * and make sure that they do. Here I also cancel Spigot/Bukkit from doing fall damage,
 * so that way only the check is doing fall damage. So instead of throwing violations
 * we just apply appropriate fall damage.
 */
public class NoFallCheck extends Check implements Listener {

  public NoFallCheck() {
    super("NoFallCheck");
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    pm.registerEvents(this, w);
  }

  @EventHandler
  public void onFall(FallEvent event) {
    Player p = event.getPlayer();
    UUID u = p.getUniqueId();
    if (shouldCheckPlayer(u)) {
      Location start = event.getStart();
      Location finish = event.getFinish();
      //First check if they're trying to do the NCP exploit by falling inside the ground.
      //This is done by checking if the block above finish is air.
      Location oneAboveFinish = new Location(finish.getWorld(), finish.getX(),
                                             finish.getY() + 1, finish.getZ());
      if (oneAboveFinish.getBlock() != null || oneAboveFinish.getBlock().getType() != Material.AIR
          ||
          !oneAboveFinish.getBlock().isEmpty()) {
        Bukkit.getPluginManager().callEvent(new CheckFailedEvent(
            u, getRaiseLevel(), getName()
        ));
      }
      double totalDistance = Math.abs(start.getY() - finish.getY());
      if (totalDistance > 3) {
        //As of 1.6 the only things that stop you from taking fall damage is deep water (3 blocks)
        //or ladders. So check in a 3 block upwards radius, as well as finish for those.
        if (!shouldMitigateFallDamage(finish)) {
          double fallDamage = calcFallDamage(totalDistance);
          p.setHealth(p.getHealth() - fallDamage);
        }
      }
    }
  }

  @EventHandler
  public void onServerExecuteFallDamage(EntityDamageByBlockEvent event) {
    if (event.getEntity() instanceof Player) {
      Player p = (Player) event.getEntity();
      if (shouldCheckPlayer(p.getUniqueId())) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
          event.setCancelled(true);
        }
      }
    }
  }

  private double calcFallDamage(double totalFall) {
    //Falling Damage =(number of blocks fallen x 0.5) - 1.5.
    return Math.abs(Math.abs(totalFall * 0.5d) - 1.5d);
  }

  private boolean shouldMitigateFallDamage(Location finish) {
    if (isLORW(finish)) {
      return true;
    }
    //Have to check 6 blocks above finish for a continuous 3 blocks of water.
    //Cause if the blocks at 6, 5, and 4 are water/ladders than the player only
    //fell three blocks of actual calculating fall damage so they would recieve
    //no damage.
    Location oneAboveFinish = new Location(finish.getWorld(), finish.getX(),
                                           finish.getY() + 1, finish.getZ());
    Location twoAboveFinish = new Location(finish.getWorld(), finish.getX(),
                                           finish.getY() + 2, finish.getZ());
    Location threeAboveFinish = new Location(finish.getWorld(), finish.getX(),
                                             finish.getY() + 3, finish.getZ());
    Location fourAboveFinish = new Location(finish.getWorld(), finish.getX(),
                                            finish.getY() + 4, finish.getZ());
    Location fiveAboveFinish = new Location(finish.getWorld(), finish.getX(),
                                            finish.getY() + 5, finish.getZ());
    Location sixAboveFinish = new Location(finish.getWorld(), finish.getX(),
                                           finish.getY() + 6, finish.getZ());
    //Time to do a super long if checking for three consecutive ladder/waters.
    if ((isLORW(oneAboveFinish) && isLORW(twoAboveFinish) && isLORW(threeAboveFinish))
        || (isLORW(twoAboveFinish) && isLORW(threeAboveFinish) && isLORW(fourAboveFinish))
        || (isLORW(threeAboveFinish) && isLORW(fourAboveFinish) && isLORW(fiveAboveFinish))
        || (isLORW(fourAboveFinish) && isLORW(fiveAboveFinish) && isLORW(sixAboveFinish))) {
      return true;
    }
    return false;
  }

  /**
   * Checks if a location is a ladder or water
   *
   * @return if the location is ladder or water.
   */
  private boolean isLORW(Location loc) {
    if (loc.getBlock().getType() == Material.WATER || loc.getBlock().getType() ==
                                                      Material.STATIONARY_WATER
        || loc.getBlock().getType() == Material.LADDER ||
        loc.getBlock().getType() == Material.VINE) {
      return true;
    }
    return false;
  }

  @Override
  public float getRaiseLevel() {
    return 4f;
  }
}
