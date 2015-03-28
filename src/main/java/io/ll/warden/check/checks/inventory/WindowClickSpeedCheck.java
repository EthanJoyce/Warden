package io.ll.warden.check.checks.inventory;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.injector.GamePhase;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
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
 * Usage: Checks for how fast your clicking a window.
 *
 * Look. These people get no slacks. At all. They have hacks like ChestStealer, and such.
 * This is basically just them automating clicking things out of a chest. Well. We want to
 * try, and nerf this advantage as much as possible. Average people click around 11.5 times
 * per second (gamers that is). So if we make sure you can't click faster than that. We've
 * nerfed some originally players. But we've made it an even playing field. The fact it can nerf
 * normal players is what makes it so so so low.
 */
public class WindowClickSpeedCheck extends Check implements PacketListener {

  private ListeningWhitelist in;
  private ListeningWhitelist out;
  private HashMap<UUID, Timer> map;

  public WindowClickSpeedCheck() {
    super("WindowClickSpeedCheck");
    out = ListeningWhitelist.EMPTY_WHITELIST;
    in = ListeningWhitelist.newBuilder().gamePhase(GamePhase.PLAYING).highest().build();
    map = new HashMap<UUID, Timer>();
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    w.getProtocolManager().addPacketListener(this);
  }

  @Override
  public float getRaiseLevel() {
    return 0.3f;
  }

  @Override
  public void onPacketSending(PacketEvent packetEvent) {
  }

  @Override
  public void onPacketReceiving(PacketEvent event) {
    PacketType pt = event.getPacketType();
    if (shouldCheckPlayer(event.getPlayer().getUniqueId())) {
      if (pt == PacketType.Play.Client.WINDOW_CLICK) {
        Player p = event.getPlayer();
        if (!map.containsKey(p.getUniqueId())) {
          map.put(p.getUniqueId(), new Timer());
        } else {
          Timer t = map.get(p.getUniqueId());
          if (!t.hasReachMS(87)) {
            Bukkit.getPluginManager().callEvent(new CheckFailedEvent(
                p.getUniqueId(), getRaiseLevel(), getName()
            ));
          } else {
            t.reset();
          }
        }
      } else if (pt == PacketType.Play.Client.ENTITY_ACTION) {
        Player p = event.getPlayer();
        PacketContainer pc = event.getPacket();
        int actionID = pc.getIntegers().read(2);
        if (actionID == 6) {
          map.put(p.getUniqueId(), new Timer());
        }
      } else if (pt == PacketType.Play.Client.CLOSE_WINDOW) {
        Player p = event.getPlayer();
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
    return in;
  }

  @Override
  public Plugin getPlugin() {
    return Warden.get();
  }
}
