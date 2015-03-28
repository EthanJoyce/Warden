package io.ll.warden.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.reflect.StructureModifier;

import org.apache.commons.lang.BitField;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.events.FallEvent;
import io.ll.warden.events.PlayerTrueMoveEvent;

/**
 * Author: LordLambda
 * Date: 3/20/2015
 * Project: Warden
 * Usage: Helps with movement
 */
public class MovementHelper {

  private static MovementHelper instance;
  private LinkedHashMap<UUID, List<Location>> playerLocationMap;
  private LinkedHashMap<UUID, Location> startFalling;
  private List<UUID> onGroundPlayers;
  private List<UUID> fallingPlayers;
  private List<UUID> sprintingPlayers;
  private ListeningWhitelist outBound;
  private ListeningWhitelist inBound;

  protected MovementHelper() {
    playerLocationMap = new LinkedHashMap<UUID, List<Location>>();
    startFalling = new LinkedHashMap<UUID, Location>();
    inBound = ListeningWhitelist.newBuilder()
        .highest().gamePhase(GamePhase.PLAYING).build();
    outBound = ListeningWhitelist.newBuilder()
        .highest().gamePhase(GamePhase.PLAYING).build();
    onGroundPlayers = new ArrayList<UUID>();
    fallingPlayers = new ArrayList<UUID>();
    sprintingPlayers = new ArrayList<UUID>();
  }

  public static MovementHelper get() {
    if (instance == null) {
      synchronized (MovementHelper.class) {
        if (instance == null) {
          instance = new MovementHelper();
        }
      }
    }
    return instance;
  }

  /**
   * Get the players latest location
   *
   * @param uuid The player
   * @return The players latest location
   */
  public Location getPlayerNLocation(UUID uuid) {
    List<Location> l = playerLocationMap.get(uuid);
    return l.get(l.size());
  }

  /**
   * Get the players second to last location
   *
   * @param uuid The player
   * @return The players second to last location.
   */
  public Location getPlayerNMinusOneLocation(UUID uuid) {
    List<Location> l = playerLocationMap.get(uuid);
    return l.get(l.size() - 1);
  }

  /**
   * Check if a player is falling
   *
   * @param uuid the player
   * @return If mentioned player is falling
   */
  public boolean isFalling(UUID uuid) {
    return fallingPlayers.contains(uuid);
  }

  /**
   * Check if the player is on the ground.
   *
   * @param uuid The player
   * @return If the player is on the ground.
   */
  public boolean isOnGround(UUID uuid) {
    return onGroundPlayers.contains(uuid);
  }

  /**
   * Check if the player is sprinting
   *
   * @param uuid The player
   * @return If formentioned player is sprinting.
   */
  public boolean isSprinting(UUID uuid) {
    return sprintingPlayers.contains(uuid);
  }

  public void setProtocolManager(ProtocolManager pm) {
    pm.addPacketListener(new PacketListener() {
      @Override
      public void onPacketSending(PacketEvent packetEvent) {
        PacketType pt = packetEvent.getPacketType();
        if (pt == PacketType.Play.Server.SPAWN_POSITION) {
          Player p = packetEvent.getPlayer();
          if (playerLocationMap.containsKey(p.getUniqueId())) {
            playerLocationMap.remove(p.getUniqueId());
          }
          List<Location> newList = new ArrayList<Location>();
          //Grab location
          long val = packetEvent.getPacket().getLongs().read(1);
          //Decode it
          double x = val >> 38;
          double y = (val >> 26) & 0xFFF;
          double z = val << 38 >> 38;
          Location l = new Location(p.getWorld(), x, y, z);

          newList.add(l);
          playerLocationMap.put(p.getUniqueId(), newList);
        } else if (pt == PacketType.Play.Server.POSITION) {
          Player p = packetEvent.getPlayer();
          List<Location> list = playerLocationMap.get(p.getUniqueId());

          PacketContainer pc = packetEvent.getPacket();
          StructureModifier<Double> doubles = pc.getDoubles();

          double x = doubles.read(1);
          double y = doubles.read(2);
          double z = doubles.read(3);

          BitField bf = new BitField(pc.getBytes().read(6));
          boolean xRelative = bf.isSet(0x01);
          boolean yRelative = bf.isAllSet(0x02);
          boolean zRelative = bf.isAllSet(0x03);

          Location loc = list.get(list.size());

          double trueX = xRelative ? x + loc.getX() : x;
          double trueY = yRelative ? y + loc.getY() : y;
          double trueZ = zRelative ? z + loc.getZ() : z;

          list.add(new Location(p.getWorld(), trueX, trueY, trueZ));
          playerLocationMap.put(p.getUniqueId(), list);
          Bukkit.getServer().getPluginManager()
              .callEvent(new PlayerTrueMoveEvent(p));
        }
      }

      @Override
      public void onPacketReceiving(PacketEvent packetEvent) {
        PacketType pt = packetEvent.getPacketType();
        if (pt == PacketType.Play.Client.POSITION) {
          Player p = packetEvent.getPlayer();
          boolean onGround = packetEvent.getPacket()
              .getBooleans().read(4);
          if (onGround) {
            if (fallingPlayers.contains(p.getUniqueId())) {
              fallingPlayers.remove(p.getUniqueId());
              Location l = startFalling.get(p.getUniqueId());
              startFalling.remove(p.getUniqueId());
              if(l.getY() - getPlayerNLocation(p.getUniqueId()).getY() > 1) {
                Bukkit.getPluginManager().callEvent(new FallEvent(
                    p, l, getPlayerNLocation(p.getUniqueId())
                ));
              }
            }
            onGroundPlayers.add(p.getUniqueId());
          } else {
            if (onGroundPlayers.contains(p.getUniqueId())) {
              onGroundPlayers.remove(p.getUniqueId());
              startFalling.put(p.getUniqueId(), getPlayerNLocation(p.getUniqueId()));
            }
            fallingPlayers.add(p.getUniqueId());
          }
        } else if (pt == PacketType.Play.Client.ENTITY_ACTION) {
          Player p = packetEvent.getPlayer();
          PacketContainer pc = packetEvent.getPacket();
          int id = pc.getIntegers().read(2);
          if (id == 3) {
            if (!sprintingPlayers.contains(p.getUniqueId())) {
              sprintingPlayers.add(p.getUniqueId());
            }
          } else if (id == 4) {
            if (sprintingPlayers.contains(p.getUniqueId())) {
              sprintingPlayers.remove(p.getUniqueId());
            }
          }
        }
      }

      @Override
      public ListeningWhitelist getSendingWhitelist() {
        return outBound;
      }

      @Override
      public ListeningWhitelist getReceivingWhitelist() {
        return inBound;
      }

      @Override
      public Plugin getPlugin() {
        return Warden.get();
      }
    });
  }
}
