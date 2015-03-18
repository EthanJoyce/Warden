package io.ll.warden.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import io.ll.warden.Warden;
import io.ll.warden.accounts.WardenAccount;

/**
 * Creator: LordLambda
 * Date: 3/16/2015
 * Project: Warden
 * Usage: Authenticate action
 */
public class AuthAction implements CommandExecutor {

  private List<WardenAccount> toAuthBackTooAccounts;
  private static AuthAction instance;

  protected AuthAction() {
    toAuthBackTooAccounts = new ArrayList<WardenAccount>();
  }

  public static AuthAction get() {
    if(instance == null) {
      synchronized (AuthAction.class) {
        if(instance == null) {
          instance = new AuthAction();
        }
      }
    }
    return instance;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
    if(!(sender instanceof Player)) {
      Warden.get().log("Console can't execute This command.");
      return true;
    }
    for(WardenAccount wa : toAuthBackTooAccounts) {
      Player p = (Player) sender;
      if(!(p.getUniqueId() == wa.getPlayerUUID())) {
        continue;
      }
      List<AuthCallback> toAuthBackToo = wa.getVerifications();
      if (toAuthBackToo.size() == 0) {
        sender.sendMessage("[Warden] You have nothing to verify!");
        return true;
      } else {
        for (AuthCallback ac : toAuthBackToo) {
          if (ac.getAuthBackName().equalsIgnoreCase(args[0])) {
            ac.onCallback(wa.getLevel());
            wa.remCallback(ac);
            if(wa.getVerifications().size() == 0) {
              toAuthBackTooAccounts.remove(wa);
            }
            return true;
          }
        }
        sender.sendMessage("[Warden] Couldn't find that command to verify?");
      }
    }
    return true;
  }

  public void registerAuthCallback(WardenAccount wa, AuthCallback ac) {
    wa.addCallback(ac);
    if(!toAuthBackTooAccounts.contains(wa)) {
      toAuthBackTooAccounts.add(wa);
    }
  }

  public interface AuthCallback {
    public String getAuthBackName();
    public void onCallback(AuthLevel level);
  }

  public enum AuthLevel {
    NONE, //If the player failed authentication
    USER, //If the player is just a general user
    MODERATOR, //If the player is a moderator
    ADMIN, //If the player is an admin
    OWNER
  }
}
