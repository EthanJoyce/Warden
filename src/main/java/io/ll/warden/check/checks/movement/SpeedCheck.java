package io.ll.warden.check.checks.movement;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedHashMap;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.check.Check;
import io.ll.warden.events.PlayerTrueMoveEvent;
import io.ll.warden.utils.BlockUtilities;
import io.ll.warden.utils.MovementHelper;
import io.ll.warden.utils.Timer;

/**
 * Author: LordLambda
 * Date: 3/21/2015
 * Project: Warden
 * Usage: A speed check
 */
public class SpeedCheck extends Check implements Listener {

  private double walkSpeed;
  private double sneakSpeed;
  private double sprintSpeed;
  private LinkedHashMap<UUID, Timer> map;

  public SpeedCheck() {
    super("SpeedCheck");
    map = new LinkedHashMap<UUID, Timer>();
  }

  @Override
  public void registerListeners(Warden w, PluginManager pm) {
    walkSpeed = w.getConfig().getDouble("WALKDISTANCE");
    sneakSpeed = w.getConfig().getDouble("SNEAKDISTANCE");
    sprintSpeed = w.getConfig().getDouble("SPRINTDISTANCE");
    pm.registerEvents(this, w);
  }

  @Override
  public float getRaiseLevel() {
    return 1.55f;
  }

  @EventHandler
  public void onMoveEvent(PlayerTrueMoveEvent event) {
    Player p = event.getPlayer();
    Timer t = map.get(p.getUniqueId());
    if(t.hasReach(2)) {
      t.stop();
      long timePassed = t.getFinalCheck() - t.getLastCheck();
      t.reset();

      if(p.isInsideVehicle()) {
        return;
      }

      double multi = getSpeedMultiplier(p);
      boolean inWeb = BlockUtilities.get().isPlayerInWeb(p);

      if(!(p.getGameMode() == GameMode.CREATIVE)) {

      }else {
        //Should I even do this?
      }
    }
  }

  public float getSpeedAmplifier(Player p) {
    float toReturn = 1F;

    for (PotionEffect effect : p.getActivePotionEffects()) {
      if (effect.getType() == PotionEffectType.SPEED) {
        toReturn *= 1.0f + (0.2f * (effect.getAmplifier() + 1));
      }
      if (effect.getType() == PotionEffectType.SLOW) {
        toReturn *= 1.0f - (0.15f * (effect.getAmplifier() + 1));
      }
    }
    return toReturn;
  }

  public double getSpeedMultiplier(Player p) {
    double multi = 1.0;
    if(BlockUtilities.get().isOnIce(p)) {
      multi *= 1.4;
    }
    boolean inWeb = BlockUtilities.get().isPlayerInWeb(p);
    if(inWeb) {
      multi *= 0.12;
    }
    if(!MovementHelper.get().isOnGround(p.getUniqueId())) {
      multi *= 1.5;
    }
    multi *= getSpeedAmplifier(p);
    if(p.isBlocking()) {
      multi *= 0.5;
    }
    return multi;
  }
}
