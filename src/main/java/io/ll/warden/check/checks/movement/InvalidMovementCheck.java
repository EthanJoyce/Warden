package io.ll.warden.check.checks.movement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;
import io.ll.warden.events.CheckFailedEvent;
import io.ll.warden.events.PlayerTrueMoveEvent;
import io.ll.warden.utils.BlockUtilities;
import io.ll.warden.utils.MovementHelper;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: An invalid movement check.
 *
 * This check checks for 2 things.
 * 1. If the player is sprinting/sneaking at the same time. Something impossible.
 * 2. If the player is phasing through an invalid block.
 *
 * How the checks work:
 * 1. Simply query movement helper to check if the player is attempting to sprint/sneak at
 * the same time.
 * 2. Check if the player is inside a block.
 */
public class InvalidMovementCheck extends Check implements Listener {

  public InvalidMovementCheck() {
    super("InvalidMovementCheck");
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    pm.registerEvents(this, w);
  }

  @Override
  public float getRaiseLevel() {
    return 4.5f;
  }

  @EventHandler
  public void onTrueMove(PlayerTrueMoveEvent event) {
    Player p = event.getPlayer();
    MovementHelper mh = MovementHelper.get();
    if (shouldCheckPlayer(p.getUniqueId())) {
      //Sprint + Sneak check
      if (mh.isSprinting(p.getUniqueId()) && p.isSneaking()) {
        Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
            p.getUniqueId(), getRaiseLevel(), getName()
        ));
      }
      //Inside block check.
      Location to = mh.getPlayerNLocation(p.getUniqueId());
      if (!isPassable(p, to.getBlockX(), to.getBlockY(), to.getBlockZ(),
                      to.getBlock().getType().getId())) {
        Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
            p.getUniqueId(), getRaiseLevel(), getName()
        ));
      }
    }
  }

  /**
   * A slightly modified version of a function from: m1enkraftman
   *
   * @return If a block is passable
   */
  private boolean isPassable(Player player, double x, double y, double z, int id) {
    final int bx = Location.locToBlock(x);
    final int by = Location.locToBlock(y);
    final int bz = Location.locToBlock(z);
    final Block craftBlock = player.getWorld().getBlockAt((int) x, (int) y, (int) z);
    if (craftBlock == null) {
      return true;
    }
    double fx = x - bx;
    double fy = y - by;
    double fz = z - bz;
    BlockUtilities bu = BlockUtilities.get();
    if (craftBlock.isLiquid() || craftBlock.isEmpty() || bu.isOnLily(player)
        || bu.isClimbing(player) || bu.isOnSnow(player)) {
      return true;
    }
    if (bu.isAboveStairs(player)) {
      return true;
    }
    if (id == Material.WOODEN_DOOR.getId() || id == Material.IRON_DOOR_BLOCK.getId()) {
      return true;
    }
    if (player.getLocation().getBlock().getRelative(BlockFace.NORTH).getType()
        == Material.WOODEN_DOOR
        || player.getLocation().getBlock().getRelative(BlockFace.NORTH).getType()
           == Material.IRON_DOOR_BLOCK) {
      return false;
    }
    if (id == Material.SOUL_SAND.getId() && fy >= 0.875) {
      return true;
    }
    if (id == Material.SAND.getId() && fy >= 0.975) {
      return true;
    }
    if (id == Material.IRON_FENCE.getId() || id == Material.THIN_GLASS.getId()) {
      if (Math.abs(0.5 - fx) > 0.05 && Math.abs(0.5 - fz) > 0.05) {
        return true;
      }
    }
    if (id == Material.FENCE.getId() || id == Material.NETHER_FENCE.getId()) {
      if (Math.abs(0.2 - fx) > 0.02 && Math.abs(0.2 - fz) > 0.02) {
        return true;
      }
    }
    if (id == Material.FENCE_GATE.getId()) {
      return true;
    }
    if (id == Material.CAKE_BLOCK.getId() && fy >= 0.4375) {
      return true;
    }
    if (id == Material.CAULDRON.getId()) {
      if (Math.abs(0.5 - fx) < 0.1 && Math.abs(0.5 - fz) < 0.1 && fy > 0.1) {
        return true;
      }
    }
    if (id == Material.WATER.getId() || id == Material.LADDER.getId() || id == Material.VINE.getId()
        || id == Material.WATER_LILY.getId() || id == Material.SNOW.getId() ||
        id == Material.AIR.getId()) {
      return true;
    }
    if (id == Material.CACTUS.getId()) {
      return fy >= 0.9375;
    }
    return false;
  }
}
