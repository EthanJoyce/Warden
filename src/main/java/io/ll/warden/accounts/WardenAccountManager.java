package io.ll.warden.accounts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.storage.Database;

/**
 * Author: LordLambda
 * Date: 3/17/2015
 * Project: Warden
 * Usage: The account manager
 */
public class WardenAccountManager implements CommandExecutor, Listener {

  private static WardenAccountManager manager;
  private List<WardenAccount> wardenAccounts;
  private boolean shouldHaveAccounts;
  private Database db;

  protected WardenAccountManager() {
    shouldHaveAccounts = Warden.get().getConfig().getBoolean("UseWardenAccounts");
    wardenAccounts = new ArrayList<WardenAccount>();
    db = null;
  }

  public boolean hasWardenAccounts() {
    return shouldHaveAccounts;
  }

  public boolean playerHasWardenAccount(UUID uuid) {
    for(WardenAccount wa : wardenAccounts) {
      if(wa.getPlayerUUID().equals(uuid)) {
        return true;
      }
    }
    return false;
  }

  public void setDB(Database db) {
    this.db = db;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {

    return true;
  }

  @EventHandler
  public void playerLoginEvent(PlayerLoginEvent event) {

  }

  public static WardenAccountManager get() {
    if(manager == null) {
      synchronized (WardenAccountManager.class) {
        if(manager == null) {
          manager = new WardenAccountManager();
        }
      }
    }
    return manager;
  }
}
