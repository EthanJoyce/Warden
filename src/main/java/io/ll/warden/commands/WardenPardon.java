package io.ll.warden.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import io.ll.warden.Warden;
import io.ll.warden.accounts.WardenAccount;
import io.ll.warden.accounts.WardenAccountManager;
import io.ll.warden.utils.UUIDFetcher;

/**
 * Creator: LordLambda
 * Date: 3/25/2015
 * Project: Warden
 * Usage: Warden UnBanning
 */
public class WardenPardon implements CommandExecutor, AuthAction.AuthCallback {

  private HashMap<UUID, UUID> waitingBanAuth;

  public WardenPardon() {
    waitingBanAuth = new HashMap<UUID, UUID>();
  }

  @Override
  public String getAuthBackName() {
    return "WardenPardon";
  }

  @Override
  public void onCallback(UUID uuid, AuthAction.AuthLevel level) {
    UUID toUnban = waitingBanAuth.get(uuid);
    waitingBanAuth.remove(uuid);
    //TODO: Go through heuristics to unban.
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
    if(!(sender instanceof Player)) {
      Warden.get().log("WardenPardon can only be called by a player [With appropriate perms]!");
      return true;
    }
    Player pSender = (Player) sender;
    if(!WardenAccountManager.get().playerHasWardenAccount(pSender.getUniqueId())) {
      return true;
    }
    WardenAccount wardenAccount = WardenAccountManager.get()
        .getWardenAccount(pSender.getUniqueId());
    if(wardenAccount.getLevel() != AuthAction.AuthLevel.ADMIN ||
        wardenAccount.getLevel() != AuthAction.AuthLevel.OWNER) {
      pSender.sendMessage("This command can only be called by Admins or Owners.");
    }
    if(!(args.length == 1)) {
      pSender.sendMessage("You must only specify a player name to be unbanned!");
    }
    String playerName = args[0];
    UUID toUnBan;
    try {
      toUnBan = UUIDFetcher.getUUIDOf(playerName);
      //TODO: Check if player was banned by warden
    }catch(Exception e1) {
      pSender.sendMessage("Failed to grab players UUID!");
      e1.printStackTrace();
      return true;
    }
    waitingBanAuth.put(pSender.getUniqueId(), toUnBan);
    wardenAccount.addCallback(this);
    AuthAction.get().registerAuthCallback(wardenAccount, this);
    return true;
  }
}
