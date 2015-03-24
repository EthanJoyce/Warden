package io.ll.warden.check.checks.inventory;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.injector.GamePhase;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;
import io.ll.warden.events.CheckFailedEvent;

/**
 * Author: LordLambda
 * Date: 3/19/2015
 * Project: Warden
 * Usage: A check for XCarry
 *
 * This check works really simply. XCarry works by just never sending the close inventory packet. So
 * by simply checking if the player is moving/looking around while their inventory is open we can
 * determine that they're using XCarry because a in a vanilla game you can't do that.
 */
public class XCarryCheck extends Check implements PacketListener {

  private List<UUID> map;
  private ListeningWhitelist lw;
  private ListeningWhitelist out;

  public XCarryCheck() {
    super("XCarryCheck");
    map = new ArrayList<UUID>();
    lw = ListeningWhitelist.newBuilder()
        .highest().gamePhase(GamePhase.PLAYING).build();
    out = ListeningWhitelist.EMPTY_WHITELIST;
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    setup(w);
  }

  @Override
  public float getRaiseLevel() {
    return 5.0f;
  }

  /**
   * Set up the packet sniffers
   *
   * @param w Release the sniffing hounds!
   */
  public void setup(Warden w) {
    ProtocolManager pm = w.getProtocolManager();
    pm.addPacketListener(this);
  }

  @Override
  public void onPacketSending(PacketEvent packetEvent) {
    //We don't need to do anything here
  }

  @Override
  public void onPacketReceiving(PacketEvent event) {
    PacketType pt = event.getPacketType();
    //A player's inventory will actually close if he gets hit before this event is fired off.
    //So no need to check for hits.
    if (pt == PacketType.Play.Client.POSITION) {
      Player p = event.getPlayer();
      if (map.contains(p.getUniqueId())) {
        Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
            p.getUniqueId(), getRaiseLevel(), getName()
        ));
      }
    } else if (pt == PacketType.Play.Client.LOOK) {
      Player p = event.getPlayer();
      if (map.contains(p.getUniqueId())) {
        Bukkit.getServer().getPluginManager().callEvent(new CheckFailedEvent(
            p.getUniqueId(), getRaiseLevel(), getName()
        ));
      }
    } else if (pt == PacketType.Play.Client.ENTITY_ACTION) {
      Player p = event.getPlayer();
      PacketContainer pc = event.getPacket();
      int actionID = pc.getIntegers().read(2);
      if (actionID == 6) {
        map.add(p.getUniqueId());
      }
    } else if (pt == PacketType.Play.Client.CLOSE_WINDOW) {
      Player p = event.getPlayer();
      if (map.contains(p.getUniqueId())) {
        map.remove(p.getUniqueId());
      }
    }
  }

  @Override
  public ListeningWhitelist getSendingWhitelist() {
    return out;
  }

  @Override
  public ListeningWhitelist getReceivingWhitelist() {
    return lw;
  }

  @Override
  public Plugin getPlugin() {
    return Warden.get();
  }
}
