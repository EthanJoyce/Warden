package io.ll.warden.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.events.PlayerTrueMoveEvent;
import io.ll.warden.events.TrueBlockBreakEvent;

/**
 * Author: LordLambda
 * Featuring code from: m1enkrafftman
 * Date: 3/20/2015
 * Project: Warden
 * Usage: Some Block Utils
 */
public class BlockUtilities implements Listener {

  private static BlockUtilities instance;
  private LinkedHashMap<UUID, List<BlockBreak>> brokenBlocks;
  private LinkedHashMap<UUID, TimerWithLoc> playersBreakingNow;
  /**
   * Thanks for these lists m1enkrafftman
   */
  /**
   * Array to hold all materials harvestable by hand
   */
  private Material[] possibleWithHand = {Material.TRAP_DOOR,
                                         Material.WOOD_DOOR, Material.CHEST,
                                         Material.WORKBENCH,
                                         Material.FENCE, Material.JUKEBOX,
                                         Material.FENCE_GATE,
                                         Material.WOOD, Material.WOOD_STEP,
                                         Material.BOOKSHELF,
                                         Material.MELON, Material.PUMPKIN, Material.SIGN,
                                         Material.WOOL,
                                         Material.RAILS, Material.CLAY,
                                         Material.POWERED_RAIL,
                                         Material.SOIL, Material.GRASS, Material.GRAVEL,
                                         Material.MYCEL,
                                         Material.SPONGE, Material.CAKE_BLOCK, Material.DIRT,
                                         Material.LEVER, Material.PISTON_BASE,
                                         Material.PISTON_EXTENSION,
                                         Material.PISTON_MOVING_PIECE,
                                         Material.PISTON_STICKY_BASE,
                                         Material.SAND, Material.SOUL_SAND,
                                         Material.WOOD_PLATE,
                                         Material.CACTUS, Material.LADDER, Material.GLASS,
                                         Material.THIN_GLASS, Material.REDSTONE_LAMP_OFF,
                                         Material.REDSTONE_LAMP_ON, Material.BED_BLOCK,
                                         Material.COCOA,
                                         Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2,
                                         Material.LEAVES, Material.VINE};
  /**
   * Array to hold all materials harvestable by axes
   */
  private Material[] axeBlocks = {Material.JACK_O_LANTERN,
                                  Material.PUMPKIN, Material.BOOKSHELF, Material.CHEST,
                                  Material.WORKBENCH, Material.FENCE, Material.FENCE_GATE,
                                  Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2,
                                  Material.JUKEBOX, Material.NOTE_BLOCK, Material.SIGN,
                                  Material.TRAP_DOOR, Material.WOOD, Material.WOODEN_DOOR,
                                  Material.WOOD_PLATE, Material.WOOD_STEP,
                                  Material.WOOD_STAIRS,
                                  Material.BIRCH_WOOD_STAIRS, Material.SPRUCE_WOOD_STAIRS,
                                  Material.JUNGLE_WOOD_STAIRS};
  /**
   * Array to hold pickaxe blocks
   */
  private Material[] pickaxeBlocks = {Material.LAPIS_ORE,
                                      Material.LAPIS_BLOCK, Material.DIAMOND_ORE,
                                      Material.GOLD_ORE,
                                      Material.IRON_ORE, Material.COAL_ORE, Material.ICE,
                                      Material.EMERALD_BLOCK, Material.BREWING_STAND,
                                      Material.CAULDRON,
                                      Material.IRON_FENCE, Material.IRON_DOOR_BLOCK,
                                      Material.IRON_BLOCK,
                                      Material.DIAMOND_BLOCK, Material.GOLD_BLOCK,
                                      Material.DETECTOR_RAIL, Material.POWERED_RAIL,
                                      Material.RAILS,
                                      Material.BRICK, Material.COAL_ORE,
                                      Material.COBBLESTONE,
                                      Material.DISPENSER, Material.ENCHANTMENT_TABLE,
                                      Material.ENDER_STONE, Material.ENDER_CHEST,
                                      Material.FURNACE,
                                      Material.MOB_SPAWNER, Material.MOSSY_COBBLESTONE,
                                      Material.NETHER_BRICK, Material.NETHER_FENCE,
                                      Material.NETHERRACK,
                                      Material.SANDSTONE, Material.STEP,
                                      Material.COBBLESTONE_STAIRS,
                                      Material.BRICK_STAIRS, Material.SANDSTONE_STAIRS,
                                      Material.STONE,
                                      Material.SMOOTH_BRICK, Material.SMOOTH_STAIRS,
                                      Material.STONE_PLATE};
  /**
   * Array to hold shear blocks
   */
  private Material[] shearBlocks = {Material.LEAVES, Material.WEB,
                                    Material.WOOL};
  /**
   * Array to hold shovel blocks
   */
  private Material[] shovelBlocks = {Material.CLAY, Material.DIRT,
                                     Material.SOIL, Material.GRASS, Material.GRAVEL,
                                     Material.MYCEL,
                                     Material.SAND, Material.SOUL_SAND, Material.SNOW,
                                     Material.SNOW_BLOCK};

