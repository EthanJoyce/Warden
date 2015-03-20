package io.ll.warden.utils;

import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Author: LordLambda
 * Featuring code from: m1enkrafftman
 * Date: 3/20/2015
 * Project: Warden
 * Usage: Some Block Utils
 */
public class BlockUtilities {

  private static BlockUtilities instance;
  private LinkedHashMap<UUID, List<BlockBreak>> brokenBlocks;

  public Timer getPlayerNBlockBreakTimer(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    if(breaks != null) {
      return breaks.get(breaks.size()).getTwl();
    }
    return null;
  }

  public TimerWithLoc getPlayerNBlockBreakTWL(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    if(breaks != null) {
      return breaks.get(breaks.size()).getTwl();
    }
    return null;
  }

  public Timer getPlayerNMinusOneBlockBreakTimer(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null ? null : breaks.get(breaks.size() - 1).getTwl();
  }

  public TimerWithLoc getPlayerNMinusOneBlockBreakTWL(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null ? null : breaks.get(breaks.size() - 1).getTwl();
  }

  public Timer getPlayerBlockBreakTimer(UUID u, int i) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null || breaks.size() < i ? null : breaks.get(i).getTwl();
  }

  public TimerWithLoc getPlayerBlockBreakTWL(UUID u, int i) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null || breaks.size() < i ? null : breaks.get(i).getTwl();
  }

  public BlockBreak getPlayerNBlockBreak(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null ? null : breaks.get(breaks.size());
  }

  public BlockBreak getPlayerNMinusOneBlockBreak(UUID u) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null ? null : breaks.get(breaks.size() - 1);
  }

  public BlockBreak getPlayerBlockBreak(UUID u, int i) {
    List<BlockBreak> breaks = brokenBlocks.get(u);
    return breaks == null || breaks.size() < i ? null : breaks.get(i);
  }

  public long getNumberOfBlocksBrokenByPlayer(UUID u) {
    List<BlockBreak> blockBreaks = brokenBlocks.get(u);
    return blockBreaks == null ? -1 : blockBreaks.size();
  }

  protected BlockUtilities() {
    brokenBlocks = new LinkedHashMap<UUID, List<BlockBreak>>();
  }

  public static BlockUtilities get() {
    if(instance == null) {
      synchronized (BlockUtilities.class) {
        if(instance == null) {
          instance = new BlockUtilities();
        }
      }
    }
    return instance;
  }

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


}