  protected BlockUtilities() {
    brokenBlocks = new LinkedHashMap<UUID, List<BlockBreak>>();
    playersBreakingNow = new LinkedHashMap<UUID, TimerWithLoc>();
  }

  /**
   * Get the current instance of BlockUtils
   *
   * @return The singleton instance of BlockUtils.
   */
  public static BlockUtilities get() {
    if (instance == null) {
      synchronized (BlockUtilities.class) {
        if (instance == null) {
          instance = new BlockUtilities();
        }
      }
    }
    return instance;
  }

  /**
   * Turn a degree of rotation into a direction name
   *
   * @param rot How far the player has rotated
   * @return The direction the player is facing.
   */
  public static String getDirection(double rot) {
    if (0 <= rot && rot < 22.5) {
      return "North";
    } else if (22.5 <= rot && rot < 67.5) {
      return "Northeast";
    } else if (67.5 <= rot && rot < 112.5) {
      return "East";
    } else if (112.5 <= rot && rot < 157.5) {
      return "Southeast";
    } else if (157.5 <= rot && rot < 202.5) {
      return "South";
    } else if (202.5 <= rot && rot < 247.5) {
      return "Southwest";
    } else if (247.5 <= rot && rot < 292.5) {
      return "West";
    } else if (292.5 <= rot && rot < 337.5) {
      return "Northwest";
    } else if (337.5 <= rot && rot < 360.0) {
      return "North";
    } else {
      return null;
    }
  }

  /**
   * Get the players Latest Break Timer
   *
   * @param u The player
   * @return His latest break time.
   */
  public Timer getPlayerNBlockBreakTimer(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    if (breaks != null) {
      return breaks.get(breaks.size()).getTwl();
    }
    return null;
  }

  /**
   * Get the players Latest Break TimerWithLoc
   *
   * @param u The player
   * @return His latest break time with location.
   */
  public TimerWithLoc getPlayerNBlockBreakTWL(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    if (breaks != null) {
      return breaks.get(breaks.size()).getTwl();
    }
    return null;
  }

  /**
   * Get the players Second to Latest Break Timer
   *
   * @param u The player
   * @return His sencond to latest break time.
   */
  public Timer getPlayerNMinusOneBlockBreakTimer(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null ? null : breaks.get(breaks.size() - 1).getTwl();
  }

  /**
   * Get the players second to Latest Break Timer with location
   *
   * @param u The player
   * @return His second to latest break time with location.
   */
  public TimerWithLoc getPlayerNMinusOneBlockBreakTWL(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null ? null : breaks.get(breaks.size() - 1).getTwl();
  }

  /**
   * Get a specific break timer
   *
   * @param u The player
   * @param i The break timer to get
   * @return the specified break timer
   */
  public Timer getPlayerBlockBreakTimer(UUID u, int i) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null || breaks.size() < i ? null : breaks.get(i).getTwl();
  }

  /**
   * Get a specific break timer with location
   *
   * @param u The player
   * @param i The break timer to get
   * @return the specified break timer with location
   */
  public TimerWithLoc getPlayerBlockBreakTWL(UUID u, int i) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null || breaks.size() < i ? null : breaks.get(i).getTwl();
  }

  /**
   * Get the players latest block break
   *
   * @param u The player
   * @return The players latest block break.
   */
  public BlockBreak getPlayerNBlockBreak(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null ? null : breaks.get(breaks.size());
  }

  /**
   * Get the second to latest block break.
   *
   * @param u The player.
   * @return The second to latest block break.
   */
  public BlockBreak getPlayerNMinusOneBlockBreak(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null ? null : breaks.get(breaks.size() - 1);
  }

  /**
   * Get a specified block break
   *
   * @param u The player
   * @param i The specific block break
   * @return The specified block break.
   */
  public BlockBreak getPlayerBlockBreak(UUID u, int i) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null || breaks.size() < i ? null : breaks.get(i);
  }

  /**
   * Get the total amount of blocks broken by a player
   *
   * @param u The player
   * @return The total amount of blocks broken by a player.
   */
  public long getNumberOfBlocksBrokenByPlayer(UUID u) {
    List<BlockBreak> blockBreaks = brokenBlocks.get(u);
    return blockBreaks == null ? -1 : blockBreaks.size();
  }

  /**
   * Check if a player is breaking a block
   *
   * @param u The player
   * @return If said player is breaking a block.
   */
  public boolean playersBreakingBlock(UUID u) {
    return playersBreakingNow.containsKey(u);
  }

  /**
   * Setup block utilites
   *
   * @param w An instance of warden.
   */
  public void setup(Warden w) {
    Bukkit.getServer().getPluginManager().registerEvents(this, w);
  }

  @EventHandler
  public void onTrueMove(PlayerTrueMoveEvent event) {
    Player p = event.getPlayer();
    if (playersBreakingBlock(p.getUniqueId())) {
      Location block = playersBreakingNow.get(p.getUniqueId()).getLoc();

      Location player = MovementHelper.get().getPlayerNLocation(p.getUniqueId());

      int reachDistance = (p.getGameMode() == GameMode.CREATIVE) ? 5 : 4;

      if (MathHelper.getDistance3D(block, player) > reachDistance) {
        playersBreakingNow.remove(p.getUniqueId());
      }
    }
    new Thread() {
      @Override
      public void run() {
        for (UUID u : playersBreakingNow.keySet()) {
          if (playersBreakingNow.get(u).hasReach(0.5f)) {
            playersBreakingNow.remove(u);
          }
        }
      }
    }.run();
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Player p = event.getPlayer();
    if (!playersBreakingNow.containsKey(p.getUniqueId())
        && !(p.getGameMode() == GameMode.CREATIVE)) {
      event.setCancelled(true);
    }
    TimerWithLoc twl = playersBreakingNow.get(p.getUniqueId());
    playersBreakingNow.remove(p.getUniqueId());
    twl.stop();
    List<BlockBreak> brokenBlocksForPlayer = brokenBlocks.get(p.getUniqueId());
    Material itemType = p.getItemInHand().getType();
    Material blockType = event.getBlock().getType();
    BlockBreak bb = new BlockBreak(twl, itemType, blockType);
    Bukkit.getServer().getPluginManager().callEvent(new TrueBlockBreakEvent(
        p, bb
    ));
    brokenBlocksForPlayer.add(bb);
    brokenBlocks.put(p.getUniqueId(), brokenBlocksForPlayer);
  }

  @EventHandler
  public void onBlockHit(BlockDamageEvent event) {
    Player p = event.getPlayer();
    if (!playersBreakingNow.containsKey(p.getUniqueId())) {
      playersBreakingNow.put(p.getUniqueId(), new TimerWithLoc(event.getBlock().getLocation()));
    }
  }

  /**
   * Get the location of the closest ground block realtive to the player
   *
   * @param p The player
   * @return The closest ground block relative to the player.
   */
  public Location getClosestGroundBlockToPlayer(Player p) {
    Location highestBlock = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation();

    if (highestBlock == null) {
      return new Location(p.getWorld(), -1, -1, -1);
    }

    if (p.getLocation().getY() >= highestBlock.getY()) {
      return highestBlock;
    }

    for (int i = 0; i < p.getLocation().getY(); i++) {
      if (!p.getWorld().getBlockAt(p.getLocation().subtract(0, i, 0)).isEmpty()) {
        return p.getLocation().subtract(0, i - 1, 0);
      }
    }

    return new Location(p.getWorld(), -1, -1, -1);
  }

  /**
   * Check if a block is a fence
   *
   * @param b The block to check
   * @return If the block is a fence
   */
  public boolean isFence(Block b) {
    return b.getType().name().contains("FENCE");
  }

  /**
   * Check if a block is a web
   *
   * @param b The block to check
   * @return If it's a web.
   */
  public boolean isWeb(Block b) {
    return b.getType() == Material.WEB;
  }

  /**
   * Check if an item is a shovel
   *
   * @param is The item
   * @return If the item is a shovel.
   */
  public boolean isShovel(ItemStack is) {
    Material mat = is.getType();
    return mat == Material.WOOD_SPADE
           || mat == Material.STONE_SPADE
           || mat == Material.IRON_SPADE
           || mat == Material.GOLD_SPADE
           || mat == Material.DIAMOND_SPADE;
  }

  /**
   * Check if an item is a shear
   *
   * @param is The item
   * @return If the item is a shear.
   */
  public boolean isShears(ItemStack is) {
    return is.getType() == Material.SHEARS;
  }

  /**
   * Check if an item is a sword
   *
   * @param is The item
   * @return If the item is a sword.
   */
  public boolean isSword(ItemStack is) {
    Material mat = is.getType();
    return mat == Material.WOOD_SWORD
           || mat == Material.STONE_SWORD
           || mat == Material.IRON_SWORD
           || mat == Material.GOLD_SWORD
           || mat == Material.DIAMOND_SWORD;
  }

  /**
   * Check if an item is a pickaxe
   *
   * @param is The item
   * @return If the item is a pickaxe.
   */
  public boolean isPick(ItemStack is) {
    Material mat = is.getType();
    return mat == Material.WOOD_PICKAXE
           || mat == Material.STONE_PICKAXE
           || mat == Material.IRON_PICKAXE
           || mat == Material.GOLD_PICKAXE
           || mat == Material.DIAMOND_PICKAXE;
  }

  /**
   * Check if an item is an axe
   *
   * @param is The item
   * @return If the item is an axe.
   */
  public boolean isAxe(ItemStack is) {
    Material mat = is.getType();
    return mat == Material.WOOD_AXE
           || mat == Material.STONE_AXE
           || mat == Material.IRON_AXE
           || mat == Material.GOLD_AXE
           || mat == Material.DIAMOND_AXE;
  }

  /**
   * A function written by: m1enkrafftman
   *
   * @param item The item that broke the block
   * @param mat  The material that was broken
   * @return If the item that broke the material was a quality tool
   */
  public boolean isQualityTool(ItemStack item, Material mat) {
    if (isAxe(item)) {
      for (Material i : axeBlocks) {
        if (i == mat) {
          return true;
        }
      }
    }
    if (isPick(item)) {
      for (Material i : pickaxeBlocks) {
        if (i == mat) {
          return true;
        }
      }
    }
    if (isShears(item)) {
      for (Material i : shearBlocks) {
        if (i == mat) {
          return true;
        }
      }
    }
    if (isShovel(item)) {
      for (Material i : shovelBlocks) {
        if (i == mat) {
          return true;
        }
      }
    }
    if (isSword(item)) {
      return true;
    }
    return false;
  }

  /**
   * A function again written by: m1enkrafftman
   *
   * @param enchantLevel The enchantment level
   * @return Calculates effeciency.
   */
  public int getPercentAtEffLevel(int enchantLevel) {
    switch (enchantLevel) {
      case 1:
        return 30;
      case 2:
        return 39;
      case 3:
        return 51;
      case 4:
        return 66;
      case 5:
        return 85;
      default:
        return 100;
    }
  }

  /**
   * Check if a certain block is a stair block
   *
   * @param block The block to check
   * @return If the block is a type of stair.
   */
  public boolean isStair(Block block) {
    return block.getType().name().contains("STEP")
           || block.getType().name().contains("STAIR");
  }

  /**
   * Check if you can break a block with your hand
   *
   * @param block The block to check
   * @return If it's possible to break with your hand.
   */
  public boolean possibleBreakWithHand(Block block) {
    for (Material i : possibleWithHand) {
      if (block.getType() == i) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if a block is snow
   *
   * @param b the block to check
   * @return If it's snow.
   */
  public boolean isSnow(Block b) {
    return b.getType() == Material.SNOW
           || b.getType() == Material.SNOW_BLOCK;
  }

  /**
   * Check if a block is ice
   *
   * @param b the block to check
   * @return If it's ice.
   */
  public boolean isIce(Block b) {
    return b.getType() == Material.ICE
           || b.getType() == Material.PACKED_ICE;
  }

  /**
   * Check if a block can be climbed
   *
   * @param b The block to check
   * @return If it can be climbed
   */
  public boolean isClimable(Block b) {
    return b.getType() == Material.VINE ||
           b.getType() == Material.LADDER;
  }

  /**
   * Check if a block is a lilly
   *
   * @param b the block to check
   * @return If it's a lilly.
   */
  public boolean isLilly(Block b) {
    return b.getType() == Material.WATER_LILY;
  }

  /**
   * Check if a player is in a web
   *
   * @param p The player to check
   * @return If the player is in a web.
   */
  public boolean isPlayerInWeb(Player p) {
    Block feetBlock = p.getWorld().getBlockAt(p.getLocation());
    Block eyeBlock = p.getWorld().getBlockAt(
        p.getLocation().add(0, p.getEyeHeight(), 0));
    return isWeb(feetBlock) || isWeb(eyeBlock);
  }

  /**
   * Check if the player is in water
   *
   * @param p The player to check
   * @return If they're in water.
   */
  public boolean isPlayerInLiquid(Player p) {
    Location pLoc = p.getLocation();
    Location under = pLoc.add(0, -0.1, 0);
    Location eye = pLoc.add(0, p.getEyeHeight(), 0);
    Block underBlock = p.getWorld().getBlockAt(under);
    Block feetBlock = p.getWorld().getBlockAt(pLoc);
    Block eyeBlock = p.getWorld().getBlockAt(eye);
    return (underBlock.isLiquid() || feetBlock.isLiquid() || eyeBlock.isLiquid());
  }

  /**
   * Check if a player is on a lilly
   *
   * @param p The player to check
   * @return If the player is on a lilly.
   */
  public boolean isOnLily(Player p) {
    Block block = p.getLocation().getBlock();
    Block blockLower = p.getLocation().subtract(0, 0.1, 0).add(0.5, 0, 0).getBlock();
    Block blockLowest = p.getLocation().subtract(0, 0.2, 0).add(0.5, 0, 0).getBlock();
    return (isLilly(block) || isLilly(blockLower) || isLilly(blockLowest)
            || isLilly(block.getRelative(BlockFace.NORTH))
            || isLilly(block.getRelative(BlockFace.SOUTH))
            || isLilly(block.getRelative(BlockFace.EAST))
            || isLilly(block.getRelative(BlockFace.WEST))
            || isLilly(block.getRelative(BlockFace.SOUTH_WEST))
            || isLilly(block.getRelative(BlockFace.NORTH_WEST))
            || isLilly(block.getRelative(BlockFace.SOUTH_EAST))
            || isLilly(block.getRelative(BlockFace.NORTH_EAST)))
           || isLilly(blockLower.getRelative(BlockFace.NORTH))
           || isLilly(blockLower.getRelative(BlockFace.SOUTH))
           || isLilly(blockLower.getRelative(BlockFace.EAST))
           || isLilly(blockLower.getRelative(BlockFace.WEST))
           || isLilly(blockLower.getRelative(BlockFace.SOUTH_WEST))
           || isLilly(blockLower.getRelative(BlockFace.NORTH_WEST))
           || isLilly(blockLower.getRelative(BlockFace.SOUTH_EAST))
           || isLilly(blockLower.getRelative(BlockFace.NORTH_EAST))
           || isLilly(blockLowest.getRelative(BlockFace.NORTH))
           || isLilly(blockLowest.getRelative(BlockFace.SOUTH))
           || isLilly(blockLowest.getRelative(BlockFace.EAST))
           || isLilly(blockLowest.getRelative(BlockFace.WEST))
           || isLilly(blockLowest.getRelative(BlockFace.SOUTH_WEST))
           || isLilly(blockLowest.getRelative(BlockFace.NORTH_WEST))
           || isLilly(blockLowest.getRelative(BlockFace.SOUTH_EAST))
           || isLilly(blockLowest.getRelative(BlockFace.NORTH_EAST));

  }

  /**
   * Check if the player is climbing
   *
   * @param p The player to check
   * @return If said player is climbing
   */
  public boolean isClimbing(Player p) {
    Block block = p.getLocation().getBlock();
    return (isClimable(block)
            || isClimable(block.getRelative(BlockFace.NORTH))
            || isClimable(block.getRelative(BlockFace.SOUTH))
            || isClimable(block.getRelative(BlockFace.EAST))
            || isClimable(block.getRelative(BlockFace.WEST))
            || isClimable(block.getRelative(BlockFace.SOUTH_WEST))
            || isClimable(block.getRelative(BlockFace.NORTH_WEST))
            || isClimable(block.getRelative(BlockFace.SOUTH_EAST))
            || isClimable(block.getRelative(BlockFace.NORTH_EAST)));
  }

  /**
   * Check if a player is on snow
   *
   * @param p The player to check
   * @return If said player is on snow.
   */
  public boolean isOnSnow(Player p) {
    Block block = p.getLocation().getBlock();
    Block blockLower = p.getLocation().subtract(0, 0.1, 0).add(0.5, 0, 0).getBlock();
    Block blockLowest = p.getLocation().subtract(0, 0.2, 0).add(0.5, 0, 0).getBlock();
    return (isSnow(block) || isSnow(blockLower) || isSnow(blockLowest)
            || isSnow(block.getRelative(BlockFace.NORTH))
            || isSnow(block.getRelative(BlockFace.SOUTH))
            || isSnow(block.getRelative(BlockFace.EAST))
            || isSnow(block.getRelative(BlockFace.WEST))
            || isSnow(block.getRelative(BlockFace.SOUTH_WEST))
            || isSnow(block.getRelative(BlockFace.NORTH_WEST))
            || isSnow(block.getRelative(BlockFace.SOUTH_EAST))
            || isSnow(block.getRelative(BlockFace.NORTH_EAST)))
           || isSnow(blockLower.getRelative(BlockFace.NORTH))
           || isSnow(blockLower.getRelative(BlockFace.SOUTH))
           || isSnow(blockLower.getRelative(BlockFace.EAST))
           || isSnow(blockLower.getRelative(BlockFace.WEST))
           || isSnow(blockLower.getRelative(BlockFace.SOUTH_WEST))
           || isSnow(blockLower.getRelative(BlockFace.NORTH_WEST))
           || isSnow(blockLower.getRelative(BlockFace.SOUTH_EAST))
           || isSnow(blockLower.getRelative(BlockFace.NORTH_EAST))
           || isSnow(blockLowest.getRelative(BlockFace.NORTH))
           || isSnow(blockLowest.getRelative(BlockFace.SOUTH))
           || isSnow(blockLowest.getRelative(BlockFace.EAST))
           || isSnow(blockLowest.getRelative(BlockFace.WEST))
           || isSnow(blockLowest.getRelative(BlockFace.SOUTH_WEST))
           || isSnow(blockLowest.getRelative(BlockFace.NORTH_WEST))
           || isSnow(blockLowest.getRelative(BlockFace.SOUTH_EAST))
           || isSnow(blockLowest.getRelative(BlockFace.NORTH_EAST));
  }

  /**
   * Check if a player is on ice.
   *
   * @param p The player
   * @return If they're on ice.
   */
  public boolean isOnIce(Player p) {
    Block block = p.getLocation().getBlock();
    Block blockLower = p.getLocation().subtract(0, 0.1, 0).add(0.5, 0, 0).getBlock();
    Block blockLowest = p.getLocation().subtract(0, 0.2, 0).add(0.5, 0, 0).getBlock();
    return (isIce(block) || isIce(blockLower) || isIce(blockLowest)
            || isIce(block.getRelative(BlockFace.NORTH))
            || isIce(block.getRelative(BlockFace.SOUTH))
            || isIce(block.getRelative(BlockFace.EAST))
            || isIce(block.getRelative(BlockFace.WEST))
            || isIce(block.getRelative(BlockFace.SOUTH_WEST))
            || isIce(block.getRelative(BlockFace.NORTH_WEST))
            || isIce(block.getRelative(BlockFace.SOUTH_EAST))
            || isIce(block.getRelative(BlockFace.NORTH_EAST)))
           || isIce(blockLower.getRelative(BlockFace.NORTH))
           || isIce(blockLower.getRelative(BlockFace.SOUTH))
           || isIce(blockLower.getRelative(BlockFace.EAST))
           || isIce(blockLower.getRelative(BlockFace.WEST))
           || isIce(blockLower.getRelative(BlockFace.SOUTH_WEST))
           || isIce(blockLower.getRelative(BlockFace.NORTH_WEST))
           || isIce(blockLower.getRelative(BlockFace.SOUTH_EAST))
           || isIce(blockLower.getRelative(BlockFace.NORTH_EAST))
           || isIce(blockLowest.getRelative(BlockFace.NORTH))
           || isIce(blockLowest.getRelative(BlockFace.SOUTH))
           || isIce(blockLowest.getRelative(BlockFace.EAST))
           || isIce(blockLowest.getRelative(BlockFace.WEST))
           || isIce(blockLowest.getRelative(BlockFace.SOUTH_WEST))
           || isIce(blockLowest.getRelative(BlockFace.NORTH_WEST))
           || isIce(blockLowest.getRelative(BlockFace.SOUTH_EAST))
           || isIce(blockLowest.getRelative(BlockFace.NORTH_EAST));

  }

  /**
   * A function again written by: m1enkrafftman
   *
   * @param p  The player
   * @param x  The x of the block
   * @param y  The y of the block
   * @param z  The z of the block
   * @param id The ID of the block
   * @return If the block is able to be passed through.
   */
  public boolean isPassable(Player p, double x, double y, double z, int id) {
    final int bx = Location.locToBlock(x);
    final int by = Location.locToBlock(y);
    final int bz = Location.locToBlock(z);
    final Block craftBlock = p.getWorld().getBlockAt((int) x, (int) y, (int) z);
    if (craftBlock == null) {
      return true;
    }
    final double fx = x - bx;
    final double fy = y - by;
    final double fz = z - bz;
    if (craftBlock.isLiquid() || craftBlock.isEmpty() || isOnLily(p) || isClimbing(p) || isOnSnow(
        p)) {
      return true;
    } else {
      if (isAboveStairs(p)) {
        if (fy < 0.5) {
          return true;
        } else if (fy >= 0.5) {
          return true;
        } else if (id == Material.WOODEN_DOOR.getId()) {
          return true;
        } else if (id == Material.IRON_DOOR_BLOCK.getId()) {
          return true;
        } else if (p.getLocation().getBlock().getRelative(BlockFace.NORTH).getType()
                   == Material.WOODEN_DOOR) {
          return false;
        } else if (p.getLocation().getBlock().getRelative(BlockFace.NORTH).getType()
                   == Material.IRON_DOOR_BLOCK) {
          return false;
        } else if (id == Material.SOUL_SAND.getId() && fy >= 0.875) {
          return true; // 0.125
        } else if (id == Material.SAND.getId() && fy >= 0.975) {
          return true; // 0.025
        } else if (id == Material.IRON_FENCE.getId() || id == Material.THIN_GLASS.getId()) {
          if (Math.abs(0.5 - fx) > 0.05 && Math.abs(0.5 - fz) > 0.05) {
            return true;
          } else if (id == Material.FENCE.getId() || id == Material.NETHER_FENCE.getId()) {
            if (Math.abs(0.2 - fx) > 0.02 && Math.abs(0.2 - fz) > 0.02) {
              return true;
            } else if (id == Material.FENCE_GATE.getId()) {
              return true;
            } else if (id == Material.CAKE_BLOCK.getId() && fy >= 0.4375) {
              return true;
            } else if (id == Material.CAULDRON.getId()) {
              if (Math.abs(0.5 - fx) < 0.1 && Math.abs(0.5 - fz) < 0.1 && fy > 0.1) {
                return true;
              }
            } else if (id == Material.WATER.getId()) {
              return true;
            } else if (id == Material.LADDER.getId()) {
              return true;
            } else if (id == Material.VINE.getId()) {
              return true;
            } else if (id == Material.WATER_LILY.getId()) {
              return true;
            } else if (id == Material.SNOW.getId()) {
              return true;
            } else if (id == Material.AIR.getId()) {
              return true;
            } else if (id == Material.CACTUS.getId() && fy >= 0.9375) {
              return true;
            }
          }
        }
      }
      return false;
    }
  }

  /**
   * Check if a player is above stairs
   *
   * @param p The player to check
   * @return If the player is above stairs.
   */
  public boolean isAboveStairs(Player p) {
    final Block block = p.getLocation().getBlock();
    final Block altBlock = p.getLocation().add(0, 0.5, 0).getBlock();
    return (BlockUtilities.get().isStair(block.getRelative(BlockFace.NORTH))
            || BlockUtilities.get().isStair(block.getRelative(BlockFace.SOUTH))
            || BlockUtilities.get().isStair(block.getRelative(BlockFace.EAST))
            || BlockUtilities.get().isStair(block.getRelative(BlockFace.WEST))
            || BlockUtilities.get().isStair(block.getRelative(BlockFace.SOUTH_WEST))
            || BlockUtilities.get().isStair(block.getRelative(BlockFace.NORTH_WEST))
            || BlockUtilities.get().isStair(block.getRelative(BlockFace.SOUTH_EAST))
            || BlockUtilities.get().isStair(block.getRelative(BlockFace.NORTH_EAST))
            || BlockUtilities.get().isStair(altBlock.getRelative(BlockFace.NORTH))
            || BlockUtilities.get().isStair(altBlock.getRelative(BlockFace.SOUTH))
            || BlockUtilities.get().isStair(altBlock.getRelative(BlockFace.EAST))
            || BlockUtilities.get().isStair(altBlock.getRelative(BlockFace.WEST))
            || BlockUtilities.get().isStair(altBlock.getRelative(BlockFace.SOUTH_WEST))
            || BlockUtilities.get().isStair(altBlock.getRelative(BlockFace.NORTH_WEST))
            || BlockUtilities.get().isStair(altBlock.getRelative(BlockFace.SOUTH_EAST))
            || BlockUtilities.get().isStair(altBlock.getRelative(BlockFace.NORTH_EAST)));
  }
}
